package ru.chuikov.study;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class MainService {
    public static void main(String[] args){
        SpringApplication.run(MainService.class,args);
    }

}
