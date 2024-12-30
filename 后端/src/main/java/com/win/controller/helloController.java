package com.win.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
class helloController {
    @GetMapping("/hello")
    public String hello(){
        return "hello Spring Boot";
    }
}
