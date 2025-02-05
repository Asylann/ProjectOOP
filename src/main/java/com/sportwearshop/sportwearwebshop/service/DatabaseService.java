package com.sportwearshop.sportwearwebshop.service;

import com.sportwearshop.sportwearwebshop.config.DatabaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    private final DatabaseConfig databaseConfig;

    @Autowired
    public DatabaseService(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public void printDatabaseConfig() {
        System.out.println("Database URL: " + databaseConfig.getUrl());
        System.out.println("Database Username: " + databaseConfig.getUsername());
    }
}
