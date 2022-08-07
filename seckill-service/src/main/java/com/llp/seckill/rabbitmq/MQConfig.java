package com.llp.seckill.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  MQ配置类
 */
@Configuration
public class MQConfig {

    public static final String SECKILL_QUEUE = "seckill.queue";
    public static final String QUEUE = "queue";
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String TOPIC_EXCHANGE = "topic-exchange";


    /**
     * Direct模式 交换机Exchange
     * 发送者先发送到交换机上，然后交换机作为路由再将信息发到队列，
     * */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true,false,false,null);
    }
//    @Bean
//    public Queue queue() {
//        return new Queue(QUEUE, true);
//    }

    /**
     * Topic模式 交换机Exchange
     * */
    /*
       new一个queue，注意这不是java.utils中的queue
       第一个参数是queue名字
       第二个参数是queue是否持久化，第三个参数是queue是否是排他的（排他指的只有一个消费者可以连上队列，一般是false，也就是不排他）
       第四个参数是auto-delete，如果为true，没有绑定交换机，则会自动删除
       第五个参数是arguments
     */
    @Bean
    public Queue topicQueue1() {
        return new Queue(TOPIC_QUEUE1, true,false,false,null);
    }
    @Bean
    public Queue topicQueue2() {
        return new Queue(TOPIC_QUEUE2, true,false,false,null);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE,true,false);
    }
    //绑定
    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
    }
    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
    }


}
