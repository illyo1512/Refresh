package com.refresh.refresh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.refresh.refresh.repository")
@EntityScan(basePackages = "com.refresh.refresh.entity")
public class RefreshApplication {

    public static void main(String[] args) {
        SpringApplication.run(RefreshApplication.class, args);
    }

}
