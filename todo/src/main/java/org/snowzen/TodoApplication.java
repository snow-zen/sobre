package org.snowzen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;

/**
 * @author snow-zen
 */
@SpringBootApplication(exclude = {
        WebSocketServletAutoConfiguration.class,
        JmxAutoConfiguration.class
})
public class TodoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoApplication.class);
    }
}
