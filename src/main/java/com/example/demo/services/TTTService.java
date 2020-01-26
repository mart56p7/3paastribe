package com.example.demo.services;

import com.example.demo.TTTConfig;
import com.example.demo.models.*;
import com.example.demo.repositories.TTTPlayerRepository;
import com.example.demo.repositories.TTTQueueRepository;
import com.example.demo.repositories.TTTTGameRepository;
import com.example.demo.repositories.TTTTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional //Required to delete from the database
public class TTTService {
    private TTTTGameRepository gamerepo;
    private TTTTokenRepository tokenrepo;
    private TTTQueueRepository queuerepo;
    private TTTPlayerRepository playerrepo;

    @Autowired
    public TTTService(TTTTGameRepository gamerepo, TTTTokenRepository tokenrepo, TTTQueueRepository queuerepo, TTTPlayerRepository playerrepo, TTTConfig config){
        this.gamerepo = gamerepo;
        this.tokenrepo = tokenrepo;
        this.queuerepo = queuerepo;
        this.playerrepo = playerrepo;
    }

    //Generates a random token, and saves it
    public TTTToken createToken(){
        System.out.println("createToken: start");
        boolean tokensaved = false;
        TTTToken token = null;
        while(!tokensaved) {
            //Generates random 32 bit string, https://docs.oracle.com/javase/1.5.0/docs/api/java/util/UUID.html
            String randomstr = java.util.UUID.randomUUID().toString();
            token = new TTTToken(randomstr);
            try {
                token = tokenrepo.save(token);
                tokensaved = true;
            } catch (javax.persistence.PersistenceException e) {
                System.out.println(e.getMessage());
            }
            if(tokenrepo.findByToken(randomstr) == null){
                System.out.println("Token not saved....");
            }
            else{
                System.out.println("saved token: " + randomstr);
            }
        }
        System.out.println("createToken: end");
        return token;
    }

    public TTTQueue joinQueue(TTTQueue queue){
        if (queue != null) {
            if(queue.getToken() != null && queue.getPlayername() != null){
                queue.setToken(tokenrepo.findByToken(queue.getToken().getToken()));
                if(gamerepo.findGame(queue.getToken().getToken()) == null){
                    return queuerepo.save(queue);
                }
                System.out.println("Token not found... this is bad");
            }
        }
        return null;
    }

    public void leaveQueue(String token){
        queuerepo.deleteByTokenTokenEquals(token);
    }

    public TTTGame findGame(String token){
        TTTGame game = gamerepo.findGame(token);
        if(game != null){
            game.identifyPlayer(token);
        }
        return game;
    }

    public TTTGame saveMove(TTTMove move){
        System.out.println("Move: " + move.getFrom() + " to " + move.getTo());
        TTTGame game = getGameData(move.getGameid(), move.getToken());
        if(game != null){
            System.out.println("Found game");
            //Check if its the players turn!
            if(game.getPlayerturn().getToken().getToken().equals(move.getToken())){
                System.out.println("Its player turn");
                //Find player Piece type
                TTTToken playertoken = game.getCircleplayer().getToken();
                Piece playerpiece = Piece.CIRCLE;
                TTTPlayer opponent = game.getCrossplayer();
                if(game.getCrossplayer().getToken().getToken().equals(move.getToken())){
                    playertoken = game.getCrossplayer().getToken();
                    playerpiece = Piece.CROSS;
                    opponent = game.getCircleplayer();
                }
                System.out.println("Player piece type" + playerpiece);
                //Check if the move is valid!
                List<TTTField> fields = game.getFields();
                boolean startphase = false;
                TTTField from = null, to = null;
                for(int i = 0; i < fields.size(); i++){
                    if(fields.get(i).getFieldnumber() == move.getFrom()){
                        from = fields.get(i);
                    }
                    if(fields.get(i).getFieldnumber() == move.getTo()){
                        to = fields.get(i);
                    }
                    //Check if we are in the start phase
                    if(fields.get(i).getFieldnumber() > 9){
                        startphase = fields.get(i).getPiece() != Piece.NONE || startphase;
                    }
                }
                //validating move
                boolean validmove = true;
                if(from != null && to != null){
                    System.out.println("from and to set");
                    if(startphase){
                        System.out.println("Its start phase");
                        System.out.println("valid move = " + validmove);
                        validmove = from.getPiece() == playerpiece && validmove;
                        System.out.println("valid move = " + validmove);
                        validmove = from.getFieldnumber() > 9 && validmove;
                        System.out.println("valid move = " + validmove);
                        validmove = from.getPiece() != Piece.NONE && validmove;
                        System.out.println("valid move = " + validmove);
                        validmove = to.getFieldnumber() < 10 && validmove;
                        System.out.println("valid move = " + validmove);
                        validmove = to.getPiece() == Piece.NONE && validmove;
                        System.out.println("valid move = " + validmove);
                    }
                    else{
                        System.out.println("Its midgame");
                        validmove = from.getPiece() == playerpiece && from.getFieldnumber() < 10 && from.getPiece() != Piece.NONE && to.getFieldnumber() < 10 && to.getPiece() == Piece.NONE;
                    }
                }
                if(validmove){ //If all is OK
                    System.out.println("Move is valid");
                    //Lets do the move
                    to.setPiece(from.getPiece());
                    from.setPiece(Piece.NONE);
                    //Lets change player turn
                    game.setPlayerturn(opponent);
                    //Check if we have a winner - Here we use our magic square, since we now know that the total sum must be 15
                    int winnervalue = 15;
                    for(int i = 0; i < fields.size(); i++){
                        if(fields.get(i).getPiece() == playerpiece){
                            winnervalue -= getMagicSquareValue(fields.get(i).getFieldnumber());
                        }
                    }
                    if(winnervalue == 0){
                        System.out.println("Winner found");
                        game.setWinner(playertoken);
                    }
                    //save the game
                    gamerepo.save(game);
                    return game;
                }
            }
        }
        System.out.println("Invalid move!");
        return null;
    }

    //https://en.wikipedia.org/wiki/Magic_square
    private int getMagicSquareValue(int fieldnumber){
        switch (fieldnumber){
            case 1: return 2;
            case 2: return 7;
            case 3: return 6;
            case 4: return 9;
            case 5: return 5;
            case 6: return 1;
            case 7: return 4;
            case 8: return 3;
            case 9: return 8;
            default: return -100;
        }
    }

    public TTTGame getGameData(int gameid, String token){
        TTTGame game = gamerepo.findGameByIdAndToken(gameid, token);
        if(game != null){
            game.identifyPlayer(token);
        }
        return game;
    }

    public TTTGame surrenderGame(String token){
        TTTGame game = gamerepo.findGame(token);
        if(game != null){
            //we have implicit verified token string by finding a game!
            TTTToken mytoken = new TTTToken(token);
            TTTToken opponent = findOpponent(game, mytoken);
            if(opponent != null){
                game.setWinner(opponent);
                gamerepo.save(game);
                System.out.println("Saved game");
            }
            return game;
        }
        return null;
    }

    private TTTToken findOpponent(TTTGame game, TTTToken token){
        if(token.getToken().equals(game.getCrossplayer().getToken().getToken()))
        {
            return game.getCircleplayer().getToken();
        }
        if(token.getToken().equals(game.getCircleplayer().getToken().getToken()))
        {
            return game.getCrossplayer().getToken();
        }
        return null;
    }
}
