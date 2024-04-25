package com.umb.tradingapp.security.service;

import com.umb.tradingapp.security.entity.RoleEntity;
import com.umb.tradingapp.security.entity.UserEntity;
import com.umb.tradingapp.security.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;



    private final PasswordEncoder passwordEncoder;

    public RegistrationService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    public void saveUser (String udaje[]) {


        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(udaje[0]);

        // Zasifrovanie hesla
        String encryptedPassword = passwordEncoder.encode(udaje[1]);

        userEntity.setPasswordHash(encryptedPassword);

        // Vytvorenie setu pre roly a pridanie jednej role
        Set<RoleEntity> roles = new HashSet<>();

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L); // 1 znamena ze je user

        roles.add(roleEntity);

        userEntity.setRoles(roles);

        // Uloženie používateľa
        userRepository.save(userEntity);


    }



}
