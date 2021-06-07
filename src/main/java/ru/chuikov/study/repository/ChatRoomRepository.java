package ru.chuikov.study.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.chuikov.study.entity.chatModel.ChatRoom;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    @Query("select u from ChatRoom u where LOWER(u.name) = lower(?1) ")
    Optional<List<ChatRoom>> findChatRoomByName(String name, Pageable pageable);

    @Query("select u from ChatRoom u join u.users t where t.id =  ?1 ")
    Optional<List<ChatRoom>> findChatRoomsByUsersId(Long id, Pageable pageable);

    @Query("select u from ChatRoom u where u.id = ?1 ")
    Optional<ChatRoom> findChatRoomById(Long id);



}
