package com.zero.miaosha.controller;

import com.zero.miaosha.domain.MiaoshaUser;
import com.zero.miaosha.domain.OrderInfo;
import com.zero.miaosha.redis.RedisService;
import com.zero.miaosha.result.CodeMsg;
import com.zero.miaosha.result.Result;
import com.zero.miaosha.service.GoodsService;
import com.zero.miaosha.service.MiaoshaUserService;
import com.zero.miaosha.service.OrderService;
import com.zero.miaosha.vo.GoodsVo;
import com.zero.miaosha.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    //优化4：订单详情页静态化，此处特别注意横向越权的问题。
    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, MiaoshaUser user,
                                      @RequestParam("orderId") long orderId) {
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //横向越权，只要本人才能查询个人的订单。
        OrderInfo order = orderService.getOrderById(orderId,user.getId());
        if(order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(order);
        vo.setGoods(goods);
        return Result.success(vo);
    }
}
