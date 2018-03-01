package com.tripko.ui.trips.detail;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tripko.app.App;
import com.tripko.app.Constants;
import com.tripko.model.data.Reservation;
import com.tripko.model.response.BasicResponse;
import com.tripko.model.response.ReservationResponse;

import java.io.File;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jansen on 2/25/2018.
 */

class TripDetailPresenter extends MvpNullObjectBasePresenter<TripDetailView> {

    Realm realm;
    String TAG = TripDetailPresenter.class.getSimpleName();

    public void onStart() {
        realm = Realm.getDefaultInstance();
    }

    public void onStop() {
        realm.close();
    }

    public Reservation getReservation(int id) {
        Reservation reservation = realm.where(Reservation.class).equalTo("referenceNo", id).findFirst();
        return realm.copyFromRealm(reservation);
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse("multipart/form-data"), descriptionString);
    }

    public void updateImage(File userImage, Integer referenceNo) {
        getView().startLoading();
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), userImage);
        MultipartBody.Part body = MultipartBody.Part.createFormData("deposit_slip_image"
                , userImage.getName(), requestFile);
        App.getInstance().getApiInterface().uploadDepositSlip(Constants.BEARER + App.getUser().getApiToken(),
                body, createPartFromString(referenceNo+""), Constants.APPJSON).enqueue(new Callback<ReservationResponse>() {
            @Override
            public void onResponse(Call<ReservationResponse> call, final Response<ReservationResponse> response) {
                getView().stopLoading();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                getView().dismissDialog();
                                realm.copyToRealmOrUpdate(response.body().getReservation());
                                getView().updateReservation();
                                getView().showAlert(response.body().getMessage());
                            }
                        });
                    } else {
                        getView().dismissDialog();
                        getView().showAlert(response.body().getMessage());
                    }

                }
            }

            @Override
            public void onFailure(Call<ReservationResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().stopLoading();
                getView().showAlert("Error Connecting to Server");
            }
        });
    }

    void cancelReservation(int referenceNo){
        getView().startLoading();
        App.getInstance().getApiInterface().cancelReservation(Constants.BEARER + App.getUser().getApiToken(),
                referenceNo, Constants.APPJSON).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                getView().stopLoading();
                if (response.isSuccessful()) {
                    try {
                        if (response.body().isSuccess()) {
                            getView().reservationCancelled();
                        } else {
                            getView().showAlert(response.message());
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        getView().showAlert("Oops");
                    }
                } else {
                    getView().showAlert(response.message() != null ? response.message()
                            : "Unknown Error");
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
