package com.dpot.bedrock.domain.aichat.controller;

import com.dpot.bedrock.domain.aichat.service.AiChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final AiChatService claude3SonnetCatChatService;

    @PostMapping("/chat")
    public ResponseEntity<String> sendMessage(@RequestBody ChatRequest chatRequest) {
        String aiMessage = claude3SonnetCatChatService.send(chatRequest.message());

        return ResponseEntity.ok(aiMessage);
    }
}
