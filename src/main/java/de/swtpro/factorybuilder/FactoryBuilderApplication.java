package de.swtpro.factorybuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class FactoryBuilderApplication {
    public static void main(String[] args) {
        SpringApplication.run(FactoryBuilderApplication.class, args);
    }

}
