package com.bookbla.americano;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AmericanoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmericanoApplication.class, args);
    }
}
