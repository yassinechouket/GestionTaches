package com.chouket370.gestiontaches;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class GestionTachesApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionTachesApplication.class, args);
    }

}
