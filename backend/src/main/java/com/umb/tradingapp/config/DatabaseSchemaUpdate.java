package com.umb.tradingapp.config;


import org.springframework.beans.factory.annotation.Autowired;
import com.umb.tradingapp.security.entity.RoleEntity;
import com.umb.tradingapp.security.repo.RoleRepository;

import java.io.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

@Configuration
public class DatabaseSchemaUpdate  {

    private static final String FLAG_FILE_PATH = "db-update.flag";

    @Autowired
    private RoleRepository roleRepository;    

    @PostConstruct
    void transferData( ) {
        File flagFile = new File(FLAG_FILE_PATH);

        if (flagFile.exists()) {
            // Read the last run timestamp from the file
            try (BufferedReader reader = new BufferedReader(new FileReader(flagFile))) {
                String line = reader.readLine();
                Instant lastRun = Instant.parse(line);

                // If less than a minute has passed since the last run, don't run the function
                if (Instant.now().isBefore(lastRun.plus(1, ChronoUnit.MINUTES))) {
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Transfering data");
        
        List<RoleEntity> roles = new ArrayList<>();
        
        if (roleRepository.findByRoleName("USER").isEmpty()) {
            RoleEntity userRole = new RoleEntity();
            userRole.setRoleName("USER");
            roles.add(userRole);
        }
        
        if (roleRepository.findByRoleName("ADMIN").isEmpty()) {
            RoleEntity adminRole = new RoleEntity();
            adminRole.setRoleName("ADMIN");
            roles.add(adminRole);
        }
        
        if (roleRepository.findByRoleName("SUPER_ADMIN").isEmpty()) {
            RoleEntity superAdminRole = new RoleEntity();
            superAdminRole.setRoleName("SUPER_ADMIN");
            roles.add(superAdminRole);
        }
        
        roleRepository.saveAll(roles);



        // Write the current timestamp to the file
        try (PrintWriter writer = new PrintWriter(new FileWriter(flagFile, false))) {
            writer.println(Instant.now().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}