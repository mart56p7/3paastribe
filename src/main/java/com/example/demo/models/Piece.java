package com.example.demo.models;

public enum Piece {
    NONE(0), CROSS(1), CIRCLE(2);

    private int pvalue = 0;

    Piece(int pvalue){
        this.pvalue = pvalue;
    }

    public int getValue(){
        return pvalue;
    }
}
