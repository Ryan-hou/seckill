package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Ryan-hou
 * @description:
 * @className: SeckillDaoTest
 * @date January 13,2017
 */

/**
 * 配置spring和junit整合,junit启动时加载springIoC容器
 * spring-WebApp,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void testReduceNumber() throws Exception {
        /**
         * 14:09:12.649 [main] DEBUG o.m.s.t.SpringManagedTransaction -
         * JDBC Connection [com.mchange.v2.c3p0.impl.NewProxyConnection@24be2d9c] will not be managed by Spring
         14:09:12.683 [main] DEBUG o.s.dao.SeckillDao.reduceNumber - ==>
         Preparing: UPDATE seckill SET number = number - 1 WHERE seckill_id = ? AND start_time <= ? AND end_time >= ? AND number > 0
         14:09:12.753 [main] DEBUG o.s.dao.SeckillDao.reduceNumber - ==>
         Parameters: 1000(Long), 2017-01-13 14:09:11.913(Timestamp), 2017-01-13 14:09:11.913(Timestamp)
         14:09:12.763 [main] DEBUG o.s.dao.SeckillDao.reduceNumber - <==
         Updates: 0
         14:09:12.763 [main] DEBUG org.mybatis.spring.SqlSessionUtils -
         Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@3deb2326]
         0
         */
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000L, killTime);
        System.out.println("updateCount=" + updateCount);
    }

    @Test
    public void testQueryById() throws Exception {
        long id = 1000L;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
        /**
         * Seckill(
         * seckillId=1000,
         * name=1000元秒杀iPhone6,
         * number=100,
         * startTime=Sun Jan 08 00:00:00 CST 2017,
         * endTime=Mon Jan 09 00:00:00 CST 2017,
         * createTime=Sun Jan 08 14:26:22 CST 2017)
         */
    }

    /**
     * Parameter 'offset' not found. Available parameters are [0, 1, param1, param2]
     * List<Seckill> queryAll(int offset, int limit) -> queryAll(arg0, arg1)
     * Java 没有保存形参的记录:需要使用 @Param 注解绑定形参的名字
     */
    @Test
    public void testQueryAll() throws Exception {
        List<Seckill> seckills = seckillDao.queryAll(0, 100);
        for (Seckill seckill : seckills) {
            // seckills 不会为 null,所以for-each循环不会NPE
            System.out.println(seckill);
        }
    }
}