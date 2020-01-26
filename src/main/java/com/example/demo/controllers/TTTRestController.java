package com.example.demo.controllers;

import com.example.demo.TTTConfig;
import com.example.demo.models.TTTGame;
import com.example.demo.models.TTTMove;
import com.example.demo.models.TTTQueue;
import com.example.demo.models.TTTToken;
import com.example.demo.services.TTTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/REST/")
public class TTTRestController {
    private TTTService service;
    private boolean debug = false;
    @Autowired
    public TTTRestController(TTTService service, TTTConfig config){
        this.service = service;
        debug = config.getDebugMode();
    }

    @PostMapping("createToken")
    public ResponseEntity<TTTToken> post_createToken(){
        TTTToken token = service.createToken();
        if(token != null){
            System.out.println("returning token");
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @PutMapping("joinQueue")
    public ResponseEntity put_joinQueue(@RequestBody TTTQueue queue){
        System.out.println("put_joinQueue");
        TTTQueue q = service.joinQueue(queue);
        if(q != null) System.out.println("Queue id: " + q.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("leaveQueue")
    public ResponseEntity delete_leaveQueue(@RequestBody TTTToken token){
        service.leaveQueue(token.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("findGame/{token}")
    public ResponseEntity<TTTGame> get_findGame(@PathVariable(value="token") String token){
        System.out.println("Find game");
        TTTGame game = service.findGame(token);
        if(game != null) {
            System.out.println("Found game, returning to player!");
            return new ResponseEntity<>(game, HttpStatus.OK);
        }
        System.out.println("No game found!");
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }


    @GetMapping("getGameData/{gameid}/{token}")
    public ResponseEntity<TTTGame> get_getGameData(@PathVariable(value = "gameid") int gameid, @PathVariable(value = "token") String token){
        //if(debug) System.out.println("get_getGameData" + " " + gameid + " " + token);

        TTTGame game = service.getGameData(gameid, token);
        if(game != null){
            return new ResponseEntity<>(game, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @PostMapping(value = "saveMove", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<TTTGame> post_saveMove(TTTMove move){
        if(debug) System.out.println("saveMove");
        if(service.saveMove(move) != null){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @PostMapping(value = "surrenderGame")
    public ResponseEntity<TTTGame> post_surrenderGame(@RequestBody TTTToken token){
        System.out.println("surrenderGame" + token.getToken());
        service.surrenderGame(token.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
