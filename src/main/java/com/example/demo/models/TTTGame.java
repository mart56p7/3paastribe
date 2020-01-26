package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains game data for TTT
 * */
@Entity(name = "TTTGame")
@Table(name = "tttgames")
public class TTTGame {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    //Each field has a value, so they add up to a magic square.
    //By looking at where the pieces are place we can then add up field values and get the magic square value
    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    private List<TTTField> fields = new ArrayList();
    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "circleplayer_id")
    private TTTPlayer circleplayer = null;
    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "crossplayer_id")
    private TTTPlayer crossplayer = null;
    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "turnplayer_id")
    private TTTPlayer playerturn = null;
    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "winnerplayer_id")
    private TTTToken winner = null;
    @Transient // Field ignored
    @JsonProperty("myturn")
    private boolean myturn = false;
    @Transient // Field ignored
    @JsonProperty("myid")
    private int myid = 0;

    public TTTGame(){
    }

    public TTTGame(TTTPlayer crossplayer, TTTPlayer circleplayer){
        this.crossplayer = crossplayer;
        this.circleplayer = circleplayer;
        this.playerturn = crossplayer;

        //Init game fields... In total there are 9 + 3 + 3 = 15
        for(int i = 0; i < 15; i++){
            fields.add(new TTTField(i+1));
        }
        fields.get(9).setPiece(Piece.CIRCLE);
        fields.get(10).setPiece(Piece.CIRCLE);
        fields.get(11).setPiece(Piece.CIRCLE);
        fields.get(12).setPiece(Piece.CROSS);
        fields.get(13).setPiece(Piece.CROSS);
        fields.get(14).setPiece(Piece.CROSS);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<TTTField> getFields() {
        return fields;
    }

    public void setFields(List<TTTField> fields) {
        this.fields = fields;
    }

    public TTTPlayer getCircleplayer() {
        return circleplayer;
    }

    public void setCircleplayer(TTTPlayer circleplayer) {
        this.circleplayer = circleplayer;
    }


    public TTTPlayer getCrossplayer() {
        return crossplayer;
    }

    public void setCrossplayer(TTTPlayer crossplayer) {
        this.crossplayer = crossplayer;
    }

    public TTTPlayer getPlayerturn() {
        return playerturn;
    }

    public void setPlayerturn(TTTPlayer playerturn) {
        this.playerturn = playerturn;
    }

    public TTTToken getWinner() {
        return winner;
    }

    public void setWinner(TTTToken winner) {
        this.winner = winner;
    }

    public int identifyPlayer(String token){
        this.myturn = token.equals(playerturn.getToken().getToken());
        if(getCircleplayer().getToken().getToken().equals(token)){
            myid = getCircleplayer().getId();
        }
        if(getCrossplayer().getToken().getToken().equals(token)){
            myid = getCrossplayer().getId();
        }
        return this.myid;
    }
}
