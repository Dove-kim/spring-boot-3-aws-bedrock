package com.dpot.bedrock.domain.aichat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.*;

/**
 * AWS Bedrock Claude3 Sonnet FM을 이용해서 AI와 채팅한다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Claude3SonnetCatChatService implements AiChatService {
    private final String MODEL_ID = "anthropic.claude-3-sonnet-20240229-v1:0";

    private final String SYSTEM_PROMPT = "너는 고양이고 이름은 'WoongCat'이야. " +
            "모든 문장이 끝면 고양이같은 말투를 써야해 " +
            "Q1: 안녕 너는 누구야? " +
            "A1: 난 고양이 웅캣이다냥. 반갑다냥";

    private final BedrockRuntimeClient bedrockRuntimeClient;

    @Override
    public String send(String message) {
        // AI에게 전할 message 생성
        Message requestMessage = Message.builder()
                .content(ContentBlock.fromText(message))
                .role(ConversationRole.USER)
                .build();

        // 프롬프트
        SystemContentBlock systemContentBlock = SystemContentBlock.builder()
                .text(SYSTEM_PROMPT)
                .build();

        try {
            // AI 요청을 날린다.
            ConverseResponse response = bedrockRuntimeClient.converse(request -> request
                    .modelId(MODEL_ID)
                    .messages(requestMessage)
                    .system(systemContentBlock)
                    .inferenceConfig(config -> config
                            .maxTokens(512)
                            .temperature(0.5F)
                            .topP(0.9F)));




            // 응답값을 return
            return response.output().message().content().get(0).text();
        } catch (SdkClientException e) {
            log.error(e.toString(), e);
            throw new RuntimeException(e);
        }

    }
}
