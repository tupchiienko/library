package com.cursor.library.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello world!";
    }
}
