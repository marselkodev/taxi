package ru.taxi;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@Slf4j
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        PropertyConfigurator.configure(
                "C:" + File.separator + "Users" + File.separator + "marce" + File.separator +
                        "IdeaProjects" + File.separator + "taxi" + File.separator + "src" + File.separator +
                        "main" + File.separator + "resources" + File.separator + "log4j.properties");
        log.info("Main starting");
        SpringApplication.run(Main.class, args);
    }
}
