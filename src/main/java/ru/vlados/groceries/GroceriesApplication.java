package ru.vlados.groceries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class GroceriesApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroceriesApplication.class, args);
    }

}


