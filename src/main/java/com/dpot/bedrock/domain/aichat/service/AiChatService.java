package com.dpot.bedrock.domain.aichat.service;

/**
 * AI 메시지 전송 interface
 */
public interface AiChatService {
    /**
     * AI chat에게 메시지를 보낸다.
     * AI가 답변해준다.
     *
     * @param message 메시지
     * @return 응답 메시지
     */
    String send(String message);
}
