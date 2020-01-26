package com.example.demo.repositories;

import com.example.demo.models.TTTToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TTTTokenRepository extends JpaRepository<TTTToken, Integer> {
    TTTToken findByToken(String token);
}
