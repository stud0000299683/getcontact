package com.utmn.chamortsev.hw1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetcontactController {
    @GetMapping("/hello")
    public String hello(){
        return "Hello Word!";
    }


}
