package com.ativie.boxservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class BoxserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BoxserviceApplication.class, args);
    }
}
