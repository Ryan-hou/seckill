package org.seckill.exception;

/**
 * @author Ryan-hou
 * @description:
 * @className: SeckillCloseException
 * @date January 13,2017
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
