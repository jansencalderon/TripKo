package com.tripko.ui.trips;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tripko.app.App;
import com.tripko.app.Constants;
import com.tripko.model.data.Reservation;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jansen on 2/4/2018.
 */

class TripsPresenter extends MvpNullObjectBasePresenter<TripsView> {
    private Realm realm;
    private String TAG = TripsPresenter.class.getSimpleName();
    private RealmResults<Reservation> realmResults;

    public void onStart() {
        realm = Realm.getDefaultInstance();
        getTrips();
        realmResults = realm.where(Reservation.class).findAllAsync().sort("referenceNo", Sort.DESCENDING);
        realmResults.addChangeListener(new RealmChangeListener<RealmResults<Reservation>>() {
            @Override
            public void onChange(@NonNull RealmResults<Reservation> reservations) {
                getView().setList(reservations);
            }
        });
    }

    void getTrips() {
        getView().startLoading();
        App.getInstance().getApiInterface().getReservations(Constants.BEARER + App.getUser().getApiToken(),
                Constants.APPJSON).enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(@NonNull Call<List<Reservation>> call, @NonNull final Response<List<Reservation>> response) {
                getView().stopLoading();
                final Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        realm.delete(Reservation.class);
                        realm.copyToRealmOrUpdate(response.body());
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        realm.close();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(@NonNull Throwable error) {
                        realm.close();
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<List<Reservation>> call, @NonNull Throwable t) {
                getView().stopLoading();
                getView().showAlert("Error Connecting to Server");

            }
        });
    }

    public void onStop() {
        if (realmResults.isValid()) {
            realmResults.removeAllChangeListeners();
        }
        realm.close();
    }
}
