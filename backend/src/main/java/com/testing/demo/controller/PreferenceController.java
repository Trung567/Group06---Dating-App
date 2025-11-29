package com.testing.demo.controller;

import com.testing.demo.dto.PreferenceDto;
import com.testing.demo.service.PreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
public class PreferenceController {

    @Autowired
    private PreferenceService preferenceService;

    // GET /api/preferences/me
    @GetMapping("/me")
    public ResponseEntity<PreferenceDto> getMyPreference(Authentication authentication) {
        String email = authentication.getName();
        PreferenceDto dto = preferenceService.getMyPreference(email);
        return ResponseEntity.ok(dto);
    }

    // PUT /api/preferences/me
    @PutMapping("/me")
    public ResponseEntity<PreferenceDto> updateMyPreference(@RequestBody PreferenceDto dto,
                                                            Authentication authentication) {
        String email = authentication.getName();
        PreferenceDto updatedDto = preferenceService.updateMyPreference(email, dto);
        return ResponseEntity.ok(updatedDto);
    }
}