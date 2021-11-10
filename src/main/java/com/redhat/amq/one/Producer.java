package com.redhat.amq.one;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private static final Logger LOG = LoggerFactory.getLogger(Producer.class);

    @Value("${queue.name}")
    private String queueName;

    @Autowired
    public JmsTemplate jmsTemplate;

    public void sendMessage(String payload) {
        this.jmsTemplate.convertAndSend(queueName, payload);
    }

}
