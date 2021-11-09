package com.redhat.amq.one;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AmqController
{

   @Autowired
   private Producer producer;

   @GetMapping("/send")
   public String send()
   {

      producer.sendMessage("Some stupid text message");

      return "home";
   }
}
