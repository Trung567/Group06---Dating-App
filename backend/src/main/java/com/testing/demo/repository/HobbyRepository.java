package com.testing.demo.repository;

import com.testing.demo.entity.Hobby;
import com.testing.demo.entity.UserPromptAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HobbyRepository extends JpaRepository<Hobby, String>{

}
