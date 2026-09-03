package com.elder.service;

public interface IChatService {
    String chat(Long elderId, String message);
    void clearHistory(Long elderId);
}
