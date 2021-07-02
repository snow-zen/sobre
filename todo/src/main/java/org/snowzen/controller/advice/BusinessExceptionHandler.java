package org.snowzen.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.snowzen.exception.NotFoundDataException;
import org.snowzen.model.ApiResult;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * 业务异常处理器
 *
 * @author snow-zen
 */
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@RestControllerAdvice
public class BusinessExceptionHandler { // TODO 后续处理返回消息格式

    @ExceptionHandler(NotFoundDataException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiResult handleNotFoundData(NotFoundDataException e) {
        log.error("未找到数据记录", e);
        return ApiResult.fail(INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResult handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.error("接口非法入参", e);
        return ApiResult.fail(BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResult handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.error("接口非法入参", e);
        String message = e.getBindingResult().getAllErrors()
                .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
        return ApiResult.fail(BAD_REQUEST.value(), message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResult handleConstraintViolation(ConstraintViolationException e) {
        log.error("接口非法入参", e);
        String message = e.getConstraintViolations()
                .stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        return ApiResult.fail(BAD_REQUEST.value(), message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResult handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.error("接口非法入参", e);
        return ApiResult.fail(BAD_REQUEST.value(), e.getMessage());
    }
}
