package com.zero.miaosha.service;

import com.zero.miaosha.dao.GoodsDao;
import com.zero.miaosha.domain.MiaoshaGoods;
import com.zero.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {


    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(Long goodsId){
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public GoodsVo getMiaoshaGoodsByGoodsId(Long goodsId){
        return goodsDao.getMiaoshaGoodsByGoodsId(goodsId);
    }
    public void reduceStock(GoodsVo goodsVo){
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goodsVo.getId());
        goodsDao.reduceStock(g);
    }

    public void resetStock(List<GoodsVo> goodsList) {
        for(GoodsVo goods : goodsList ) {
            MiaoshaGoods g = new MiaoshaGoods();
            g.setGoodsId(goods.getId());
            g.setStockCount(goods.getStockCount());
            goodsDao.resetStock(g);
        }
    }
}
