package com.testing.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreferenceDto {
    private Integer minAge;
    private Integer maxAge;
    private String interestedInGender; // "MALE", "FEMALE", "BOTH"
    private Integer maxDistance;
}