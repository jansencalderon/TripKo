package com.tripko.model.response;


import com.google.gson.annotations.SerializedName;
import com.tripko.model.data.Notification;

/**
 * Created by Mark Jansen Calderon on 1/10/2017.
 */

public class NotifResponse extends BasicResponse {

    @SerializedName("data")
    private Notification notification;


    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
