package com.bapoto.vtc.model;

import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;

public class Reservation {

     private String name;
     private String telephone;
     @Nullable
     private String email;
     private String pickUp;
     private String dropOff;
     private String hour;
     private String date;
     @Nullable
     private int price;
     private Timestamp dayAccepted;
     private Timestamp dayFinished;
     @Nullable
     private String infos;
     private User sender;


    public Reservation() {
    }


    public Reservation(String name, String telephone, @Nullable String email, Timestamp dayAccepted,Timestamp dayFinished, String pickUp, String dropOff,
                       String hour,@Nullable int price, String date, @Nullable String infos) {
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.pickUp = pickUp;
        this.dropOff = dropOff;
        this.hour = hour;
        this.date = date;
        this.dayAccepted = dayAccepted;
        this.dayFinished = dayFinished;
        this.price = price;
        this.infos = infos;

    }

    public Reservation(String nom, String tel, String desti, String rdv, String date, String hour, String infos, User sender, Boolean isAccepted) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
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

    public String getDropOff() {
        return dropOff;
    }

    public void setDropOff(String dropOff) {
        this.dropOff = dropOff;
    }

    public Timestamp getDayAccepted() {
        return dayAccepted;
    }

    public void setDayAccepted(Timestamp dayAccepted) {
        this.dayAccepted = dayAccepted;
    }

    public Timestamp getDayFinished() {
        return dayFinished;
    }

    @Nullable
    public int getPrice() {
        return price;
    }

    public void setPrice(@Nullable int price) {
        this.price = price;
    }

    public void setDayFinished(Timestamp dayFinished) {
        this.dayFinished = dayFinished;
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
