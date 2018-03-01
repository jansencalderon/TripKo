package com.tripko.ui.schedule.seat;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Jansen on 2/8/2018.
 */

public interface SeatSelectionView extends MvpView{

    void startLoading();

    void stopLoading();

    void showAlert(String s);
}
