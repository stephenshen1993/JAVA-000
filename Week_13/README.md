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

#### 测试类

```java
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
```

先启动JmsActivemqApplication，再启动测试类，访问ActiveMQ管控台：http://127.0.0.1:8161/admin/

账号:admin	密码: admin

投递的`inbound.topic`消息被正常消费，并将处理结果投递到`outbound.topic`供下游处理。



#### 参考文章

- [Spring Boot Jms ActiveMQ Example](https://www.devglan.com/spring-boot/spring-boot-jms-activemq-example)



## homework 2

**（必做）**搭建一个 3 节点 Kafka 集群，测试功能和性能；实现 spring kafka 下对 kafka 集群的操作，将代码提交到 github。



### 搭建Kafka集群

#### 软件下载

```bash
docker pull wurstmeister/zookeeper
docker pull wurstmeister/kafka
```

#### zk单点部署

```bash
docker run -d --name zookeeper -p 2181:2181 -t wurstmeister/zookeeper
```

#### kafka集群部署

```bash
# 此处zk地址不能使用127.0.0.1或者localhost 如果IP变了之后需要重新生成容器
# 端口 2181 即zk地址
# 节点1
docker run -d --name kafka1 -p 9092:9092 -e KAFKA_BROKER_ID=0 -e KAFKA_ZOOKEEPER_CONNECT=192.168.3.39:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://192.168.3.39:9092 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 -t wurstmeister/kafka
# 节点2
docker run  -d --name kafka2 -p 9093:9093 -e KAFKA_BROKER_ID=1 -e KAFKA_ZOOKEEPER_CONNECT=192.168.3.39:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://192.168.3.39:9093 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9093 -t wurstmeister/kafka
# 节点3
docker run  -d --name kafka3 -p 9094:9094 -e KAFKA_BROKER_ID=2 -e KAFKA_ZOOKEEPER_CONNECT=192.168.3.39:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://192.168.3.39:9094 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9094 -t wurstmeister/kafka
```

检查节点部署情况

```bash
docker ps
```

kafka-manager安装

```bash
# 根据自己需要 确认是否增加restart参数 由于本人公司和家里IP不同，所以没加此参数
# --restart=always 在容器退出时总是重启容器
docker run -itd --name=kafka-manager -p 9000:9000 -e ZK_HOSTS="192.168.3.39:2181" sheepkiller/kafka-manager
```

#### topic创建

- 访问 localhost:9000

> 错误记录：
>
> 回家后访问kafka-manager页面，发现集群未正确加载，原因是无线网络变化导致IP变更。
>
> 解决方案：
>
> 目前还未掌握docker动态修改参数的方法，故直接删除容器重新以正确IP重新启动。

- 创建集群"kafka-test"

- 创建主题”test-topic“，3分区/2副本

  ![image-20210113221326718](README.assets/image-20210113221326718.png)



#### 参考文章

- [Docker搭建zookeeper + kafka集群(Mac)](https://blog.csdn.net/weigang200820chengdu/article/details/101214895)
- [mac 使用 docker搭建 kafka集群 + Zookeeper + kafka-manager](https://blog.csdn.net/cpongo2ppp1/article/details/89383168)
- [[用 Docker 快速搭建 Kafka 集群](https://www.cnblogs.com/mihich/p/13589827.html)](https://www.cnblogs.com/mihich/p/13589827.html)



### 测试功能和性能

#### 功能测试

发送消息

```
# 进入kafka容器
docker exec -it kafka1 bash
cd /opt/kafka_2.13-2.7.0/bin
# 生产消息
./kafka-console-producer.sh --bootstrap-server localhost:9092 --topic test-topic
```

消费消息

```
# 进入kafka容器
docker exec -it kafka2 bash
cd /opt/kafka_2.13-2.7.0/bin
# 消费消息
./kafka-console-consumer.sh --bootstrap-server localhost:9093 --topic test-topic --from-beginning
```



#### 性能测试

```
bash-4.4# bin/kafka-producer-perf-test.sh --topic test32 --num-records 100000 --record-size 1000 --throughput 2000 --producer-props bootstrap.servers=localhost:9092
1153 records sent, 229.2 records/sec (0.22 MB/sec), 1139.8 ms avg latency, 2785.0 ms max latency.
2768 records sent, 542.3 records/sec (0.52 MB/sec), 3479.6 ms avg latency, 4883.0 ms max latency.
2512 records sent, 476.0 records/sec (0.45 MB/sec), 6444.9 ms avg latency, 8673.0 ms max latency.
3632 records sent, 708.5 records/sec (0.68 MB/sec), 10173.5 ms avg latency, 12244.0 ms max latency.
2992 records sent, 593.7 records/sec (0.57 MB/sec), 13524.5 ms avg latency, 15696.0 ms max latency.
3504 records sent, 697.3 records/sec (0.67 MB/sec), 17302.4 ms avg latency, 19248.0 ms max latency.
3616 records sent, 718.7 records/sec (0.69 MB/sec), 20297.2 ms avg latency, 22253.0 ms max latency.
3888 records sent, 769.1 records/sec (0.73 MB/sec), 22179.2 ms avg latency, 23438.0 ms max latency.
3648 records sent, 723.2 records/sec (0.69 MB/sec), 24576.6 ms avg latency, 26438.0 ms max latency.
3872 records sent, 774.4 records/sec (0.74 MB/sec), 27179.6 ms avg latency, 29438.0 ms max latency.
3632 records sent, 717.2 records/sec (0.68 MB/sec), 29450.3 ms avg latency, 31185.0 ms max latency.
4000 records sent, 798.1 records/sec (0.76 MB/sec), 32249.9 ms avg latency, 34249.0 ms max latency.
3088 records sent, 615.9 records/sec (0.59 MB/sec), 35751.0 ms avg latency, 38248.0 ms max latency.
3792 records sent, 758.4 records/sec (0.72 MB/sec), 39606.3 ms avg latency, 41824.0 ms max latency.
4064 records sent, 808.0 records/sec (0.77 MB/sec), 42651.8 ms avg latency, 44766.0 ms max latency.
4041 records sent, 805.8 records/sec (0.77 MB/sec), 43763.1 ms avg latency, 45139.0 ms max latency.
4215 records sent, 841.5 records/sec (0.80 MB/sec), 43369.9 ms avg latency, 45052.0 ms max latency.
4592 records sent, 912.7 records/sec (0.87 MB/sec), 42505.4 ms avg latency, 44369.0 ms max latency.
4329 records sent, 865.8 records/sec (0.83 MB/sec), 41737.3 ms avg latency, 44435.0 ms max latency.
4471 records sent, 884.1 records/sec (0.84 MB/sec), 40736.2 ms avg latency, 43675.0 ms max latency.
3920 records sent, 780.3 records/sec (0.74 MB/sec), 39759.2 ms avg latency, 44141.0 ms max latency.
5024 records sent, 1004.6 records/sec (0.96 MB/sec), 38370.6 ms avg latency, 43881.0 ms max latency.
4240 records sent, 839.9 records/sec (0.80 MB/sec), 37909.9 ms avg latency, 42654.0 ms max latency.
4208 records sent, 838.4 records/sec (0.80 MB/sec), 37645.6 ms avg latency, 42228.0 ms max latency.
3872 records sent, 769.5 records/sec (0.73 MB/sec), 37880.9 ms avg latency, 42504.0 ms max latency.
3776 records sent, 750.0 records/sec (0.72 MB/sec), 38888.2 ms avg latency, 42810.0 ms max latency.
100000 records sent, 735.342780 records/sec (0.70 MB/sec), 31962.48 ms avg latency, 45139.00 ms max latency, 36852 ms 50th, 44092 ms 95th, 44880 ms 99th, 45062 ms 99.9th.
```



### spring kafka操作集群

(待完成...)