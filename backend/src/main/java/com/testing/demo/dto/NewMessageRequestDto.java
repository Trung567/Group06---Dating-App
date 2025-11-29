package com.testing.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewMessageRequestDto {
    private String conversationId;
    private String content;
}