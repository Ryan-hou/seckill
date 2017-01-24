package org.seckill.exception;

/**
 * @author Ryan-hou
 * @description: 秒杀相关业务异常
 * @className: SeckillException
 * @date January 13,2017
 */
public class SeckillException extends RuntimeException {

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
