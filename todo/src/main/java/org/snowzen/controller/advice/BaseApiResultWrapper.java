package org.snowzen.controller.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.snowzen.model.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一响应体封装器
 *
 * @author snow-zen
 */
@Slf4j
@RestControllerAdvice
@SuppressWarnings("NullableProblems")
public class BaseApiResultWrapper implements ResponseBodyAdvice<Object> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !ApiResult.class.isAssignableFrom(returnType.getContainingClass()) &&
                (returnType.hasMethodAnnotation(ResponseBody.class) || MergedAnnotations.from(returnType.getContainingClass(), MergedAnnotations.SearchStrategy.INHERITED_ANNOTATIONS).isPresent(ResponseBody.class));
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        ApiResult apiResult = ApiResult.success(body);

        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(apiResult);
            } catch (JsonProcessingException e) {
                log.error(String.format("响应体封装错误，封装对象为[%s]，响应类型为[%s]", body, selectedContentType), e);
                return "";
            }
        }
        return apiResult;
    }
}
