package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity(name = "TTTQueue")
@Table(name = "tttqueue")
public class TTTQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id = -1;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "ttttoken_id")
    private TTTToken token = null;
    @Column(name="playername")
    private String playername = null;

    public TTTQueue(){

    }

    public TTTQueue(TTTToken token, String playername){
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
