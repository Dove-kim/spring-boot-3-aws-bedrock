package com.dpot.bedrock.domain.aichat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

/**
 * AWS Bedrock Claude3 Sonnet FM을 이용해서 AI와 채팅한다.
 * With AWS Bedrock Guardrail
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Claude3SonnetCatChatWithGuardRailService implements AiChatService {
    private final String MODEL_ID = "anthropic.claude-3-sonnet-20240229-v1:0";

    private final String SYSTEM_PROMPT = "너는 고양이고 이름은 'WoongCat'이야. " +
            "모든 문장이 끝면 고양이같은 말투를 써야해 " +
            "Q1: 안녕 너는 누구야? " +
            "A1: 난 고양이 웅캣이다냥. 반갑다냥";

    private final BedrockRuntimeClient bedrockRuntimeClient;

    @Override
    public String send(String message) {
        // AI에게 전할 message 생성
        String nativeRequestTemplate = """
                {
                    "anthropic_version": "bedrock-2023-05-31",
                    "max_tokens": 512,
                    "temperature": 0.5,
                    "topP": 0.9,
                    "system": "{{system_prompt}}",
                    "messages": [{
                        "role": "user",
                        "content": "{{prompt}}"
                    }]
                }""";


        String nativeRequest = nativeRequestTemplate
                .replace("{{prompt}}", message)
                .replace("{{system_prompt}}", SYSTEM_PROMPT);

        try {

            // AI 요청을 날린다.
            InvokeModelResponse invokeModelResponse = bedrockRuntimeClient.invokeModel(InvokeModelRequest.builder()
                    .modelId(MODEL_ID)
                    .body(SdkBytes.fromUtf8String(nativeRequest))
                    .guardrailIdentifier("s3bowsoa37sy")
                    .guardrailVersion("DRAFT")
                    .build());

            // Decode the response body.
            JSONObject responseBody = new JSONObject(invokeModelResponse.body().asUtf8String());

            // 응답값을 return
            return responseBody.getJSONArray("content").getJSONObject(0).getString("text");
        } catch (SdkClientException e) {
            log.error(e.toString(), e);
            throw new RuntimeException(e);
        }

    }
}
