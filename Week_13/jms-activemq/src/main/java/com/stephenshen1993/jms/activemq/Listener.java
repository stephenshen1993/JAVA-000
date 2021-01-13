package com.stephenshen1993.jms.activemq;

import com.google.gson.Gson;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.Map;

/**
 * @ClassName : Listener  //类名
 * @Description : 消息监听  //描述
 * @Author : StephenShen  //作者
 * @Date: 2021-01-12 07:31  //时间
 */
@Component
public class Listener {


    @JmsListener(destination = "inbound.topic")
    @SendTo("outbound.topic")
    public String receiveMessageFromTopic(final Message jsonMessage) throws JMSException {
        return receiveMessage(jsonMessage);
    }

    public String receiveMessage(final Message jsonMessage) throws JMSException {
        System.out.println("Received message " + jsonMessage);
        String response = null;
        if(jsonMessage instanceof TextMessage) {
            TextMessage textMessage = (TextMessage)jsonMessage;
            Map map = new Gson().fromJson(textMessage.getText(), Map.class);
            response  = "Hello " + map.get("name");
        }
        return response;
    }
}
