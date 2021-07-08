package org.snowzen.controller.advice;

import org.snowzen.exception.NotFoundDataException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.snowzen.controller.advice.BusinessExceptionHandler.NOT_FOUND_DATA;


/**
 * @author snow-zen
 */
@RestController
@RequestMapping("/exception")
public class ExceptionTestController {

    @GetMapping("/NullPointerException")
    public void nullPointerException() {
        throw new NullPointerException();
    }

    @GetMapping("/IllegalStateException")
    public void illegalStateException() {
        throw new IllegalStateException();
    }

    @GetMapping("/NotFoundDataException")
    public void notFoundDataException() {
        throw new NotFoundDataException(NOT_FOUND_DATA);
    }

}
