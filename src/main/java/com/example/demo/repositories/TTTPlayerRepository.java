package com.example.demo.repositories;

import com.example.demo.models.TTTPlayer;
import com.example.demo.models.TTTToken;
import org.springframework.data.jpa.repository.JpaRepository;



public interface TTTPlayerRepository extends JpaRepository<TTTPlayer, Integer> {
    TTTPlayer findByTokenAndPlayername(TTTToken token, String playername);

}
