package com.zero.miaosha.rabbitmq;

import com.zero.miaosha.domain.MiaoshaOrder;
import com.zero.miaosha.domain.MiaoshaUser;
import com.zero.miaosha.redis.RedisService;
import com.zero.miaosha.service.GoodsService;
import com.zero.miaosha.service.MiaoshaService;
import com.zero.miaosha.service.OrderService;
import com.zero.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {
    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

	@Autowired
	RedisService redisService;

	@Autowired
	GoodsService goodsService;

	@Autowired
	OrderService orderService;

	@Autowired
	MiaoshaService miaoshaService;

    	@RabbitListener(queues=MQConfig.QUEUE)
		public void receive(String message) {
			log.info("receive message:"+message);
		}

	//接口优化4：请求出队，生成订单，减少库存
	@RabbitListener(queues=MQConfig.MIAOSHA_QUEUE)
	public void miaoshaReceive(String message) {
		log.info("receive message:"+message);
		MiaoshaMessage mm  = RedisService.stringToBean(message, MiaoshaMessage.class);
		MiaoshaUser user = mm.getUser();
		long goodsId = mm.getGoodsId();

		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		int stock = goods.getStockCount();
		if(stock <= 0) {
			return;
		}
		//判断是否已经秒杀到了
		MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
		if(order != null) {
			return;
		}
		//减库存 下订单 写入秒杀订单
		miaoshaService.miaosha(user, goods);
	}
}
