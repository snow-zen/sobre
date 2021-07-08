package org.snowzen.controller.advice;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
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
public class BusinessExceptionHandler {

    public static final String NOT_FOUND_DATA = "未找到数据";
    public static final String INVALID_PARAM = "非法接口参数";
    public static final String INVALID_PARAM_DETAIL = "非法接口参数[%s]，值为[%s]";

    @ExceptionHandler(NotFoundDataException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiResult handleNotFoundData(NotFoundDataException e) {
        log.error(NOT_FOUND_DATA, e);
        return ApiResult.fail(INTERNAL_SERVER_ERROR.value(), NOT_FOUND_DATA);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResult handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        String message = String.format(INVALID_PARAM_DETAIL, e.getName(), e.getValue());
        log.error(message, e);
        return ApiResult.fail(BAD_REQUEST.value(), message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResult handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors()
                .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));
        log.error(message, e);
        return ApiResult.fail(BAD_REQUEST.value(), message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResult handleConstraintViolation(ConstraintViolationException e) {
        String message = e.getConstraintViolations()
                .stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("\n"));
        log.error(message, e);
        return ApiResult.fail(BAD_REQUEST.value(), message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResult handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        String message = INVALID_PARAM;
        if (e.getCause() instanceof InvalidFormatException) {
            message = parseInvalidFormatException((InvalidFormatException) e.getCause());
        }
        return ApiResult.fail(BAD_REQUEST.value(), message);
    }

    private String parseInvalidFormatException(InvalidFormatException e) {
        JsonLocation location = e.getLocation();
        return String.format("http body读取错误，错误信息为[line:%s, column:%s]", location.getLineNr(), location.getColumnNr());
    }
}
