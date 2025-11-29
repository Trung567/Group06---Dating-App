package com.testing.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SwipeRequestDto {
    private String swipedUserId; // ID của người BẠN quẹt
    private String action;       // "like" hoặc "dislike"
}