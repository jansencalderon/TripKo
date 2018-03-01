package com.tripko.ui.tripStatus;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tripko.app.App;
import com.tripko.app.Constants;
import com.tripko.model.data.Notification;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jansen on 2/25/2018.
 */

class TripStatusPresenter extends MvpNullObjectBasePresenter<TripStatusView> {
    Realm realm;
    String TAG = TripStatusPresenter.class.getSimpleName();

    public void onStart() {
        realm = Realm.getDefaultInstance();
    }

    void sendNotification(Integer scheduleId, String message) {
        getView().startLoading();
        App.getInstance().getApiInterface().addNotif(Constants.BEARER + App.getUser().getApiToken(), scheduleId + "", message, Constants.APPJSON).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, final Response<List<Notification>> response) {
                if (response.isSuccessful()) {
                    getView().stopLoading();
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            realm.delete(Notification.class);
                            realm.copyToRealmOrUpdate(response.body());
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            realm.close();
                            getView().onSucess();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(@NonNull Throwable error) {
                            realm.close();
                        }
                    });
                } else {
                    getView().showAlert(response.message() != null ? response.message()
                            : "Unknown Error");

                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().stopLoading();
                getView().showAlert("Error Connecting to Server");
            }
        });


    }

    public void onStop() {
        realm.close();
    }
}
