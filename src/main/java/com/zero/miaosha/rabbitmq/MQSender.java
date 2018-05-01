package com.zero.miaosha.rabbitmq;

import com.zero.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    	public void send(Object message) {
		String msg = RedisService.beanToString(message);
		log.info("send message:"+msg);
		amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
	}


    public void sendMiaoshaMessage(MiaoshaMessage mm) {
    	    String msg = RedisService.beanToString(mm);
    	    log.info("send message :"+msg);
    	    amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE,msg);
    }
}
