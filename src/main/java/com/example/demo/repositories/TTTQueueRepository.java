package com.example.demo.repositories;

import com.example.demo.models.TTTQueue;
import com.example.demo.models.TTTToken;
import org.springframework.data.jpa.repository.JpaRepository;



public interface TTTQueueRepository extends JpaRepository<TTTQueue, Integer> {
    void deleteByTokenTokenEquals(String token);
    void deleteByTokenEquals(TTTToken token);
}
