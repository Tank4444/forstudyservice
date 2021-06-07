package ru.chuikov.study.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.chuikov.study.entity.chatModel.ChatMessage;
import ru.chuikov.study.entity.chatModel.ChatRoom;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "USERACC")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    //    @JoinTable(name = "useracc_chatroom",
    //            joinColumns = @JoinColumn(name = "useracc_id"),
    //            inverseJoinColumns = @JoinColumn(name = "chatroom_id")
    //    )
    @ManyToMany(mappedBy = "users")
    private List<ChatRoom> rooms;

    @OneToMany
    @JoinColumn(name = "USERACC_ID")
    @JsonIgnore
    private List<ChatMessage> message;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !getUserStatus().equals(UserStatus.BLOCKED);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !getUserStatus().equals(UserStatus.BLOCKED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return  !getUserStatus().equals(UserStatus.BLOCKED);
    }

    @Override
    public boolean isEnabled() {
        return !getUserStatus().equals(UserStatus.BLOCKED);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", status=" + userStatus +
                ", created=" + created +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<ChatRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<ChatRoom> rooms) {
        this.rooms = rooms;
    }

    public List<ChatMessage> getMessage() {
        return message;
    }

    public void setMessage(List<ChatMessage> message) {
        this.message = message;
    }
}
