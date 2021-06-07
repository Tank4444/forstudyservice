package ru.chuikov.study.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.chuikov.study.entity.User;
import ru.chuikov.study.entity.UserStatus;
import ru.chuikov.study.repository.UserRepository;
import ru.chuikov.study.service.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(s);
        if (user.isEmpty()) throw new UsernameNotFoundException("User with username = "+s+" not found");
        return user.get();
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> user=userRepository.findUserById(id);
        if(user.isEmpty())throw new UsernameNotFoundException("User with id = "+id+" not found");
        else return user.get();
    }

    @Override
    public User getUserByUsername(String username) {
        Optional<User> user=userRepository.findUserByUsername(username);
        if(user.isEmpty())throw new UsernameNotFoundException("User with username = "+username+" not found");
        else return user.get();
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user=userRepository.findUserByEmail(email);
        if(user.isEmpty())throw new UsernameNotFoundException("User with email = "+email+" not found");
        else return user.get();
    }

    @Override
    public Page<User> getUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page,size));
    }

    @Override
    public void addUser(User user) throws Exception {
        if(usernameBusy(user.getUsername()))throw new Exception("username is busy");
        if(emailBusy(user.getUsername()))throw new Exception("email is busy");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
    }

    @Override
    public void updateUser(User user) throws Exception {
        userRepository.saveAndFlush(user);
    }

    @Override
    public void changeUsername(User user) throws Exception {
        if(!usernameBusy(user.getUsername()))throw new Exception("username is busy");
        userRepository.saveAndFlush(user);
    }

    @Override
    public void changeUserEmail(User user) throws Exception {
        if(!emailBusy(user.getUsername()))throw new Exception("email is busy");
        User acc=getUserById(user.getId());
        acc.setEmail(user.getEmail());
        userRepository.save(acc);
    }

    @Override
    public boolean emailBusy(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    @Override
    public boolean usernameBusy(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }

    @Override
    public void changeUserStatusTo(User user, UserStatus status) throws Exception {
        User acc=getUserById(user.getId());
        if(acc.getUserStatus()!=status)
        {
            acc.setUserStatus(status);
            userRepository.saveAndFlush(acc);
        }else throw new Exception("user status is the same");
    }
}


