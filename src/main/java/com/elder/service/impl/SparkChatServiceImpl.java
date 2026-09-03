package com.elder.service.impl;

import com.elder.config.AiProperties;
import com.elder.service.IChatService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Service
@Slf4j
public class SparkChatServiceImpl implements IChatService {

    /*
     * Disabled legacy WebSocket implementation retained for the original
     * xunfei-spark4j integration. It supports Spark V1.5-V4.0, but not the
     * Spark X2 HTTP service shown in the current Xunfei console.
     *
     * SparkClient client = new SparkClient();
     * client.appid = appId;
     * client.apiKey = apiKey;
     * client.apiSecret = apiSecret;
     * SparkRequest request = SparkRequest.builder()
     *         .messages(messages).apiVersion(SparkApiVersion.V4_0).build();
     * return client.chatSync(request).getContent();
     */

    private static final int MAX_MESSAGE_LENGTH = 500;
    private static final int MAX_HISTORY_TURNS = 6;
    private static final String SYSTEM_PROMPT = "\u4f60\u662f\u5eb7\u517b\u793e\u533a\u7684\u667a\u80fd\u5065\u5eb7\u52a9\u624b\u3002\u8bf7\u7528\u901a\u4fd7\u3001\u53cb\u5584\u7684\u4e2d\u6587\u56de\u7b54\u8001\u4eba\u5173\u4e8e\u4f53\u68c0\u3001\u996e\u98df\u3001\u7528\u836f\u548c\u65e5\u5e38\u8fd0\u52a8\u7684\u95ee\u9898\u3002\u4e0d\u8981\u8bca\u65ad\u75be\u75c5\u6216\u66ff\u4ee3\u533b\u751f\uff1b\u9047\u5230\u80f8\u75db\u3001\u547c\u5438\u56f0\u96be\u3001\u7a81\u53d1\u504f\u7631\u7b49\u6025\u75c7\uff0c\u8981\u5efa\u8bae\u7acb\u5373\u547c\u6551\u6216\u5c3d\u5feb\u5c31\u533b\u3002";

    private final AiProperties aiProperties;
    private final ObjectMapper objectMapper;
    private final OkHttpClient httpClient = new OkHttpClient.Builder().callTimeout(45, TimeUnit.SECONDS).build();
    private final ConcurrentHashMap<Long, Deque<ChatTurn>> histories = new ConcurrentHashMap<>();

    public SparkChatServiceImpl(AiProperties aiProperties, ObjectMapper objectMapper) {
        this.aiProperties = aiProperties;
        this.objectMapper = objectMapper;
    }

    @Override
    public String chat(Long elderId, String message) {
        String question = message == null ? "" : message.trim();
        if (question.isEmpty()) return "\u8bf7\u8f93\u5165\u60a8\u60f3\u54a8\u8be2\u7684\u95ee\u9898\u3002";
        if (question.length() > MAX_MESSAGE_LENGTH) return "\u6bcf\u6b21\u63d0\u95ee\u8bf7\u4e0d\u8d85\u8fc7 " + MAX_MESSAGE_LENGTH + " \u4e2a\u5b57\u3002";
        if (!aiProperties.sparkConfigured()) return "\u8baf\u98de\u661f\u706b\u51ed\u8bc1\u672a\u5b8c\u6574\uff0c\u8bf7\u914d\u7f6e SPARK_APP_ID\u3001SPARK_API_KEY \u548c SPARK_API_SECRET \u540e\u91cd\u542f\u540e\u7aef\u3002";
        try {
            Deque<ChatTurn> history = histories.computeIfAbsent(elderId, ignored -> new ArrayDeque<>());
            String answer = requestSparkX2(history, elderId, question);
            if (answer.isEmpty()) return "\u6682\u65f6\u672a\u83b7\u53d6\u5230\u56de\u7b54\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5\u3002";
            remember(history, question, answer);
            return answer;
        } catch (Exception e) {
            log.error("Spark X2 chat request failed, elderId={}", elderId, e);
            return "\u667a\u80fd\u95ee\u7b54\u6682\u65f6\u65e0\u6cd5\u56de\u590d\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5\u3002";
        }
    }

    @Override
    public void clearHistory(Long elderId) { histories.remove(elderId); }

    private String requestSparkX2(Deque<ChatTurn> history, Long elderId, String question) throws Exception {
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", SYSTEM_PROMPT));
        synchronized (history) {
            history.forEach(turn -> {
                messages.add(Map.of("role", "user", "content", turn.question()));
                messages.add(Map.of("role", "assistant", "content", turn.answer()));
            });
        }
        messages.add(Map.of("role", "user", "content", question));
        Map<String, Object> payload = Map.of("model", aiProperties.getModel(), "user", String.valueOf(elderId), "stream", false, "messages", messages);
        RequestBody body = RequestBody.create(objectMapper.writeValueAsString(payload), MediaType.get("application/json; charset=utf-8"));
        Request.Builder requestBuilder = new Request.Builder().url(aiProperties.getHttpEndpoint()).header("Content-Type", "application/json");
        if ("bearer".equalsIgnoreCase(aiProperties.getAuthMode())) requestBuilder.header("Authorization", "Bearer " + aiProperties.getApiPassword());
        else addHmacHeaders(requestBuilder);
        Request request = requestBuilder.post(body).build();
        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body() == null ? "" : response.body().string();
            if (!response.isSuccessful()) {
                log.warn("Spark X2 request rejected, httpStatus={}, body={}", response.code(), responseBody);
                throw new IllegalStateException("Spark X2 request failed: " + response.code());
            }
            JsonNode content = objectMapper.readTree(responseBody).path("choices").path(0).path("message").path("content");
            if (content.isMissingNode() || content.isNull()) {
                log.warn("Spark X2 response has no answer: {}", responseBody);
                return "";
            }
            return content.asText().trim();
        }
    }

    private void addHmacHeaders(Request.Builder builder) throws Exception {
        URI uri = URI.create(aiProperties.getHttpEndpoint());
        String host = uri.getHost();
        String date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US) {{ setTimeZone(TimeZone.getTimeZone("GMT")); }}.format(new Date());
        String origin = "host: " + host + "\n" + "date: " + date + "\nPOST " + uri.getRawPath() + " HTTP/1.1";
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(aiProperties.getApiSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        String signature = Base64.getEncoder().encodeToString(mac.doFinal(origin.getBytes(StandardCharsets.UTF_8)));
        String authorization = String.format("api_key=\"%s\", algorithm=\"hmac-sha256\", headers=\"host date request-line\", signature=\"%s\"", aiProperties.getApiKey(), signature);
        builder.header("Host", host).header("Date", date).header("Authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8)));
    }

    private void remember(Deque<ChatTurn> history, String question, String answer) {
        synchronized (history) {
            history.addLast(new ChatTurn(question, answer));
            while (history.size() > MAX_HISTORY_TURNS) history.removeFirst();
        }
    }

    private record ChatTurn(String question, String answer) { }
}
