package com.tripko.ui.trips;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tripko.model.data.Reservation;

import java.util.List;

/**
 * Created by Jansen on 2/4/2018.
 */

public interface TripsView extends MvpView{
    void onItemClicked(Reservation item);

    void startLoading();

    void stopLoading();

    void showAlert(String s);

    void setList(List<Reservation> reservations);
}
