package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.ExposerDto;
import org.seckill.dto.SeckillExecutionDto;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Ryan-hou
 * @description:
 * @className: SeckillServiceTest
 * @date January 15,2017
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})

public class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void testGetSeckillList() throws Exception {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}", list);
    }

    @Test
    public void testGetById() throws Exception {
        long id = 1000L;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}", seckill);
    }

    @Test
    public void testExecuteSeckillProcedure() throws Exception {
        long seckillId = 1000L;
        long phone = 15527273168L;
        ExposerDto exposerDto = seckillService.exportSeckillUrl(seckillId);
        if (exposerDto.isExposed()) {
            String md5 = exposerDto.getMd5();
            SeckillExecutionDto seckillExecutionDto = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            logger.info(seckillExecutionDto.getStateInfo());
        } else {
            // 秒杀未开启
            logger.info("秒杀未开启,exposerDto={}", exposerDto);
        }
    }

    // 下面注释的两个方法,应该组成一个完整的测试逻辑
    // 集成测试代码完整逻辑,注意可重复执行
    @Test
    public void testSeckillLogic() throws Exception {
        long id = 1000L;
        ExposerDto exposerDto = seckillService.exportSeckillUrl(id);
        if (exposerDto.isExposed()) {
            logger.info("exposerDto={}", exposerDto);
            long phone = 15527273169L;
            String md5 = exposerDto.getMd5();
            try {
                SeckillExecutionDto seckillExecutionDto = seckillService.executeSeckill(id, phone, md5);
                logger.info("result={}", seckillExecutionDto);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            }
        } else {
            // 秒杀未开启
            logger.warn("exposerDto={}", exposerDto);
        }
    }

//    @Test
//    public void testExportSeckillUrl() throws Exception {
//        long id = 1000L;
//        ExposerDto exposerDto = seckillService.exportSeckillUrl(id);
//        logger.info("exposerDto={}", exposerDto);
//        /**
//         * exposerDto=ExposerDto{exposed=true,
//         * md5='9874e08ca45ee49b3a6b214804092c9e',
//         * seckillId=1000,
//         * now=0, start=0, end=0}
//         */
//    }
//
//    @Test
//    public void testExecuteSeckill() throws Exception {
//        long id = 1000L;
//        long phone = 15527273160L;
//        String md5 = "9874e08ca45ee49b3a6b214804092c9e";
//        try {
//            SeckillExecutionDto seckillExecutionDto = seckillService.executeSeckill(id, phone, md5);
//            logger.info("result={}", seckillExecutionDto);
//        } catch (RepeatKillException e) {
//            logger.error(e.getMessage());
//        } catch (SeckillCloseException e) {
//            logger.error(e.getMessage());
//        }
//        /**
//         * result=SeckillExecutionDto{seckillId=1000,
//         * state=1, stateInfo='秒杀成功',
//         * successKilled=SuccessKilled{seckillId=1000, userPhone=15527273160, state=0, createTime=Sun Jan 15 15:08:41 CST 2017, seckill=Seckill(seckillId=1000, name=1000元秒杀iPhone6, number=98, startTime=Sun Jan 15 15:08:41 CST 2017, endTime=Mon Feb 20 00:00:00 CST 2017, createTime=Sun Jan 08 14:26:22 CST 2017)}}
//         */
//    }
}