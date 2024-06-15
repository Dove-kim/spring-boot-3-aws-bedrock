package com.dpot.bedrock.domain.aichat.controller;

/**
 * Chat 요청 body
 * @param message
 */
public record ChatRequest (
        String message
){
}
