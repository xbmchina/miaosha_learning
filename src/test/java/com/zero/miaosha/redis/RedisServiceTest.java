package com.zero.miaosha.redis;

import com.zero.miaosha.MiaoshaApplicationTests;
import com.zero.miaosha.domain.MiaoshaUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class RedisServiceTest extends MiaoshaApplicationTests {

    @Autowired
    private RedisService redisService;
    @Test
    public void  testRedis(){
        redisService.set(MiaoshaUserKey.token,"hehe","12");
        System.out.println(redisService.get(MiaoshaUserKey.token,"hh",String.class));
    }
}