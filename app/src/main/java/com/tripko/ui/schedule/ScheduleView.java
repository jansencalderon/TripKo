package com.tripko.ui.schedule;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tripko.model.data.Schedule;

/**
 * Created by Jansen on 2/4/2018.
 */

public interface ScheduleView extends MvpView{

    void startLoading();

    void stopLoading();

    void showAlert(String s);

    void setSchedule(Schedule schedule);
}

