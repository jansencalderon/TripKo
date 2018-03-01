package com.tripko.ui.schedule.seat;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tripko.model.data.Schedule;
import com.tripko.model.temp_data.TempReservation;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by Jansen on 2/8/2018.
 */

class SeatSelectionPresenter extends MvpNullObjectBasePresenter<SeatSelectionView> {

    private String TAG = SeatSelectionPresenter.class.getSimpleName();
    private Realm realm;

    public void onStart() {
        realm = Realm.getDefaultInstance();
    }

    public void onStop() {
        realm.close();
        realm.removeAllChangeListeners();
    }

    void saveData(final ArrayList<TextView> seats, final int scheduleId) {
        String seatsReserved = "";
        for (int i = 0; i < seats.size(); i++) {
            if (i != 0) {
                seatsReserved = seatsReserved + ", " + seats.get(i).getText().toString();
            } else {
                seatsReserved = seats.get(i).getText().toString();
            }
        }
        final String finalSeatsReserved = seatsReserved;
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.delete(TempReservation.class);
                TempReservation tempReservation = new TempReservation();
                tempReservation.setId(1);
                tempReservation.setScheduleId(scheduleId);
                tempReservation.setQty(seats.size());
                tempReservation.setSeatsReserved(finalSeatsReserved);
                realm.copyToRealm(tempReservation);
            }
        });
    }

    public TempReservation tempReservation() {
        TempReservation tempReservation;
        tempReservation = realm.where(TempReservation.class).findFirst();
        if (tempReservation != null) {
            return realm.copyFromRealm(tempReservation);
        } else {
            return tempReservation;
        }
    }

    public Schedule getSchedule(int scheduleId) {
        return realm.where(Schedule.class).equalTo("scheduleId",scheduleId).findFirst();
    }
}
