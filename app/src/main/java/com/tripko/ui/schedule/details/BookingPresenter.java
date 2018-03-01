package com.tripko.ui.schedule.details;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tripko.app.App;
import com.tripko.app.Constants;
import com.tripko.model.data.Schedule;
import com.tripko.model.response.BasicResponse;
import com.tripko.model.temp_data.TempReservation;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jansen on 2/8/2018.
 */

class BookingPresenter extends MvpNullObjectBasePresenter<BookingView> {
    private Realm realm;
    private String TAG = BookingPresenter.class.getSimpleName();

    public void onStart() {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(TempReservation.class);
            }
        });
/*

        tempReservation = realm.where(TempReservation.class).findFirstAsync();
        tempReservation.addChangeListener(new RealmChangeListener<TempReservation>() {
            @Override
            public void onChange(TempReservation tempReservation) {
                getView().setTempReservation(realm.copyFromRealm(tempReservation));
            }
        });
*/

    }


    public void onStop() {
        realm.removeAllChangeListeners();
        realm.close();
    }

    public Schedule getSchedule(int id) {
        return realm.copyFromRealm(realm.where(Schedule.class).equalTo("scheduleId", id).findFirst());
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

    public void onReserve(int scheduleId, String actualSeats, int numberOfSeats, String modeOfPayment) {
        getView().startLoading();
        App.getInstance().getApiInterface().addReservation(
                Constants.BEARER + App.getUser().getApiToken(),
                scheduleId,
                actualSeats,
                numberOfSeats,
                modeOfPayment,
                Constants.APPJSON).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                getView().stopLoading();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        getView().reservationSuccess();
                    } else {
                        getView().showAlert(response.body().getMessage());
                    }
                } else {
                    getView().showAlert(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().stopLoading();
                getView().showAlert("Error Connecting to Server");

            }
        });
    }

}