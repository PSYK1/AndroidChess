package com.example.chess.chessBoard;

public class Replay {

    private String name;
    private String date;

    private String[] moves;

    public Replay(String name, String date, String[] moves) {
        this.name = name;
        this.date = date;
        this.moves = moves;
    }

    public Replay(String[] data) {
        name = data[0];
        date = data[1];
        moves = new String[data.length - 2];
        for (int i = 0; i < moves.length; i++) {
            moves[i] = data[i+2];
        }
    }

    public String toString() {
        return name + " (" + date + ")";
    }

    public String toCSV() {
        String ret = "";
        ret += name + ",";
        ret += date + ",";
        for (String move: moves) {
            ret += move +",";
        }
        return ret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String[] getMoves() {
        return moves;
    }

    public void setMoves(String[] moves) {
        this.moves = moves;
    }
}
