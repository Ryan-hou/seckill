package org.seckill.exception;

/**
 * @author Ryan-hou
 * @description:
 * @className: RepeatKillException
 * @date January 13,2017
 */
public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
