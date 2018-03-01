package com.tripko.model.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Jansen on 2/27/2018.
 */

public class Notification extends RealmObject{

    public static final String NOTIF_ID = "notif_id";
    public static final String USER_ID = "user_id";
    public static final String SCHEDULE_ID = "schedule_id";
    public static final String MESSAGE = "message";
    public static final String USER = "user";
    public static final String SCHEDULE = "schedule";

    @SerializedName("notif_id")
    private int notif_id;
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("schedule_id")
    private int schedule_id;
    @SerializedName("message")
    private String message;
    @SerializedName("user")
    private User user;
    @SerializedName("schedule")
    private Schedule schedule;

    public Notification setNotif_id(int notif_id) {
        this.notif_id = notif_id;
        return this;
    }

    public int getNotif_id() {
        return this.notif_id;
    }

    public Notification setUser_id(int user_id) {
        this.user_id = user_id;
        return this;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public Notification setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
        return this;
    }

    public int getSchedule_id() {
        return this.schedule_id;
    }

    public Notification setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

}
