package org.seckill.enums;

/**
 * @author Ryan-hou
 * @description: 使用枚举表述常量数据字段
 * @className: SeckillStateEnum
 * @date January 14,2017
 */
public enum SeckillStateEnum {

    SUCCESS(1, "秒杀成功"),
    END(0, "秒杀结束"),
    REPEAT_KILL(-1, "重复秒杀"),
    INNER_ERROR(-2, "系统异常"),
    DATA_REWRITE(-3, "数据篡改");

    private int state;

    private String stateInfo;

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    SeckillStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static SeckillStateEnum stateOf(int state) {
        for (SeckillStateEnum seckillState : values()) {
            if (seckillState.getState() == state) {
                return seckillState;
            }
        }
        return null;
    }
}
