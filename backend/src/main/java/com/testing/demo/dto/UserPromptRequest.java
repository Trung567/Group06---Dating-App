package com.testing.demo.dto;
import lombok.Data;

@Data
public class UserPromptRequest {
    private String promptId;
    private String answer;
}