package com.elder.service.impl;

import com.elder.service.IChatService;
import com.elder.service.IElderService;
import com.elder.tools.ElderTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

/** DashScope chat implementation following the reference project's Spring AI flow. */
@Service
@Slf4j
public class BailianChatServiceImpl implements IChatService {

    private static final int MAX_MESSAGE_LENGTH = 500;
    private static final String SYSTEM_PROMPT = "你是康养社区智能健康助手。请用通俗、友善的中文回答老人关于体检、饮食、用药和日常运动的问题。不要诊断疾病或替代医生；遇到胸痛、呼吸困难、突发偏瘫等急症，要建议立即呼救或尽快就医。";

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final IElderService elderService;

    public BailianChatServiceImpl(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, IElderService elderService) {
        this.chatClient = chatClientBuilder.build();
        this.chatMemory = chatMemory;
        this.elderService = elderService;
    }

    @Override
    public String chat(Long elderId, String message) {
        String question = message == null ? "" : message.trim();
        if (!StringUtils.hasText(question)) return "请输入您想咨询的问题。";
        if (question.length() > MAX_MESSAGE_LENGTH) return "每次提问请不超过 " + MAX_MESSAGE_LENGTH + " 个字。";
        try {
            return chatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(question)
                    .advisors(MessageChatMemoryAdvisor.builder(chatMemory)
                            .conversationId(String.valueOf(elderId))
                            .build())
                    .tools(new ElderTools(elderId, elderService))
                    .call()
                    .content();
        } catch (Exception e) {
            log.error("DashScope chat request failed, elderId={}", elderId, e);
            return "智能问答暂时无法回复，请稍后再试。";
        }
    }

    @Override
    public Flux<String> chatStream(Long elderId, String message) {
        String question = message == null ? "" : message.trim();
        if (!StringUtils.hasText(question)) return Flux.just("请输入您想咨询的问题。", "[END]");
        if (question.length() > MAX_MESSAGE_LENGTH) return Flux.just("每次提问请不超过 " + MAX_MESSAGE_LENGTH + " 个字。", "[END]");
        return chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(question)
                .advisors(MessageChatMemoryAdvisor.builder(chatMemory)
                        .conversationId(String.valueOf(elderId))
                        .build())
                .tools(new ElderTools(elderId, elderService))
                .stream()
                .content()
                .onErrorResume(e -> {
                    log.error("DashScope streaming chat request failed, elderId={}", elderId, e);
                    return Flux.just("智能问答暂时无法回复，请稍后再试。");
                })
                .concatWith(Flux.just("[END]"));
    }

    @Override
    public void clearHistory(Long elderId) {
        chatMemory.clear(String.valueOf(elderId));
    }
}
