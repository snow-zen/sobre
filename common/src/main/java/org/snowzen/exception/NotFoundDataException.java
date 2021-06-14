package org.snowzen.exception;

/**
 * 数据不存在时抛出，且一般用于单条数据查询
 *
 * @author snow-zen
 */
public class NotFoundDataException extends RuntimeException {
    public NotFoundDataException() {
        super();
    }

    public NotFoundDataException(String message) {
        super(message);
    }

    public NotFoundDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
