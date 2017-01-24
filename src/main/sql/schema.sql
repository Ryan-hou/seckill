-- 数据库初始化脚本

-- 创建数据库
CREATE database seckill;
-- 使用数据库
use seckill;
-- 创建秒杀库存表
CREATE TABLE seckill(
`seckill_id` bigint NOT NULL auto_increment comment '商品库存id',
`name` varchar(120) NOT NULL comment '商品名称',
`number` int NOT NULL comment '库存数量',
`start_time` DATETIME NOT NULL comment '秒杀开始时间',
`end_time` DATETIME NOT NULL comment '秒杀结束时间',
`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
PRIMARY KEY (seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)engine=InnoDB AUTO_INCREMENT=1000 DEFAULT charset=utf8 comment='秒杀库存表';

-- 初始化数据
INSERT INTO
  seckill(name,number,start_time,end_time)
VALUES
  ('1000元秒杀iPhone6',100,'2017-01-08 00:00:00', '2017-01-09 00:00:00'),
  ('500元秒杀iPad2',200,'2017-01-08 00:00:00', '2017-01-09 00:00:00'),
  ('300元秒杀小米4',300,'2017-01-08 00:00:00', '2017-01-09 00:00:00'),
  ('200元秒杀红米note',400,'2017-01-08 00:00:00', '2017-01-09 00:00:00');

-- 秒杀成功明细表
-- 用户登录认证相关信息
CREATE TABLE success_killed(
`seckill_id` bigint NOT NULL comment '秒杀商品id',
`user_phone` bigint NOT NULL comment '用户手机号',
`state` tinyint NOT NULL DEFAULT -1 comment '状态标示:-1:无效 0:成功 1:已付款 2:已发货',
`create_time` TIMESTAMP NOT NULL comment '创建时间',
PRIMARY KEY (seckill_id,user_phone), /*联合主键,防止同一用户对同一商品的重复秒杀*/
key idx_create_time(create_time)
)engine=InnoDB DEFAULT charset=utf8 comment='秒杀成功明细表';

-- 连接数据库
mysql -uroot -p

-- 为什么手写DDL
-- 记录每一次上线的DDL修改
-- 上线v1.1
ALTER TABLE seckill
DROP INDEX idx_create_time,
ADD INDEX idx_c_s(start_time,create_time);

-- 上线v1.2
-- ddl

