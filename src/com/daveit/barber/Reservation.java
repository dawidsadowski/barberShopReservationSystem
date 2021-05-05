package com.daveit.barber;

import java.io.Serializable;

public class Reservation implements Serializable {
    private final String date;
    private final String hour;
    private final String name;
    private String status;
    private boolean free = true;

    public Reservation(String date, String hour, boolean free, String name) {
        this.date = date;
        this.hour = hour;
        this.free = free;
        this.name = name;

        if(free) {
            status = "Wolne";
        } else {
            status = "Zarezerwowane";
        }
    }

    public String getDate() { return date; }

    public String getHour() {
        return hour;
    }

    public String getStatus() {
        return status;
    }

    public String getName() { return name; }

    public String getKey() { return date + " " + hour; }

    public boolean isFree() {
        return free;
    }

    public boolean equals(Reservation r) {
        System.out.println(this.name + " : " + r.name);
        System.out.println(this.getKey() + " : " + r.getKey());
        return (this.name.equals(r.name) && this.getKey().equals(r.getKey()));
    }

    public void setFree(boolean free) {
        this.free = free;
    }
}
