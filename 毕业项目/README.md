# 毕业总结

转眼4个月过去了，开课之前只是想打通自己的知识脉络，为2021年的春招做准备，没想到的是收获远超预期。

15周里，跟着秦老师从虚拟机到并发、从框架到中间件、从单体架构到rpc微服务整个都趟了一遍，这将为自己后面的技术成长指明道路。加课期间秦老师的学习/工作方法、崔崔的小葵花课堂都受益匪浅，赋予了这门课额外的魅力。在课程知识之外，更重要的是结识了优秀且努力的老师和同学们，从你们身上我感受到了上学时那种求知若渴，这或是才是你们不断进步变得强大的终极秘诀。

现在回顾可以明显发现自己在课程知识理解上还停留在很粗浅的阶段，只学习这一遍是不够的，训练营下来还需要大量的时间去复习和消化，争取把秦老师传授的知识点嚼烂吃透，提升自己的技术水平。今年上半年的目标就是把课程期间积累的内容复习消化，然后多加练习，进行产出，形成自己的认知和影响力。

最后谢谢秦老师、美崔和助教们的辛勤付出。



## 知识总结

分别用 100 个字以上的一段话，加上一幅图 (架构图或脑图)，总结自己对下列技术的关键点思考和经验认识。

### 1、JVM

JVM（Java 虚拟机）是java能够一次编译，跨平台运行的核心。其原理是先将源代码编译成字节码，再由 JVM 中的类加载器加载字节码并解释执行。

类的生命周期分为加载、校验、准备、解析、初始化、使用、卸载七个阶段，其中校验、准备和解析我们可以视作链接过程。虚拟机启动时，初始化用户主类，再根据new 关键字、静态方法/字段调用、反射调用等条件触发后续类的加载和初始化过程。JVM 默认提供了启动、扩展、应用三种类加载器，这些类加载器具有双亲委托、负责依赖以及缓存加载的特性。

JVM 运行时数据区分为方法区、堆、虚拟机栈、本地方法栈以及程序计数器五个部分。每启动一个线程，JVM 就会在栈空间栈分配对应的线程栈，其内由一个个栈帧组成。堆内存是所有线程共用的内存空间，JVM 将 Heap 内存分为年轻代和老年代部分。年轻代还划分新生代和存活区，在大部分 GC 算法中有 2 个存活区。

内存资源的有限催生了GC（垃圾回收机制）。在新生代、老年代内存不足或手动调用 System.gc 时将会触发 GC，通过可达性算法判断对象是否存活，然后根据清除、复制、整理、分代收集等算法进行垃圾回收。常见的 GC 组合为 Serial+Serial Old、ParNew+CMS、Parallel Scavenge + Parallel Scavenge Old，分别适用于单线程低延迟、多线程低延迟以及多线程高吞吐场景。

常见 JVM 问题主要来自高分配速率和过早提升，都可能导致巨大的 GC 开销，一般通过增加年轻代大小和减少每批次处理的数量来解决。



### 2、NIO

常见有五种 IO 模型，分别是：阻塞IO、非阻塞IO、多路复用IO、信号驱动IO以及异步IO。NIO 指的就是非阻塞 IO，聊 NIO 时，注意区分阻塞与非阻塞、同步与异步。

阻塞和非阻塞关注的是**程序在等待调用结果（消息，返回值）时的状态**。阻塞调用是指调用结果返回之前，当前线程会被挂起直到得到结果之后才会返回；非阻塞调用指在不能立刻得到结果之前，该调用不会阻塞当前线程。

同步和异步关注的是**消息通信机制**。所谓同步，就是在发出一个功能调用时，在没有得到结果之前，该调用就不返回。异步的概念和同步相对。当一个异步过程调用发出后，调用者不能立刻得到结果。实际处理这个调用的部件在完成后，通过状态、通知和回调来通知调用者。

NIO是一种同步非阻塞模型，主要有三大核心部分：Channel(通道)，Buffer(缓冲区), Selector（多路复用器）。传统IO基于字节流和字符流进行操作，而NIO基于Channel和Buffer(缓冲区)进行操作，数据总是从通道读取到缓冲区中，或者从缓冲区写入到通道中。Selector(多路复用器)用于监听多个通道的事件（比如：连接打开，数据到达）。因此，单个线程可以监听多个数据通道。



### 3、并发编程

伴随摩尔定律失效，多核 + 分布式时代的来临催生了并发编程的蓬勃发展。

在Java当中多线程技术主要体现几个方面：

1. 在线程之间的协作，互相之间的等待与唤醒
2. 各种锁：synchronized和显式Lock，
3. 线程间协作信号量：CountDownLatch/CyclicBarrier/Semaphore
4. 原子操作类（基于compare and swap 即CAS）
5. 线程管理：Executor，Future，Runnable/Callable
6. 并发集合类：CopyOnWriteArrayList/CopyOnWriteMap

其总结下来就是尽量将写和读之间的冲突进行管理，使得数据在同一个时刻是正确的。



### 4、Spring 和 ORM 等框架

Spring 框架基本上是企业开发的一套基本标准，可以很好的分工协作，分层开发，方便维护。Spring 的核心是 IOC（控制反转） 和AOP（切面编程），控制反转是将 Bean 的管理交给Spring容器，通过依赖注入和依赖查找来满足我们对 Bean 的管理；切面编程是对面向对象编程的一种补充加强。Bean 的生命周期大体分为创建对象、属性赋值、初始化和注销接口注册四个阶段，spring 管理对象生命周期以后，也就改变了编程和协作模式。SpringBoot 的出现简化的复杂的 Spring 配置，实现了零配置可以运行，也就是约定大于配置，在现在的开发中，基本上使用注解方式代替 xml 配置方式，代码可读性增强。SpringBoot 的自动注入原理，如何自定义 starter 还需要好好复习。

