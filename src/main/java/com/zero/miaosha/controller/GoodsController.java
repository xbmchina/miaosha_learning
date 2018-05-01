package com.zero.miaosha.controller;

import com.zero.miaosha.domain.MiaoshaUser;
import com.zero.miaosha.redis.GoodsKey;
import com.zero.miaosha.redis.RedisService;
import com.zero.miaosha.result.Result;
import com.zero.miaosha.service.GoodsService;
import com.zero.miaosha.service.MiaoshaUserService;
import com.zero.miaosha.vo.GoodsDetailVo;
import com.zero.miaosha.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	MiaoshaUserService userService;

	@Autowired
    RedisService redisService;

	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    GoodsService goodsService;

    @Autowired
    ApplicationContext applicationContext;

    //redis优化1：将商品列表保存到缓存中，列表页面静态化，特别注意要加：produces="text/html"  @ResponseBody
    @RequestMapping(value="/to_list", produces="text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user) {
    	model.addAttribute("user", user);
    	//1.2、从缓存中取数据
        String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        //原有业务逻辑
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
         //1.1、写到缓存中
        //手动渲染
        SpringWebContext ctx = new SpringWebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap(),applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    //优化2：静态化商品详情页。将页面放到static中静态化，利用js调用。
    @RequestMapping(value="/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response,Model model, MiaoshaUser user, @PathVariable("goodsId") long goodsId){
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStautus = 0;
        int remainSeconds = 0;
        if (now < startAt){
            miaoshaStautus = 0;
            remainSeconds = (int)((startAt - now) / 1000);
        }else if(now >endAt){
            miaoshaStautus = 2;
            remainSeconds = -1;
        }else {
            miaoshaStautus = 1;
            remainSeconds = 0;
        }

        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStautus);

        return Result.success(vo);
    }
}
