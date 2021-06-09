package ru.chuikov.study.service;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.chuikov.study.entity.Role;
import ru.chuikov.study.entity.User;
import ru.chuikov.study.entity.UserStatus;

public interface UserService extends UserDetailsService {

    User getUserById(Long id);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    Page<User> getUsers(int from, int count);
    void addUser(User user) throws Exception;
    void updateUser(User user) throws Exception;
    void changeUsername(User user) throws Exception;
    void changeUserEmail(User user) throws Exception;
    boolean emailBusy(String email);
    boolean usernameBusy(String username);
    void changeUserStatusTo(User user, UserStatus status) throws Exception;
    void deleteUserById(Long id)throws Exception;
    void changeUserRoleTo(Long id, Role role) throws Exception;
}
