package pl.agh.edu.to.aleksandria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AleksandriaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AleksandriaApplication.class, args);
    }

}
