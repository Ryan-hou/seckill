package org.seckill.service.impl;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.ExposerDto;
import org.seckill.dto.SeckillExecutionDto;
import org.seckill.dao.cache.RedisDao;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ryan-hou
 * @description:
 * @className: SeckillServiceImpl
 * @date January 14,2017
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private RedisDao redisDao;
    // md5盐值字符串,用于混淆md5
    private final String SALT = "qfjafhr9241971894((0a8f0a***";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public ExposerDto exportSeckillUrl(long seckillId) {
        // 优化点:缓存优化,超时的基础上维护一致性(一个秒杀单建立后一般不会修改,如果要改,废弃重建)
        // 1.访问redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            // 2.访问数据库
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {
                return new ExposerDto(false, seckillId);
            } else {
                // 3.放入redis
                redisDao.putSeckill(seckill);
            }
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        // 系统当前时间
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime()
                || nowTime.getTime() > endTime.getTime()) {
            return new ExposerDto(false, seckillId, nowTime.getTime(), startTime.getTime(),
                    endTime.getTime());
        }
        // 转化特定字符串的过程,不可逆
        String md5 = getMD5(seckillId);
        return new ExposerDto(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + SALT;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    @Transactional
    /**
     * 使用注解控制事务的优点:
     * 1:开发团队达成一致约定,明确标注事务方法的编程风格
     * 2:保证事务方法的执行时间尽可能短,不要穿插其他网络操作(RPC/HTTP请求)或者剥离到事务方法外部
     * 3:不是所有的方法都需要事务,如只有一条修改操作,只读操作不需要事务控制
     */
    public SeckillExecutionDto executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        // 执行秒杀逻辑: 减库存 + 记录购买行为
        Date nowTime = new Date();

        /**先insert后update,降低了一半的网络延迟和GC操作时间,从而减少了行级锁的占有时间,提高了并发**/
        try {
            // 记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            // 唯一: seckillId+userPhone
            if (insertCount <= 0) {
                // 重复秒杀
                throw new RepeatKillException("seckill repeat");
            } else {
                // 减库存,热点商品竞争
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    // 没有更新到记录,秒杀结束,rollback(这里没有更新到记录的原因除了秒杀时间外,还有库存等原因
                    // 而且库存一般是动态变化的,这里统一返回秒杀结束,对于用户来说,这种处理可以的)
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    // 秒杀成功 commit
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    // return new SeckillExecutionDto(seckillId, 1, "秒杀成功", successKilled);
                    return new SeckillExecutionDto(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e) {
            throw e;
        } catch (RepeatKillException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 所有编译器异常转为运行期异常,由Spring声明式事务来控制回滚
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }

    @Override
    public SeckillExecutionDto executeSeckillProcedure(long seckillId, long userPhone, String md5) {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            return new SeckillExecutionDto(seckillId, SeckillStateEnum.DATA_REWRITE);
        }
        Date killTime = new Date();
        Map<String, Object> map = new HashMap<>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);
        // 执行存储过程,result被赋值
        try {
            seckillDao.killByProcedure(map);
            // 获取result
            int result = MapUtils.getInteger(map, "result", -2);
            if (result == 1) {
                SuccessKilled sk = successKilledDao.
                        queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecutionDto(seckillId, SeckillStateEnum.SUCCESS, sk);
            } else {
                return new SeckillExecutionDto(seckillId, SeckillStateEnum.stateOf(result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SeckillExecutionDto(seckillId, SeckillStateEnum.INNER_ERROR);
        }
    }
}
