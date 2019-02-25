package com.example.psusports.models;

public class Sport {
    public String id;
    public String game_type;
    public String logo;
    public String name;

    public Sport(){

    }

    public Sport(String id, String game_type, String logo, String name) {
        this.id = id;
        this.game_type = game_type;
        this.logo = logo;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Sport{" +
                "id='" + id + '\'' +
                ", game_type='" + game_type + '\'' +
                ", logo='" + logo + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
