package org.seckill.service;

/**
 * @author Ryan-hou
 * @description:
 * @className: SeckillService
 * @date January 13,2017
 */

import org.seckill.dto.ExposerDto;
import org.seckill.dto.SeckillExecutionDto;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口:站在"使用者"角度设计接口
 * 三个方面:方法定义粒度,参数,返回类型(return 类型/异常)
 */
public interface SeckillService {

    /**
     * 查询所有秒杀记录
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启时输出秒杀接口地址,
     * 否则输出系统时间和秒杀时间
     * ExposerDto:DTO对象,封装Service返回的数据,传递给web层
     * @param seckillId
     */
    ExposerDto exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillCloseException
     */
    SeckillExecutionDto executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException;

    /**
     * 执行秒杀操作by 存错过程
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecutionDto executeSeckillProcedure(long seckillId, long userPhone, String md5);

}
