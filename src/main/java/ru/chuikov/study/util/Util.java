package ru.chuikov.study.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Util {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    public Tika tika(){
        return new Tika();
    }

    @Bean
    public EmailValidator emailValidator(){
        return EmailValidator.getInstance();
    }
}
