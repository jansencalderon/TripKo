package com.tripko.model.temp_data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jansen on 2/22/2018.
 */

public class TempReservation extends RealmObject{

    @PrimaryKey
    private int id;
    private int scheduleId;
    private int qty;
    private String seatsReserved;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getSeatsReserved() {
        return seatsReserved;
    }

    public void setSeatsReserved(String seatsReserved) {
        this.seatsReserved = seatsReserved;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
