package com.elder.service;

import reactor.core.publisher.Flux;

public interface IChatService {
    String chat(Long elderId, String message);
    Flux<String> chatStream(Long elderId, String message);
    void clearHistory(Long elderId);
}
