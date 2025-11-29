package com.testing.demo.repository;

import com.testing.demo.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, String> {

    // Tìm Conversation bằng MatchId
    Optional<Conversation> findByMatch_MatchId(String matchId);
}