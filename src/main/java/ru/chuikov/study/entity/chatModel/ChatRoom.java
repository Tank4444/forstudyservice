package ru.chuikov.study.entity.chatModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.chuikov.study.entity.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "CHATROOM")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoom {
    @Id
    private long id;

    @Column
    private String name;

    @ManyToMany
    @JoinTable(name = "USERACC_CHATROOM",
            joinColumns = @JoinColumn(name = "USERACC_ID"),
            inverseJoinColumns = @JoinColumn(name = "CHATROOM_ID")
    )
    @JsonIgnore
    private List<User> users;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @OneToMany
    @JoinColumn(name = "CHATROOM_ID")
    @JsonIgnore
    private List<ChatMessage> message;

    @Enumerated(EnumType.STRING)
    private ChatStatus status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<ChatMessage> getMessage() {
        return message;
    }

    public void setMessage(List<ChatMessage> message) {
        this.message = message;
    }

    public ChatStatus getStatus() {
        return status;
    }

    public void setStatus(ChatStatus status) {
        this.status = status;
    }
}
