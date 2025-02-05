package com.latihan.latihan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.latihan.latihan.entities.UserEntities;

@Repository
public interface UserRepository extends JpaRepository<UserEntities, Integer> {
    UserEntities findByEmail(String email);
}
