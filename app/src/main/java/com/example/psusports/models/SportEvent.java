package com.example.psusports.models;

public class SportEvent {
    public String id;
    public String name;
    public String event_date;
    public String venue;
    public String organizer;

    public SportEvent(){}

    @Override
    public String toString() {
        return "SportEvent{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", event_date='" + event_date + '\'' +
                ", venue='" + venue + '\'' +
                ", organizer='" + organizer + '\'' +
                '}';
    }
}
