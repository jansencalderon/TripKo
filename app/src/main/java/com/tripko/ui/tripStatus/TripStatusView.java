package com.tripko.ui.tripStatus;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Jansen on 2/25/2018.
 */

public interface TripStatusView extends MvpView{
    void startLoading();

    void stopLoading();

    void showAlert(String s);

    void onSucess();
}
