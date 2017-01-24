package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author Ryan-hou
 * @description:
 * @className: SuccessKilledDaoTest
 * @date January 13,2017
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void testInsertSuccessKilled() throws Exception {
        /**
         * 第一次:insertCount=1
         * 第二次:insertCount=0
         */
        long id = 1000L;
        long userPhone = 15527273169L;
        int insertCount = successKilledDao.insertSuccessKilled(id, userPhone);
        System.out.println("insertCount=" + insertCount);
    }

    @Test
    public void testQueryByIdWithSeckill() throws Exception {
        long id = 1000L;
        long userPhone = 15527273169L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id, userPhone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
        /**
         * SuccessKilled{seckillId=1000,
         * userPhone=15527273169,
         * state=0,
         * createTime=Fri Jan 13 14:33:53 CST 2017,
         * seckill=Seckill(seckillId=1000, name=1000元秒杀iPhone6, number=100, startTime=Sun Jan 08 00:00:00 CST 2017, endTime=Mon Jan 09 00:00:00 CST 2017, createTime=Sun Jan 08 14:26:22 CST 2017)}
         Seckill(seckillId=1000, name=1000元秒杀iPhone6, number=100, startTime=Sun Jan 08 00:00:00 CST 2017, endTime=Mon Jan 09 00:00:00 CST 2017, createTime=Sun Jan 08 14:26:22 CST 2017)
         */
    }
}