ORM 是一种关系映射模式，是为了解决面向对象与关系数据库存在的互不匹配的现象的技术。常见的ORM框架有 hibernate 和 mybatis，大大简化了jdbc的操作，以对象和数据库表的映射方式来操作数据库。



### 5、MySQL 数据库和 SQL

MySQL 是关系型数据库，由于其开源且性能较好，逐步取代了 Oracle，成为了互联网开发中最常用的数据库。在 MySQL 中，除了对 mysql 架构、存储和索引原理的理解外，最重要的知识点还是数据库性能优化。

先了解事务可靠性模型 ACID，包括原子性、一致性、隔离性以及持久性。数据库锁大致分为表级锁和行级锁两类，需要注意避免死锁的发生导致数据库阻塞相互等待。事务隔离是数据库的基本特征，需要了解事务四种隔离级别，分别是读未提交、读已提交、可重复读、串行化，以及隔离级别想要解决的脏读、幻读、不可重复读等问题。数据库执行过程中为了保证事务的原子性和持久性，分别引入了 undo log 和 redo log，用于事务回滚和断电恢复。

SQL 优化方面，需要注意数据类型、存储引擎和隐式转换。SQL 尽量走主键，不行也要走索引，要掌握慢 SQL 查询和分析索引问题。



### 6、分库分表

传统的将数据集中存储至单一数据节点的解决方案，在容量、性能、可用性和运维成本这三方面已经难于满足互联网的海量数据场景。主从结构解决了高可用，读扩展，但是单机容量不变，单机写性能无法解决。提升容量和写性能，除了硬件提升以外，还可以对数据库进行分库分表，将多个数据库作为数据分片的集群提供服务，有效降低单个节点的读写压力，提升数据库整体性能。

数据扩展有三个方向，分别是数据复制、垂直分库分表以及水平分库分表。一般情况下，如果数据本身的读写压力较大，磁盘 IO 已经成为瓶颈，那么分库比分表要好。分库将数据分散到不同的数据库实例，使用不同的磁盘，从而可以并行提升整个集群的并行数据处理能力。相反的情况下，可以尽量多考虑分表，降低单表的数据量，从而减少单表操作的时间，同时也能在单个数据库上使用并行操作多个表来增加处理能力。

分库分表在带来容量和性能提升的同时，系统复杂度也大大增加，在集群管理、数据迁移及数据一致性方面面临不小的挑战。一致性方面已有 XA、TCC、SAGA 以及 AT 四种分布式事务解决方案，目前笔者所在的银行业基于性能考虑，大量使用 SAGA 事务模式。



### 7、RPC 和微服务

RPC 的核心是代理机制，简化原理为客户端和服务端通过代理方式访问，中间数据传输通信需要序列化数据。分布式业务场景里，除了能调用远程方法，我们还需要考虑服务注册和发现、负载均衡、服务路由等集群功能， 熔断，限流等治理能力，重试等策略，高可用、监控、性能等等。

RPC 即远程过程调用，目标是像调用本地方法一样调用远程方法。RPC的简化核心原理 1.本地代理存根 2.本地序列化反序化 3.网络通信 4.远程序列化 5.远程服务存根 6.调用实际服务 7.返回服务结果 8.返回结果给本地调用方。

rpc与分布式服务化的区别：rpc是技术概念；分布式服务化是业务语义，讲的是业务与系统的集成。

分布式服务化与 SOA/ESB 的区分，在于ESB作为一个中间层与前后服务交互，分布式服务化把配置与注册发现独立出来， 前后服务去注册发现处查找服务在直接去访问对应系统。

微服务发展过程：单体架构 --> 垂直架构 --> SOA架构 --> 微服务架构。



### 8)、分布式缓存

缓存的本质是系统各级处理速度不匹配，导致利用空间换时间。利用缓存技术，我们可以简单有效地提升系统性能。

按缓存所处位置分为本地缓存和远程缓存，常见的本地缓存有 java 集合、Hibernate/Mybatis 框架的缓存、Guava Cache、Spring 注解 Cache 等，常见的远程缓存有 Redis 和 Memcached。缓存容量是有限的，所以有了相应的缓存过期策略，常见的有 FIFO 或 LRU、固定过期时间、业务时间加权等。在使用过程中，缓存也会存在缓存穿透、缓存击穿和缓存雪崩三种问题，注意在缓存 key、过期策略、更新策略、数据分布以及熔断限流等方面做足工作以保护缓存。

提到缓存就不得不说当今最主流的 Redis，Redis 有 5 种基本数据类型，分别是 string、hash、list、set、sorted set。Redis 6 以前是单线程，Redis 6 之后是多线程 NIO 模型，这是主要的性能提升点。经过前辈们的使用总结，目前 Redis 有六大使用场景，分别是业务数据缓存、业务数据处理、全局一致计数、高效统计计数、发布订阅与 Stream、分布式锁。



### 9)、分布式消息队列

消息开始是队列然后到消息服务。消息处理保障一般建议使用At least once然后在应用层去做幂等处理，这样做能确保数据不会丢失。同一个 Topic 或者 Queue 保证有序，但是做了分区就无法保证整体有序，只能保证分区内有序。JMS是应用层的API协议是一系列的接口，具体是使用它的实现。第一代 MQ（ActiveMQ、RabbitMQ）提供 Queue 和 Topic，第二代 MQ（kafka、rocketMQ）相比于第一代提供了堆积的功能， 第三代 MQ（pulsar）分离了计算节点和存储节点。

kafka 得注意集群中增加机器或者减少机器时候的rebalance问题。