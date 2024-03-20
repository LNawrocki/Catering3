package pl.nawrockiit.catering3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
//@PropertySource("classpath:application.properties")
@PropertySource(value = "classpath:application.properties", encoding = "UTF-8")
public class Catering3Application {

    public static void main(String[] args) {
        SpringApplication.run(Catering3Application.class, args);
    }

}
