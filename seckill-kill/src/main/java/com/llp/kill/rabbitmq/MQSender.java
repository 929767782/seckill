package com.llp.kill.rabbitmq;


import com.llp.common.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  发送消息
 */
@Service
public class MQSender {

    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

//    public void send(Object message){
//        String msg = RedisService.beanToString(message);
//        log.info("send message:"+msg);
//        amqpTemplate.convertAndSend(MQConfig.QUEUE, message);
//    }

    public void sendStockReleaseMessage(StockReleaseMessage message) {
		String msg = RedisService.beanToString(message);
		amqpTemplate.convertAndSend(MQConfig.ORDER_EVENT_EXCHANGE, "order.release.other", msg);
	}

	// convertAndSend函数还支持只指定消息发送的队列名称，可以不指定交换机名称；
    // 只要声明要发送的队列，也不需要和交换机绑定。
    // 不过并非不使用交换机，实际上是使用RabbitMQ自带的默认交换机和指定队列进行路由。
	public void sendSeckillMessage(SeckillMessage message){
        String msg = RedisService.beanToString(message);
        log.info("send message:"+msg);
        amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE, msg);

    }
}
