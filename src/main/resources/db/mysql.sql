create TABLE miaosha_user (
	id BIGINT(20) NOT NULL COMMENT '用户ID,手机号码',
  nickname VARCHAR(255) NOT NULL,
	password VARCHAR(32) DEFAULT NULL COMMENT 'MD5(MD5(pass明文+固定salt)+salt',
	salt VARCHAR(10) DEFAULT NULL,
	head VARCHAR(128) DEFAULT NULL COMMENT '头像,云存储的ID',
	register_date datetime DEFAULT NULL COMMENT '注册时间',
	last_login_date datetime DEFAULT NULL COMMENT '上次登录时间',
	login_count int(11) DEFAULT '0' COMMENT '登录次数',
	PRIMARY KEY(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4



create TABLE goods (
	id BIGINT(20) NOT NULL auto_increment COMMENT '商品ID',
	goods_name VARCHAR(16) DEFAULT NULL COMMENT '商品名称',
	goods_title VARCHAR(64) DEFAULT NULL COMMENT '商品标题',
	goods_img VARCHAR(64) DEFAULT NULL COMMENT '商品图片',
	goods_detail LONGTEXT COMMENT '商品详情介绍',
	goods_price DECIMAL(10,2) DEFAULT '0.00' COMMENT '商品单价',
	goods_stock INT(11) DEFAULT '0' COMMENT '商品库存,-1表示没有限制',
	PRIMARY KEY(id)
);

INSERT INTO goods VALUES (1,'iphoneX','Apple iPhoneX 64GB 银色','/img/phonex.png',
'Apple iPhoneX 64GB 银色',8765.00,10000),(2,'小米note3','小米note3 64GB 银色','/img/phonex.png',
'小米note3 64GB 银色',8765.00,10000)



create TABLE miaosha_goods (
	id BIGINT(20) NOT NULL auto_increment COMMENT '秒杀商品表',
	goods_id BIGINT(20) DEFAULT NULL COMMENT '商品ID',
	goods_title VARCHAR(64) DEFAULT NULL COMMENT '商品标题',
	miaosha_price DECIMAL(10,2) DEFAULT '0.00' COMMENT '秒杀价',
	stock_count INT(11) DEFAULT NULL COMMENT '库存量',
	start_date datetime DEFAULT NULL COMMENT '秒杀开始时间',
	end_date datetime DEFAULT NULL COMMENT '秒杀结束时间',
	PRIMARY KEY(id)
);


INSERT INTO `miaosha`.`miaosha_goods` (`id`, `goods_id`, `goods_title`, `miaosha_price`, `stock_count`, `start_date`, `end_date`) VALUES ('2', '2', '贱卖', '0.02', '8', '2018-04-16 22:14:49', '2018-04-17 22:14:51');




create TABLE order_info (
	id BIGINT(20) NOT NULL auto_increment,
	user_id BIGINT(20) DEFAULT NULL COMMENT '用户ID',
	goods_id BIGINT(20) DEFAULT NULL COMMENT '商品ID',
	delivery_addr_id BIGINT(20) DEFAULT NULL COMMENT '收货地址ID',
	goods_name	VARCHAR(16) DEFAULT null COMMENT '冗余过来的商品',
	goods_count int(11) DEFAULT '0' COMMENT '商品数量',
	goods_price DECIMAL(10,2) DEFAULT '0.00' COMMENT '商品单价',
	order_channel TINYINT(4) DEFAULT '0' COMMENT '1 pc,2 android, 3 ios',
	status TINYINT(4) DEFAULT '0' COMMENT '订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成',
	create_date datetime DEFAULT NULL COMMENT '订单的创建时间',
	pay_date datetime DEFAULT NULL COMMENT '支付时间',
	PRIMARY KEY (id)

);


create TABLE miaosha_order (
	id BIGINT(20) NOT NULL auto_increment,
	user_id BIGINT(20) DEFAULT NULL COMMENT '用户ID',
	order_id BIGINT(20) DEFAULT NULL COMMENT '订单ID',
	goods_id BIGINT(20) DEFAULT NULL COMMENT '商品ID',
	PRIMARY KEY(id)
);











