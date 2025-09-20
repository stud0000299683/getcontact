package ru.utmn.chamortsev.getcontact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetcontactController {
    @GetMapping("/hello")
    public String hello(){
        return "Hello Word!";
    }


}
