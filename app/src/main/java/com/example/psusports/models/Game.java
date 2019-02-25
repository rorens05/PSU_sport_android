package com.example.psusports.models;

public class Game {
    public String id;
    public String name;
    public String game_type;
    public String sport_id;
    public String schedule;
    public String event_id;
    public String status;
    public String c1_id;
    public String team1;
    public String score1;
    public String c2_id;
    public String team2;
    public String score2;

    public Game() {
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", game_type='" + game_type + '\'' +
                ", sport_id='" + sport_id + '\'' +
                ", schedule='" + schedule + '\'' +
                ", event_id='" + event_id + '\'' +
                ", status='" + status + '\'' +
                ", c1_id='" + c1_id + '\'' +
                ", team1='" + team1 + '\'' +
                ", score1='" + score1 + '\'' +
                ", c2_id='" + c2_id + '\'' +
                ", team2='" + team2 + '\'' +
                ", score2='" + score2 + '\'' +
                '}';
    }
}
