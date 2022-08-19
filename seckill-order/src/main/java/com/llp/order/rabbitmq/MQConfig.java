package com.llp.order.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 *  MQ配置类
 */
@Configuration
public class MQConfig {

    public static final String SECKILL_QUEUE = "seckill.queue";
    public static final String ORDER_DELAY_QUEUE = "order.delay.queue";
    public static final String ORDER_RELEASE_QUEUE = "order.release.order.queue";
    public static final String STOCK_RELEASE_QUEUE = "stock.release.stock.queue";
    public static final String ORDER_EVENT_EXCHANGE = "order-event-exchange";
    public static final String STOCK_EVENT_EXCHANGE = "stock-event-exchange";


    /**
     * Direct模式 交换机Exchange
     * 发送者先发送到交换机上，然后交换机作为路由再将信息发到队列，
     * */
    @Bean
    public Queue seckillQueue() {
        return new Queue(SECKILL_QUEUE, true,false,false,null);
    }
//    @Bean
//    public Queue queue() {
//        return new Queue(QUEUE, true);
//    }


    /*
       new一个queue，注意这不是java.utils中的queue
       第一个参数是queue名字
       第二个参数是queue是否持久化，第三个参数是queue是否是排他的（排他指的只有一个消费者可以连上队列，一般是false，也就是不排他）
       第四个参数是auto-delete，如果为true，没有绑定交换机，则会自动删除
       第五个参数是arguments
     */
    /**
     * Topic模式，订单延迟队列
     * @return
     */
    @Bean
    public Queue orderDelayQueue() {
        /**
         Queue(String name,  队列名字
         boolean durable,  是否持久化
         boolean exclusive,  是否排他
         boolean autoDelete, 是否自动删除
         Map<String, Object> arguments) 属性
         */
        HashMap<String, Object> arguments = new HashMap<>();
        //死信交换机
        arguments.put("x-dead-letter-exchange", ORDER_EVENT_EXCHANGE);
        //死信路由键
        arguments.put("x-dead-letter-routing-key", "order.release.order");
        arguments.put("x-message-ttl", 30*60*1000); // 消息过期时间 30分钟
        return new Queue(ORDER_DELAY_QUEUE,true,false,false,arguments);
    }

    /**
     * Topic模式，订单取消队列
     *
     * @return
     */
    @Bean
    public Queue orderReleaseQueue() {

        Queue queue = new Queue(ORDER_RELEASE_QUEUE, true, false, false);

        return queue;
    }



    /**
     * 库存延迟队列
     * @return
     */
    @Bean
    public Queue stockDelayQueue() {
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", STOCK_EVENT_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", "stock.release");
        // 消息过期时间 40分钟
        arguments.put("x-message-ttl", 40*60*1000);
        return new Queue("stock.delay.queue", true, false, false, arguments);
    }

    /**
     * 普通队列，用于解锁库存
     * @return
     */
    @Bean
    public Queue stockReleaseStockQueue() {
        return new Queue(STOCK_RELEASE_QUEUE, true, false, false, null);
    }




    @Bean
    public TopicExchange orderEventExchange(){
        return new TopicExchange(ORDER_EVENT_EXCHANGE,true,false);
    }

    @Bean
    public TopicExchange stockEventExchange(){
        return new TopicExchange(STOCK_EVENT_EXCHANGE,true,false);
    }



    //绑定
    /**
     * 创建订单的binding
     * @return
     */
    @Bean
    public Binding orderCreateBinding() {
        /**
         * String destination, 目的地（队列名或者交换机名字）
         * DestinationType destinationType, 目的地类型（Queue、Exhcange）
         * String exchange,
         * String routingKey,
         * Map<String, Object> arguments
         * */
        return new Binding(ORDER_DELAY_QUEUE, Binding.DestinationType.QUEUE, ORDER_EVENT_EXCHANGE, "order.create.order", null);
    }
    @Bean
    public Binding orderReleaseBinding() {
        return new Binding(ORDER_RELEASE_QUEUE,
                Binding.DestinationType.QUEUE,
                ORDER_EVENT_EXCHANGE,
                "order.release.order",
                null);
    }


    @Bean
    public Binding orderReleaseOtherBinding() {
        return new Binding(STOCK_RELEASE_QUEUE,
                Binding.DestinationType.QUEUE,
                ORDER_EVENT_EXCHANGE,
                "order.release.other.#",
                null);
    }


    /**
     * 交换机和延迟队列绑定
     * @return
     */
    @Bean
    public Binding stockLockedBinding() {
        return new Binding("stock.delay.queue",
                Binding.DestinationType.QUEUE,
                "stock-event-exchange",
                "stock.locked",
                null);
    }

    /**
     * 交换机和普通队列绑定
     * @return
     */
    @Bean
    public Binding stockReleaseBinding() {
        return new Binding("stock.release.stock.queue",
                Binding.DestinationType.QUEUE,
                "stock-event-exchange",
                "stock.release.#",
                null);
    }

}
