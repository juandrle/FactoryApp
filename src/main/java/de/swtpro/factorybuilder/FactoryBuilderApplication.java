package de.swtpro.factorybuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FactoryBuilderApplication {
    private final boolean jenkinsWorking = true;
    public static void main(String[] args) {
        SpringApplication.run(FactoryBuilderApplication.class, args);
    }

}
