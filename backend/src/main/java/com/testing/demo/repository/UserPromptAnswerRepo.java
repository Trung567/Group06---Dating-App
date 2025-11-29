package com.testing.demo.repository;

import com.testing.demo.entity.UserPromptAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPromptAnswerRepo extends JpaRepository<UserPromptAnswer, String> {

    List<UserPromptAnswer> findAllByUser_UserId(String userId);
}