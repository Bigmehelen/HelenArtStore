package com.helenartstore.HelenArtStore;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelenArtStoreApplication {

    public static void main(String[] args) {
        // Manually load .env and set them as System properties
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(HelenArtStoreApplication.class, args);
    }
}