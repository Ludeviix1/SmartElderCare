package com.elder.controller.app;

import com.elder.pojo.dto.ChatRequestDTO;
import com.elder.service.IChatService;
import com.elder.util.JwtUtil;
import com.elder.util.Result;
import org.springframework.http.MediaType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/app/chat")
@ConditionalOnProperty(name = "spring.ai.dashscope.enabled", havingValue = "true")
public class AppChatController {
    private final IChatService chatService;

    public AppChatController(IChatService chatService) { this.chatService = chatService; }

    @PostMapping
    public Result<String> chat(@RequestHeader("Authorization") String token, @RequestBody ChatRequestDTO request) {
        return Result.ok(chatService.chat(getElderId(token), request == null ? null : request.getMessage()));
    }

    @PostMapping(value = "/chatStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public reactor.core.publisher.Flux<String> chatStream(@RequestHeader("Authorization") String token, ChatRequestDTO request) {
        return chatService.chatStream(getElderId(token), request == null ? null : request.getMessage());
    }

    @DeleteMapping("/history")
    public Result<Void> clearHistory(@RequestHeader("Authorization") String token) {
        chatService.clearHistory(getElderId(token));
        return Result.ok("\u5bf9\u8bdd\u8bb0\u5f55\u5df2\u6e05\u7a7a");
    }

    private Long getElderId(String token) {
        Map<String, Object> claims = JwtUtil.parseToken(token);
        return ((Number) claims.get("id")).longValue();
    }
}
