package com.example.demo.services;

import com.example.demo.models.TTTGame;
import com.example.demo.models.TTTPlayer;
import com.example.demo.models.TTTQueue;
import com.example.demo.models.TTTToken;
import com.example.demo.repositories.TTTPlayerRepository;
import com.example.demo.repositories.TTTQueueRepository;
import com.example.demo.repositories.TTTTGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

//Ref: https://mycuteblog.com/spring-task-scheduler-cron-job-example/
@Service
@EnableScheduling
@Transactional
public class TTTGameMaster {
    TTTQueueRepository queuerepo;
    TTTTGameRepository gamerepo;
    TTTPlayerRepository playerrepo;
    @Autowired
    public TTTGameMaster(TTTQueueRepository queuerepo, TTTTGameRepository gamerepo, TTTPlayerRepository playerrepo){
        this.queuerepo = queuerepo;
        this.gamerepo = gamerepo;
        this.playerrepo = playerrepo;
    }

    @Scheduled(fixedDelay = 150000)
    public void fixedDelayTask() {
        System.out.println("Cleaning - NOT Implemented!");
        System.out.println("- Remove old tokens");
        System.out.println("- Remove old games");
    }

    @Scheduled(fixedDelay = 2000)
    public void createGames() {
        //System.out.println("Creating games");
        List<TTTQueue> qs = getQueue();
        if(qs != null){
            for(int i = 0; i < qs.size(); i++){
                if(i % 2 == 1){
                    TTTPlayer player1 = playerrepo.findByTokenAndPlayername(qs.get(i-1).getToken(), qs.get(i-1).getPlayername());
                    if(player1 == null){ //Incase the player has not been created
                        player1 = new TTTPlayer(qs.get(i-1).getToken(), qs.get(i-1).getPlayername());
                    }
                    TTTPlayer player2 = playerrepo.findByTokenAndPlayername(qs.get(i).getToken(), qs.get(i).getPlayername());
                    if(player2 == null){ //Incase the player has not been created
                        player2 = new TTTPlayer(qs.get(i).getToken(), qs.get(i).getPlayername());
                    }
                    System.out.println("Creating new game between");
                    System.out.println(qs.get(i-1).getPlayername() + " " + player1.getToken().getToken());
                    System.out.println("vs");
                    System.out.println(qs.get(i).getPlayername() + " " + player2.getToken().getToken());
                    queuerepo.deleteByTokenEquals(player1.getToken());
                    queuerepo.deleteByTokenEquals(player2.getToken());
                    TTTGame g = gamerepo.save(new TTTGame(player1, player2));
                    System.out.println("GameData player1: http://localhost/REST/getGameData/" + g.getId() + "/" + player1.getToken().getToken());
                    System.out.println("GameData player2: http://localhost/REST/getGameData/" + g.getId() + "/" + player2.getToken().getToken());
                }
            }
        }
    }

    private List<TTTQueue> getQueue(){
        return queuerepo.findAll();
    }
}
