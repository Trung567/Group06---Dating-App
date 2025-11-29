package com.testing.demo.repository;

import com.testing.demo.entity.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromptRepo extends JpaRepository<Prompt, String>{
    // (Giả sử khóa chính của Prompt cũng là String)
}