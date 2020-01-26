package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

/**
 * The TTTToken is a player specific token, only 1 token can exist at a time
 * */
@Entity(name = "TTTToken")
@Table(name = "ttttokens")
public class TTTToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name="token", columnDefinition="VARCHAR(36)", unique = true)
    private String token=null;
    @Column(name="expiration")
    private long expiration=-1;

    public TTTToken(){

    }

    public TTTToken(String token){
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
}
