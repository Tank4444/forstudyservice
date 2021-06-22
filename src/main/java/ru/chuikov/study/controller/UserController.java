package ru.chuikov.study.controller;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.chuikov.study.entity.*;
import ru.chuikov.study.service.UserService;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(value = "/user",headers = {"X-API-VERSION=1"})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailValidator emailValidator;

    @PostMapping(value = "/add")
    public ResponseEntity addUser(@RequestBody UserAddModel model) {
        try {
            if (!emailValidator.isValid(model.getEmail()))
                throw new Exception("email is not a valid");
            User user = User.builder()
                    .username(model.getUsername())
                    .email(model.getEmail())
                    .password(model.getPassword())
                    .role(Role.USER)
                    .userStatus(UserStatus.OPEN)
                    .created(new Date())
                    .build();
            userService.addUser(user);
        } catch (Exception e) {
            return new ResponseEntity<Map>(
                    Collections.singletonMap("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map>(Collections.singletonMap("message", "ok"), HttpStatus.CREATED);
    }

    @PostMapping(value = "/get/{id}")
    public ResponseEntity getUserById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        try {
            if (user.getId() == id)
                return new ResponseEntity<UserResponce>(
                        UserResponce.fromUser(userService.getUserById(id)),
                        HttpStatus.OK);
            if (user.getUserStatus().equals(UserStatus.CLOSED)
                    || user.getUserStatus().equals(UserStatus.BLOCKED))
                return new ResponseEntity<Map<String, String>>(
                        Collections.singletonMap("message", "User closed or blocked"),
                        HttpStatus.BAD_REQUEST);
            return new ResponseEntity<UserResponce>(
                    UserResponce.fromUser(userService.getUserById(id)),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(Collections.singletonMap("message", e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/changeUserStatus/Closed")
    public ResponseEntity changeUserStatusToClose(@AuthenticationPrincipal User user) {
        try {
            userService.changeUserStatusTo(user, UserStatus.CLOSED);
        } catch (Exception e) {
            return new ResponseEntity<Map>(Collections.singletonMap("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map>(Collections.singletonMap("message", "ok"), HttpStatus.OK);
    }

    @PostMapping(value = "/changeUserStatus/Open")
    public ResponseEntity changeUserStatusToOpen(@AuthenticationPrincipal User user) {
        try {
            userService.changeUserStatusTo(user, UserStatus.OPEN);
        } catch (Exception e) {
            return new ResponseEntity<Map>(Collections.singletonMap("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map>(Collections.singletonMap("message", "ok"), HttpStatus.OK);
    }

    @PostMapping(value = "/changeUserEmail")
    public ResponseEntity changeUserEmail(@RequestBody EmailModel email, @AuthenticationPrincipal User user) {
        try {
            if (!emailValidator.isValid(email.getEmail()))
                throw new Exception("email is not a valid");
            user.setEmail(email.getEmail());
            userService.changeUserEmail(user);
        } catch (Exception e) {
            return new ResponseEntity<Map>(Collections.singletonMap("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map>(Collections.singletonMap("message", "ok"), HttpStatus.CREATED);
    }

    @PostMapping(value = "/deleteUser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteUser(@PathVariable("id") String id,@AuthenticationPrincipal User user){
        try {
            userService.deleteUserById(Long.parseLong(id));
            return new ResponseEntity<Map>(Collections.singletonMap("message","ok"),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Map>(Collections.singletonMap("message",e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/changeUserRole/{id}/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity changeUserRoleToAdmin(@PathVariable("id") String id,@AuthenticationPrincipal User user){
        try {
            userService.changeUserRoleTo(Long.parseLong(id), Role.ADMIN);
        } catch (Exception e) {
            return new ResponseEntity<Map>(Collections.singletonMap("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map>(Collections.singletonMap("message", "ok"), HttpStatus.OK);

    }

    @PostMapping(value = "/changeUserRole/{id}/user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity changeUserRoleToUser(@PathVariable("id") String id,@AuthenticationPrincipal User user){
        try {
            userService.deleteUserById(Long.parseLong(id));
            return new ResponseEntity<Map>(Collections.singletonMap("message","ok"),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Map>(Collections.singletonMap("message",e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }

}
