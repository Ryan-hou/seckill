package org.seckill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Ryan-hou
 * @description:
 * @className: ExposerDto
 * @date January 13,2017
 */
@ApiModel(value = "ExposerDto", description = "秒杀暴露接口dto对象")
public class ExposerDto {

    // 是否开启秒杀
    @ApiModelProperty(value = "是否开启秒杀")
    private boolean exposed;

    // 一种加密措施
    @ApiModelProperty(value = "秒杀地址md5值")
    private String md5;

    @ApiModelProperty(value = "秒杀商品id")
    private long seckillId;

    // 系统当前时间(毫秒)
    @ApiModelProperty(value = "系统当前时间")
    private long now;

    // 开始时间
    @ApiModelProperty(value = "秒杀开始时间")
    private long start;

    // 结束时间
    @ApiModelProperty(value = "秒杀结束时间")
    private long end;

    @Override
    public String toString() {
        return "ExposerDto{" +
                "exposed=" + exposed +
                ", md5='" + md5 + '\'' +
                ", seckillId=" + seckillId +
                ", now=" + now +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    public ExposerDto(boolean exposed, long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }

    public ExposerDto(boolean exposed, String md5, long seckillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public ExposerDto(boolean exposed, long seckillId, long now, long start, long end) {
        this.exposed = exposed;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}

