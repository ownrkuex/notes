# Kafka简明教程

## Kafka作用

* 服务解耦
* 缓冲，提高吞吐量和容错性

## kafka使用场景

* 仪表盘（监控）程序
* 日志集散中心（JobManagement）

## 核心概念

* 消息：kafka的基本数据单元，类似数据库里的一个“数据行”或一条“记录”。消息由topic, [partition, key,] value组成。
* 批次：主题和分区相同的一组消息。为了提高效率，消息以批次为单位写入kafka。
* 消息模式：json, xml, avro等。
* 主题：消息通过主题进行分类，类似数据库里的“表”。
* 分区：一个主题可以分为若干分区，kafka通过分区实现伸缩性，分区可以分布在不同的机器上，也就是说，一个主题可以横跨多个服务器。

![](kafka_topic_partition.PNG)

* 生产者：创建和发布消息的一方。
* 消费者：订阅和读取消息的一方。
* 偏移量：消息元数据之一，自增整数。在给定分区中，消息的偏移量是唯一的。
* 消费者群组：订阅同一个主题的若干个消费者可以组成消费者群组。群组能保证**一个分区只能被群组内一个消费者使用**（消费者对分区的所有权关系）。

![](kafka_consumer_group.PNG)

## 生产者

### 生产者发送消息过程

![](kafka_producer.PNG)

### 生产者重要配置项

```
Properties kafkaProducerProps = new Properties();
// 必须要配置的项
kafkaProducerProps.put("bootstrap.servers", "broker1:9092,broker2:9092");
kafkaProducerProps.put("key.serializer", "com.xx.xx.xx.StringSerializer");
kafkaProducerProps.put("value.serializer", "com.xx.xx.xx.StringSerializer");
// 可选项
// buffer.memory 生产者内部缓冲区大小（字节）
// batch.size 批次大小（字节）
// linger.ms 生产者在批次已满或linger.ms到达上限时把批次发送出去
// client.id 生产者的id，可以是任意内容的字符串，用来标识生产者
// retries 重试次数，设为0则不重试
// max.in.flight.requests.per.connection 生产者在收到成功响应之前可以发送多少个批次
// 设为1可以保证消息是按发送的顺序写入服务器的，即使发生了重试

KafkaProducer producer = new KafkaProducer<String, String>(kafkaProducerProps);
// KafkaProducer是线程安全的，一般情况下一个实例即可，除非需要更高的吞吐量
```

### 发送消息的三种方式

* 发送并忘记

```
ProducerRecord<String, String> record = new ProducerRecord<>("topic", "key", "value");
producer.send(record);
```

* 同步发送

```
ProducerRecord<String, String> record = new ProducerRecord<>("topic", "key", "value");
Future<RecordMetadata> future = producer.send(record);
RecordMetadata metadata = future.get();
```

* 异步发送

```
private static final class MyProducerCallback implements Callback {
    @Override
    public void onCompletion(RecordMetadata metadata, Exception e) {
        // 发送过程中出现异常则 e != null
    }
}
ProducerRecord<String, String> record = new ProducerRecord<>("topic", "key", "value");
producer.send(record, new MyProducerCallback());
```

### 自定义序列化器

1. 实现`Serializer<T>`接口
1. value.serializer指定为`Serializer<T>`的实现类
1. KafkaProducer和ProducerRecord使用T类型的参数

### 默认分区策略

key为null，用轮询的方式把消息均匀分布到各个分区上；key不为null，根据key的散列值把消息映射到特定分区上。在不改变分区数量的情况下，同一个key总是被映射到同一个分区上。

### 自定义分区器

1. 实现`Partitioner`接口
1. KafkaProducer的`partitioner.class`属性设置为`Partitioner`的实现类

## 消费者

### 分区再均衡

分区的所有权从一个消费者移动到另一个消费者的过程称为再均衡。再均衡期间，当前群组会整体不可用，且上一个消费者的读取状态会丢失。应尽量避免不必要的再均衡。

### 触发再均衡的条件

* 每个消费者都有一个后台的心跳线程，每隔一段时间(`heartbeat.interval.ms`)向群组协调器broker发送心跳。若群组协调器超过一定时间(`session.timeout.ms`)没有收到心跳，就认为该消费者已死亡，将其移除群组并触发再均衡。
