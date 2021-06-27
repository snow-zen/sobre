package org.snowzen.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.snowzen.exception.NotFoundDataException;
import org.snowzen.model.ApiResult;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * 业务异常处理器
 *
 * @author snow-zen
 */
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@RestControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(NotFoundDataException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiResult handleNotFoundData(NotFoundDataException e) {
        log.error("未找到数据记录", e);
        return ApiResult.fail(INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}
