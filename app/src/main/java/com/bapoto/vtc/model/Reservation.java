package com.bapoto.vtc.model;

import androidx.annotation.Nullable;

public class Reservation {

     private String name;
     private String telephone;
     @Nullable
     private String email;
     private String pickUp;
     private String destination;
     private String hour;
     private String date;
     @Nullable
     private String infos;

    public Reservation() {
    }

    public Reservation(String name, String telephone, @Nullable String email, String pickUp, String destination,
                       String hour, String date, @Nullable String infos) {
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.pickUp = pickUp;
        this.destination = destination;
        this.hour = hour;
        this.date = date;
        this.infos = infos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    public String getPickUp() {
        return pickUp;
    }

    public void setPickUp(String pickUp) {
        this.pickUp = pickUp;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Nullable
    public String getInfos() {
        return infos;
    }

    public void setInfos(@Nullable String infos) {
        this.infos = infos;
    }
}
