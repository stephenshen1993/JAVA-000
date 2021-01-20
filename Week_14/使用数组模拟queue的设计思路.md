## 目标

在版本1 MQ的基础上，去掉内存Queue，设计自定义Queue，实现消息确认和消费offset

1. 自定义内存Message数组模拟Queue。
2. 使用指针记录当前消息写入位置。
3. 对于每个命名消费者，用指针记录消费位置



## 实现思路

### 1.寻找可参照目标

在第一个版本实现的内存Queue中，使用的是LinkedBlockingQueue，其内部使用单链表 + 双指针实现。在我们第二个版本MQ的需求中，涉及数组和指针，所以我们把参照目标锁定在ArrayBlockingQueue。



### 2.可参照目标分析

#### 2.1 目标定位

ArrayBlockingQueue是数组实现的线程安全的有界的阻塞队列。

#### 2.2 原理和数据结构

ArrayBlockingQueue的数据结构如下：

```java
// 保存数据的数组
Object[] items;
// 可重入锁
ReentrantLock lock;
// 非空条件
Condition notEmpty;
// 非满条件
Condition notFull;
```

实现原理：

- ArrayBlockingQueue继承于AbstractQueue，并且它实现了BlockingQueue接口。

- ArrayBlockingQueue本质上是通过数组实现的，其大小是创建ArrayBlockingQueue时指定的。

- ArrayBlockingQueue根据互斥锁实现“多线程对竞争资源的互斥访问”，其持有一个ReentrantLock对象(lock)，默认使用非公平锁。

- ArrayBlockingQueue通过Condition实现更精确的访问，其持有两个Condition对象

  - notEmpty

    获取数据时数组为空，则该线程执行notEmpty.await()进入等待；

    当其他线程向数组插入数据后，调用notEmpty.signal唤醒notEmpty上的等待线程。

  - notFull

    插入数据时数组已满，则该线程执行notFull.await()进入等待；

    当其他线程从数组取出数据后，调用notFull.signal唤醒notFull上的等待线程。



#### 2.3 源码分析

下面从创建、入队、出队这几个方面来进行分析。

1）创建

以ArrayBlockingQueue(int capacity, boolean fair)为例进行说明：

```java
public ArrayBlockingQueue(int capacity, boolean fair) {
	if (capacity <= 0)
		throw new IllegalArgumentException();
	this.items = new Object[capacity];
	lock = new ReentrantLock(fair);
	notEmpty = lock.newCondition();
	notFull =  lock.newCondition();
}
```

创建时初始化指定容量的数据，根据传入参数构造公平/非公平锁，用锁初始化两个condition

2）入队

以offer(E e)为例，对入队方法进行说明：

```java
public boolean offer(E e, long timeout, TimeUnit unit)
    throws InterruptedException {

    checkNotNull(e);
    long nanos = unit.toNanos(timeout);
  	// 获取“该阻塞队列的独占锁”
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    try {
      	// 如果队列已满，则返回false。
        while (count == items.length) {
            if (nanos <= 0)
                return false;
            nanos = notFull.awaitNanos(nanos);
        }
      	// 否则，入队
        enqueue(e);
        return true;
    } finally {
      	// 释放锁
        lock.unlock();
    }
}
```

offer(E e)的作用是将e插入阻塞队列的尾部，使用ReentrantLock进行资源独占。

enqueue方法：

```java
private void enqueue(E x) {
    // assert lock.getHoldCount() == 1;
    // assert items[putIndex] == null;
    final Object[] items = this.items;
  	// 将x添加到”队列“中
    items[putIndex] = x;
  	// 循环处理
    if (++putIndex == items.length)
        putIndex = 0;
    count++;
  	// 唤醒notEmpty上面的等待线程。
    notEmpty.signal();
}
```

enqueue()在元素入队之后，会唤醒notEmpty上面的等待线程。

3）出队

下面以poll()为例，对出队方法进行说明。

```java
public E poll(long timeout, TimeUnit unit) throws InterruptedException {
    long nanos = unit.toNanos(timeout);
    final ReentrantLock lock = this.lock;
    // 获取“队列的独占锁”
  	lock.lockInterruptibly();
    try {
      	// 若“队列为空”，则在指定时间内等待。
        while (count == 0) {
            if (nanos <= 0)
                return null;
            nanos = notEmpty.awaitNanos(nanos);
        }
      取出元素
        return dequeue();
    } finally {
      	// 释放锁
        lock.unlock();
    }
}
```

take()返回队列的头，若队列为空则一直等待。

```
private E dequeue() {
    // assert lock.getHoldCount() == 1;
    // assert items[takeIndex] != null;
    final Object[] items = this.items;
    @SuppressWarnings("unchecked")
    E x = (E) items[takeIndex];
    items[takeIndex] = null;
    if (++takeIndex == items.length)
        takeIndex = 0;
    count--;
    if (itrs != null)
        itrs.elementDequeued();
    notFull.signal();
    return x;
}
```

dequeue里面有个唤醒动作，唤醒notFull上的等待线程。



### 3.设计思路

经上面的分析我们发现，ArrayBlockingQueue是满足我们需求的，但自定义queue的话不需要全部实现，只需实现部分方法即可。

#### 3.1 沿用ArrayBlockingQueue的数据结构

- items：元素队列
- takeIndex：下一个取出的元素下标
- putIndex：下一个添加的元素下标
- count：队列内元素总数
- lock：独占锁
- notEmpty：非空条件
- notFull：非满条件

#### 3.2 待实现的方法列表

- 构造方法：MqQueue(int capacity, boolean fair)
- 入队方法：offer(E e)，如果队列满了则返回false；
- 出队方法：poll(long timeout, TimeUnit unit)，如果队列为空则阻塞等待一段时间后如果还为空就返回null；

