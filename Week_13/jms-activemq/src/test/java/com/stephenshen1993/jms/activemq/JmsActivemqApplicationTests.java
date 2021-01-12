package com.stephenshen1993.jms.activemq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JmsActivemqApplicationTests {

    @Autowired
    private Producer producer;

    @Test
    public void testProducer(){
        producer.sendMessage("inbound.topic", "{\"name\":\"John\"} ");
    }

}
