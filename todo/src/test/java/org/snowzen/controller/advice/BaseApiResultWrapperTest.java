package org.snowzen.controller.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author snow-zen
 */
@JsonTest
public class BaseApiResultWrapperTest {

    @Autowired
    private ObjectMapper objectMapper;

    private BaseApiResultWrapper wrapper;

    @BeforeEach
    public void setup() {
        this.wrapper = new BaseApiResultWrapper();
        ReflectionTestUtils.setField(this.wrapper, "objectMapper", objectMapper);
    }

    @Test
    public void testStringReturn() {
        ServletServerHttpRequest request = new ServletServerHttpRequest(new MockHttpServletRequest());
        ServletServerHttpResponse response = new ServletServerHttpResponse(new MockHttpServletResponse());
        Class<? extends HttpMessageConverter<?>> converterType = StringHttpMessageConverter.class;
        MethodParameter returnType = new MethodParameter(ClassUtils.getMethod(this.getClass(), "handlerString"), -1);
        MediaType contentType = MediaType.TEXT_PLAIN;
        String body = "test";

        assertTrue(wrapper.supports(returnType, converterType));

        String result = (String) wrapper.beforeBodyWrite(body, returnType, contentType, converterType, request, response);

        assertNotNull(result);
        assertTrue(result.contains("\"code\":200"));
        assertTrue(result.contains("\"msg\":\"成功\""));
        assertTrue(result.contains("\"result\":\"test\""));
    }

    @ResponseBody
    public String handlerString() {
        return "";
    }
}
