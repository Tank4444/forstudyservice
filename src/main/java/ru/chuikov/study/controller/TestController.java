package ru.chuikov.study.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @GetMapping
    @RequestMapping(value = "/open")
    public Map test(){
        return Collections.singletonMap("Status","OK");
    }

    @GetMapping
    @RequestMapping(value = "/closed")
    public Map testAuth(){
        return Collections.singletonMap("Status","OK");
    }
}
