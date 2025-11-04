package com.utmn.chamortsev.hw12;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// REST контроллер
@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/events/{type}")
    public String publishEvent(@PathVariable String type, @RequestBody Object data) {
        eventService.publish(type, data);
        return "Event published";
    }
}
