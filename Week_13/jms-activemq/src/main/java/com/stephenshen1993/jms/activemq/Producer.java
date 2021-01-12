package com.stephenshen1993.jms.activemq;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Map;

/**
 * @ClassName : Producer  //类名
 * @Description : jms生产者  //描述
 * @Author : StephenShen  //作者
 * @Date: 2021-01-12 07:37  //时间
 */
@Component
public class Producer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(final String destinationName, final String message) {
        Map map = new Gson().fromJson(message, Map.class);
        jmsTemplate.send(destinationName, new MessageCreator() {

            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(message);
                System.out.println("Sending message " + textMessage.getText() + "to destination - " + destinationName);
                return textMessage;
            }
        });
    }
}
