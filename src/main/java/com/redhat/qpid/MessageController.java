package com.redhat.qpid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessageController {

    @Autowired
    private Producer producer;

    @GetMapping("/send")
    public String send() {

        producer.sendMessage("Some useless message content.");

        return "home";
    }

}
