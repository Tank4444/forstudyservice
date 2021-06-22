package ru.chuikov.study;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.chuikov.study.entity.Role;
import ru.chuikov.study.entity.User;
import ru.chuikov.study.entity.UserStatus;
import ru.chuikov.study.repository.UserRepository;
import ru.chuikov.study.service.UserService;

import java.util.Date;

@Component
@Log
public class InitData {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations gridFsOperations;

    @Value("${admin.username}:admin")
    private String adminUsername;
    @Value("${admin.password}:admin")
    private String adminPassword;
    @Value("${admin.email}:admin@test.ru")
    private String adminEmail;

    @Value("${testOpenUser.username}:testOpen")
    private String testOpenUsername;
    @Value("${testOpenUser.password}:testOpen")
    private String testOpenPassword;
    @Value("${testOpenUser.email}:testOpen@test.ru")
    private String testOpenEmail;

    @Value("${testClosedUser.username}:testClosed")
    private String testClosedUsername;
    @Value("${testClosedUser.password}:testClosed")
    private String testClosedPassword;
    @Value("${testClosedUser.email}:testClosed@test.ru")
    private String testClosedEmail;

    public String getAdminUsername() {
        return adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public String getTestOpenUsername() {
        return testOpenUsername;
    }

    public String getTestOpenPassword() {
        return testOpenPassword;
    }

    public String getTestOpenEmail() {
        return testOpenEmail;
    }

    public String getTestClosedUsername() {
        return testClosedUsername;
    }

    public String getTestClosedPassword() {
        return testClosedPassword;
    }

    public String getTestClosedEmail() {
        return testClosedEmail;
    }

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        //clear all Users
        userRepository.deleteAll();

        //add admin user
        userRepository.saveAndFlush(User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .email(adminEmail)
                .role(Role.ADMIN)
                .userStatus(UserStatus.CLOSED)
                .created(new Date())
                .id(0)
                .build());
        log.info("admin added");

        //add open user
        userRepository.saveAndFlush(User.builder()
                .username(testOpenUsername)
                .password(passwordEncoder.encode(testOpenPassword))
                .email(testOpenEmail)
                .role(Role.USER)
                .userStatus(UserStatus.OPEN)
                .created(new Date())
                .id(1)
                .build());
        log.info("testOpen added");
        //add open user
        userRepository.saveAndFlush(User.builder()
                .username(testClosedUsername)
                .password(passwordEncoder.encode(testClosedPassword))
                .email(testClosedEmail)
                .role(Role.USER)
                .userStatus(UserStatus.CLOSED)
                .created(new Date())
                .id(2)
                .build());
        log.info("testClosed added");
    }
}
