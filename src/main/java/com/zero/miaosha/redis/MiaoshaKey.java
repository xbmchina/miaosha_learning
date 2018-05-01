package com.zero.miaosha.redis;

public class MiaoshaKey extends BasePrefix{




    public MiaoshaKey(String prefix) {
        super(prefix);
    }
    public static MiaoshaKey isGoodsOver = new MiaoshaKey("go");
    public static MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey("code");
    public static MiaoshaKey getMiaoshaPath = new MiaoshaKey("path");
}
