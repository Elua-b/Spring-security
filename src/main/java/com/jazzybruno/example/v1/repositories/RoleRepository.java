package com.jazzybruno.example.v1.repositories;

import com.jazzybruno.example.v1.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role , Long> {
    List<Role> findAll();
}
