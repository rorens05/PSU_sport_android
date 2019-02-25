package com.example.psusports.models;

public class Team {
    public String id;
    public String name;
    public String logo;
    public String sport_id;
    public String event_id;

    public Team() {
    }

    public Team(String id, String name, String logo, String sport_id, String event_id) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.sport_id = sport_id;
        this.event_id = event_id;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", sport_id='" + sport_id + '\'' +
                ", event_id='" + event_id + '\'' +
                '}';
    }
}
