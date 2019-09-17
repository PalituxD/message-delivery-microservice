package org.justfoolingaround.apps.messagedelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@PropertySources(
        @PropertySource(value = {
                "classpath:application.properties",
                "classpath:endpoints.properties",
                "classpath:email.properties",
                "classpath:business.properties"
        }))
@EnableScheduling
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}