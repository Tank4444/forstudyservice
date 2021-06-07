package ru.chuikov.study.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.chuikov.study.entity.chatModel.ChatMessage;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

    @Query("select u from ChatMessage u where u.chatRoom.id = ?1 ")
    Optional<List<ChatMessage>> findChatMessagesByChatRoom(Long id, Pageable pageable);

    @Query("select u from ChatMessage u where u.sender.id = ?1 ")
    Optional<List<ChatMessage>> findChatMessagesByUserId(Long id,Pageable pageable);

}
