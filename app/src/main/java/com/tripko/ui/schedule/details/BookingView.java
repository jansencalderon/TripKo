package com.tripko.ui.schedule.details;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tripko.model.temp_data.TempReservation;

/**
 * Created by Jansen on 2/8/2018.
 */

public interface BookingView extends MvpView {
    void showAlert(String s);

    void setTempReservation(TempReservation tempReservation);

    void startLoading();

    void stopLoading();

    void reservationSuccess();
}
