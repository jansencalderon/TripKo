package com.tripko.ui.register;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tripko.R;
import com.tripko.app.App;
import com.tripko.app.Constants;
import com.tripko.model.response.BasicResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

public class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView> {

    private static final String TAG = RegisterPresenter.class.getSimpleName();

    public void register(String email,
                         String password,
                         String confirmPassword,
                         String firstName,
                         String lastName,
                         String birthday,
                         String contact,
                         String address) {

        if (email.equals("") || password.equals("") || confirmPassword.equals("") || firstName.equals("") || lastName.equals("") || birthday.equals("") ||
                contact.equals("") || address.equals("")) {
            getView().showAlert("Fill-up all fields");
        } else if (!password.contentEquals(confirmPassword)) {
            getView().showAlert("Password does not match");
        } else if (password.length() > 5){
            getView().showAlert("Password must be minimum of 5 characters");
        }else {
            getView().startLoading();
            App.getInstance().getApiInterface().register(email, password, firstName, lastName, contact, birthday, address, "M", "Middle", Constants.APPJSON)
                    .enqueue(new Callback<BasicResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<BasicResponse> call, @NonNull Response<BasicResponse> response) {
                            getView().stopLoading();
                            if (response.isSuccessful()) {
                                if (response.body().isSuccess())
                                    getView().onRegistrationSuccess();
                                else
                                    getView().showAlert(String.valueOf(R.string.oops));
                            } else {
                                try {
                                    String errorBody = response.errorBody().string();
                                    getView().showAlert(errorBody);
                                } catch (IOException e) {
                                    Log.e(TAG, "onResponse: Error parsing error body as string", e);
                                    getView().showAlert(response.message() != null ?
                                            response.message() : "Unknown Exception");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BasicResponse> call, Throwable t) {
                            Log.e(TAG, "onFailure: Error calling register api", t);
                            getView().stopLoading();
                            getView().showAlert("Error Connecting to Server");
                        }
                    });
        }

    }


}
