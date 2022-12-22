package com.vesko;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class}
)
@ConfigurationPropertiesScan
public class MoneyServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(MoneyServiceApplication.class).run(args);
    }
}
