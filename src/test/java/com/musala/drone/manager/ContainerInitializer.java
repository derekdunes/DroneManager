package com.musala.drone.manager;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.ResourceUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.FileNotFoundException;

public class ContainerInitializer {

    public static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest").withDatabaseName("integration-tests-db");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.initialization-mode", () -> "always");
        registry.add("spring.batch.initialize-schema", () -> "always");
        registry.add("spring.jpa.show-sql", () -> "true");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        registry.add("spring.jpa.properties.hibernate.format_sql", () -> "true");

    }

    @BeforeAll
    static void setUp(){
        mySQLContainer.start();
    }



    @AfterAll
    static void endSetUp(){
        mySQLContainer.stop();
    }

}
