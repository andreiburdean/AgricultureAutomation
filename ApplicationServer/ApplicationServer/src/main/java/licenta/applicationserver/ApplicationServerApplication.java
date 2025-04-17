package licenta.applicationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAsync
public class ApplicationServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationServerApplication.class, args);
    }
}

