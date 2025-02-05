package com.sportwearshop.sportwearwebshop.controller;

import com.sportwearshop.sportwearwebshop.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseController {

    private final DatabaseService databaseService;

    @Autowired
    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @GetMapping("/db-config")
    public String getDatabaseConfig() {
        databaseService.printDatabaseConfig();
        return "Database configuration printed to console";
    }
}
