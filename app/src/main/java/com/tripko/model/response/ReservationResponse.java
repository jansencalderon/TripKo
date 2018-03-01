package com.tripko.model.response;

import com.google.gson.annotations.SerializedName;
import com.tripko.model.data.Reservation;
import com.tripko.model.data.User;

/**
 * Created by Mark Jansen Calderon on 1/10/2017.
 */

public class ReservationResponse extends BasicResponse{

    @SerializedName("data")
    private Reservation reservation;

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
