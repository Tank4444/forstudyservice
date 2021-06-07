package ru.chuikov.study.entity.chatModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.chuikov.study.entity.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CHATMESSAGE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    @Id
    private long id;

//    @ManyToOne
//    @JoinTable(name = "chatmessage_id")
//    private ChatRoom chatRoom;
//
//    private Long senderId;
//    private Long recipientId;
//    private String senderName;
//    private String recipientName;
//    private String content;
//    private Date timestamp;
//    private MessageStatus status;

    @ManyToOne
    @JoinTable(name = "CHATMESSAGE_ID")
    private ChatRoom chatRoom;

    @Column
    private String content;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @ManyToOne
    @JsonIgnore
    private User sender;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
