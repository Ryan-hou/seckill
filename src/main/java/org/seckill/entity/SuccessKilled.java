package org.seckill.entity;

import java.util.Date;

/**
 * @author Ryan-hou
 * @description:
 * @className: SuccessKilled
 * @date January 08,2017
 */
public class SuccessKilled {

    private long seckillId;

    private long userPhone;

    // 对应数据库的tinyint类型;这里采用 int而不是byte,避免了后面 int强转 byte 的麻烦,牺牲一部分空间换取代码的简洁
    private int state;

    private Date createTime;

    // 变通
    // 一对多关系: 一个秒杀商品对应多个秒杀成功记录
    private Seckill seckill;

    @Override
    public String toString() {
        return "SuccessKilled{" +
                "seckillId=" + seckillId +
                ", userPhone=" + userPhone +
                ", state=" + state +
                ", createTime=" + createTime +
                ", seckill=" + seckill +
                '}';
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Seckill getSeckill() {
        return seckill;
    }

    public void setSeckill(Seckill seckill) {
        this.seckill = seckill;
    }
}
