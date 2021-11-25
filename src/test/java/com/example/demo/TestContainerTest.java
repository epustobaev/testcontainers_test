package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;

@Testcontainers
public class TestContainerTest {
    Logger logger = LoggerFactory.getLogger(TestContainerTest.class);

    PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:12.0")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");
    PGSimpleDataSource ds;

    @BeforeEach
    void setUp() {
        postgres.start();
        ds = new PGSimpleDataSource();
        ds.setURL(postgres.getJdbcUrl());
        ds.setDatabaseName("test");
        ds.setUser("test");
        ds.setPassword("test");
    }

    @Test
    void testDb() throws SQLException {
        var connection = ds.getConnection();
        var result = connection.createStatement().executeQuery("SELECT 'TEST', NOW()");
        result.next();
        Assertions.assertEquals("TEST", result.getString(1));
        logger.error("Result: {}", result.getString(2));
        Assertions.assertEquals("TEST2", result.getString(2));
    }
}
