package com.testing.demo.controller;

import com.testing.demo.entity.Hobby;
import com.testing.demo.entity.Prompt;
import com.testing.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicDataController {

    @Autowired
    private UserService profileService;

    @GetMapping("/hobbies")
    public ResponseEntity<List<Hobby>> getAllHobbies() {
        return ResponseEntity.ok(profileService.getAllHobbies());
    }

    @GetMapping("/prompts")
    public ResponseEntity<List<Prompt>> getAllPrompts() {
        return ResponseEntity.ok(profileService.getAllPrompts());
    }
}