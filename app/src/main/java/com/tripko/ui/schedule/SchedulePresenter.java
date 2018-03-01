package com.tripko.ui.schedule;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tripko.model.data.Schedule;

import io.realm.Realm;

/**
 * Created by Jansen on 2/4/2018.
 */

class SchedulePresenter extends MvpNullObjectBasePresenter<ScheduleView> {
    private Realm realm;
    public void onStart() {
        realm = Realm.getDefaultInstance();
    }

    public void onStop() {
        realm.close();
    }

    public void getSchedule(int id) {
        Schedule schedule = realm.where(Schedule.class).equalTo("scheduleId", id).findFirst();
        getView().setSchedule(realm.copyFromRealm(schedule));
    }
}
