package com.tripko.ui.trips.detail;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Jansen on 2/25/2018.
 */

public interface TripDetailView extends MvpView{

    void startLoading();

    void stopLoading();

    void showAlert(String s);

    void updateReservation();

    void dismissDialog();

    void reservationCancelled();
}
