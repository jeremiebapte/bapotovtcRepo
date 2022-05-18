package com.bapoto.vtc.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.time.LocalTime;
import java.util.Date;

public class Message {

    private Date dateCreated;
    private LocalTime hourCreated;
    private User userSender;

    private String message;
    private String urlImage;

    public Message() {
    }

    public Message(String message, User userSender) {
        this.message = message;
        this.userSender = userSender;
    }



    public Message(Date dateCreated, String message,String urlImage, LocalTime hourCreated, User userSender, String pickUp, String destination) {
        this.dateCreated = dateCreated;
        this.hourCreated = hourCreated;
        this.userSender = userSender;

        this.message = message;
        this.urlImage = urlImage;

    }

    public Message(String textMessage, String urlImage, User user) {
    }


    @ServerTimestamp
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalTime getHourCreated() {
        return hourCreated;
    }

    public void setHourCreated(LocalTime hourCreated) {
        this.hourCreated = hourCreated;
    }

    public User getUserSender() {
        return userSender;
    }

    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

}
