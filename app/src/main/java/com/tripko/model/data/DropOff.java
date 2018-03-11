package com.tripko.model.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jansen on 3/11/2018.
 */

public class DropOff extends RealmObject {
    public static final String DROP_OFF_ID = "drop_off_id";
    public static final String SCHEDULE_ID = "schedule_id";
    public static final String NAME = "name";
    public static final String FARE = "fare";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";

    @PrimaryKey
    @SerializedName("drop_off_id")
    private int dropOffId;
    @SerializedName("schedule_id")
    private String scheduleId;
    @SerializedName("name")
    private String name;
    @SerializedName("fare")
    private String fare;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;

    @Override
    public String toString() {
        return this.name+ " - PHP "+this.fare;
    }
    public int getDropOffId() {
        return dropOffId;
    }

    public void setDropOffId(int dropOffId) {
        this.dropOffId = dropOffId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
