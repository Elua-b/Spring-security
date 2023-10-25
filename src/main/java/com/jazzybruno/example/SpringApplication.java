package com.jazzybruno.example;

import com.jazzybruno.example.v1.enums.Roles;
import com.jazzybruno.example.v1.models.Role;
import com.jazzybruno.example.v1.models.User;
import com.jazzybruno.example.v1.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class SpringApplication {
    private final RoleRepository roleRepository;

    @Autowired
    public SpringApplication(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Bean
    public  void registerRoles(){
        System.out.println("Hello World i am inaerting roles and there are not available");
        List<Role> existingRoles = roleRepository.findAll();
        if (existingRoles.isEmpty()) {
            System.out.println("Hello World i am inaerting roles");
            long id = 1l;
            for (Roles role : Roles.values()) {
                Role sampleRole = new Role (id ,  role.name());
                roleRepository.save(sampleRole);
                id++;
            }
        }
    }
    public static void main(String[] args) {


        org.springframework.boot.SpringApplication.run(SpringApplication.class, args);
    }
}
