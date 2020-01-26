package com.example.demo.models;

import javax.persistence.*;

@Entity(name = "TTTField")
@Table(name= " tttfield")
public class TTTField {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id = -1;
    @Column
    private int fieldnumber = -1;
    @Column
    private Piece piece = Piece.NONE;

    public TTTField(){}

    public TTTField(int fieldnumber){
        this(fieldnumber, 0, Piece.NONE);
    }


    public TTTField(int fieldnumber, int fieldvalue, Piece piece){
        this.fieldnumber = fieldnumber;
        this.piece = piece;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFieldnumber() {
        return fieldnumber;
    }

    public void setFieldnumber(int fieldnumber) {
        this.fieldnumber = fieldnumber;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
