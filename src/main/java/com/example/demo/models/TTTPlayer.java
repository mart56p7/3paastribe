package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@JsonIgnoreProperties({ "token" }) //We dont want to see token data send to the client, its a secret!
@Entity(name = "TTTPlayer")
@Table(name = "tttplayers")
public class TTTPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id = -1;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private TTTToken token = null;
    @Column(name="playername", columnDefinition="VARCHAR(50)", unique = false)
    private String playername = null;

    public TTTPlayer(){}

    public TTTPlayer(TTTToken token, String playername){
        this.token = token;
        this.playername = playername;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TTTToken getToken() {
        return token;
    }

    public void setToken(TTTToken token) {
        this.token = token;
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }
}
