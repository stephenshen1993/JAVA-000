# week 13 消息中间件

## homework 1

**（必做）**搭建 ActiveMQ 服务，基于 JMS，写代码分别实现对于 queue 和 topic 的消息生产和消费，代码提交到 github。

#### 启动ActiveMQ

```bash
$ ./activemq console
```

#### 引入核心依赖

pom.xml

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-activemq</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.activemq</groupId>
    <artifactId>activemq-broker</artifactId>
</dependency>
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
</dependency>
```

#### 添加ActiveMQ配置

application.properties

```properties
spring.activemq.broker-url=tcp://localhost:61616
spring.activemq.user=admin
spring.activemq.password=admin
```

#### JMS配置Bean

注意 template.setPubSubDomain(true) 和 factory.setPubSubDomain(true)，配置发布订阅模式，默认是点对点的queue。

```java
/**
 * @ClassName : JmsConfig  //类名
 * @Description : jms配置类  //描述
 * @Author : StephenShen  //作者
 * @Date: 2021-01-12 07:24  //时间
 */
@Configuration
public class JmsConfig {
    String BROKER_URL = "tcp://localhost:61616";
    String BROKER_USERNAME = "admin";
    String BROKER_PASSWORD = "admin";

    @Bean
    public ActiveMQConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(BROKER_URL);
        connectionFactory.setPassword(BROKER_USERNAME);
        connectionFactory.setUserName(BROKER_PASSWORD);
        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        // 开启发布/订阅，默认是点对点
        template.setPubSubDomain(true);
        return template;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrency("1-1");
        // 开启发布/订阅，默认是点对点
        factory.setPubSubDomain(true);
        return factory;
    }
}
```

#### 消息监听（消费者）

```java
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
```

#### 消息投递（生产者）

```java
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
        final String textMessage = "Hello" + map.get("name");
        System.out.println("Sending message " + textMessage + "to queue - " + destinationName);

        jmsTemplate.send(destinationName, new MessageCreator() {

            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(message);
                return textMessage;
            }
        });
    }
}
```

#### 启动类

@EnableJms 启用JMS

```java
@SpringBootApplication
@EnableJms
public class JmsActivemqApplication {
    public static void main(String[] args) {
        SpringApplication.run(JmsActivemqApplication.class, args);
    }
}
```

启动后，访问ActiveMQ管控台：http://127.0.0.1:8161/admin/

账号:admin	密码: admin

投递的`inbound.topic`消息被正常消费，并将处理结果投递到`outbound.topic`供下游处理。



## homework 2

**（必做）**搭建一个 3 节点 Kafka 集群，测试功能和性能；实现 spring kafka 下对 kafka 集群的操作，将代码提交到 github。

