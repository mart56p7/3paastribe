package com.example.demo.repositories;

import com.example.demo.models.TTTGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TTTTGameRepository extends JpaRepository<TTTGame, Integer> {
    @Query("SELECT g FROM TTTGame g, TTTPlayer p, TTTToken t WHERE ((g.circleplayer = p AND p.token = t AND t.token = ?1) OR (g.crossplayer = p AND p.token = t AND t.token = ?1)) AND g.winner = null")
    TTTGame findGame(String token);

    @Query("SELECT g FROM TTTGame g, TTTPlayer p, TTTToken t WHERE g.id = ?1 AND ((g.circleplayer = p AND p.token = t) OR (g.circleplayer = p AND p.token = t))")
    TTTGame findGameByIdAndToken(int gameid, String token);
}
