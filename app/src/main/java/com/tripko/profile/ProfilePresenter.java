package com.tripko.profile;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tripko.app.App;
import com.tripko.app.Constants;
import com.tripko.model.data.User;
import com.tripko.model.response.LoginResponse;

import java.io.File;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mark Jansen Calderon on 1/12/2017.
 */

public class ProfilePresenter extends MvpNullObjectBasePresenter<ProfileView> {

    private static final String TAG = ProfilePresenter.class.getSimpleName();
    private Realm realm;
    private User user;
    //private RealmResults<Event> eventRealmResults;

    public void onStart() {
        realm = Realm.getDefaultInstance();
        user = App.getUser();
    }


    /* public void updateUserWithImage(File image, String userId, String firstName, String lastName, String contact, String birthday, String address) {
         if (firstName.equals("") || lastName.equals("") || birthday.equals("") || contact.equals("") || address.equals("")) {
             getView().showAlert("Fill-up all fields");
         } else {
             getView().startLoading();
             RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image);
             MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", image.getName(), requestFile);
             App.getInstance().getApiInterface().updateUserWithImage(body,
                     createPartFromString(userId), createPartFromString(firstName), createPartFromString(lastName), createPartFromString(contact)
                     , createPartFromString(birthday), createPartFromString(address))
                     .enqueue(new Callback<User>() {
                         @Override
                         public void onResponse(Call<User> call, final Response<User> response) {
                             getView().stopLoading();
                             if (response.isSuccessful() && response.body().getUserId() == user.getUserId()) {
                                 realm.executeTransaction(new Realm.Transaction() {
                                     @Override
                                     public void execute(Realm realm) {
                                         realm.copyToRealmOrUpdate(response.body());
                                         getView().finishAct();
                                     }
                                 });
                             } else {
                                 getView().showAlert("Oops something went wrong");
                             }
                         }

                         @Override
                         public void onFailure(Call<User> call, Throwable t) {
                             Log.e(TAG, "onFailure: Error calling login api", t);
                             getView().stopLoading();
                             getView().showAlert("Error Connecting to Server");
                         }
                     });
         }
     }
 */
    public void updateUser(String firstName, String lastName, String contact, String birthday, String address) {
        if (firstName.equals("") || lastName.equals("") || birthday.equals("") || contact.equals("") || address.equals("")) {
            getView().showAlert("Fill-up all fields");
        } else {
            getView().startLoading();
            App.getInstance().getApiInterface().updateProfile(Constants.BEARER + user.getApiToken(), App.getUser().getEmail(), firstName, "M", lastName, birthday, "M", address, contact, Constants.APPJSON)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<LoginResponse> call, @NonNull final Response<LoginResponse> response) {
                            getView().stopLoading();
                            if (response.isSuccessful() && response.body().isSuccess()) {
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.copyToRealmOrUpdate(response.body().getUser());
                                        getView().finishAct();
                                    }
                                });
                            } else {
                                getView().showAlert("Oops something went wrong");
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.e(TAG, "onFailure: Error calling login api", t);
                            getView().stopLoading();
                            getView().showAlert("Error Connecting to Server");
                        }
                    });
        }
    }

    ;

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse("multipart/form-data"), descriptionString);
    }

    public void onStop() {
        realm.close();
    }

    public void updateImage(File userImage) {
        getView().startLoading();
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), userImage);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image"
                , userImage.getName(), requestFile);
        App.getInstance().getApiInterface().updateImage(Constants.BEARER + App.getUser().getApiToken(), body, Constants.APPJSON).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                getView().stopLoading();
                if (response.isSuccessful())
                    if (response.body().isSuccess()) {
                        getView().showAlert(response.body().getMessage());
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(response.body().getUser());
                                getView().updateImage();
                            }
                        });
                    } else {
                        getView().showAlert(response.message());
                    }
                else {
                    getView().showAlert("Oops something went wrong");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().stopLoading();
                getView().showAlert("Error Connecting to Server");
            }
        });
    }

    public void changePassword(String currPass, String newPass, String confirmNewPass) {
        if (confirmNewPass.length() < 6) {
            getView().showAlert("Password must be atleast 6 characters");
        } else if (newPass.equals(confirmNewPass)) {
            getView().startLoading();
            App.getInstance().getApiInterface().changePassword(Constants.BEARER + App.getUser().getApiToken(), newPass, Constants.APPJSON).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    getView().stopLoading();
                    if (response.isSuccessful()) {
                        if (response.body().isSuccess()) {
                            getView().onPasswordChanged();
                        } else {
                            getView().showAlert(Constants.OOPS);
                        }
                    } else {
                        getView().showAlert(response.message() != null ? response.message()
                                : "Unknown Error");
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    getView().stopLoading();
                    Log.e(TAG, "onFailure: Error calling login api", t);
                    getView().stopLoading();
                    getView().showAlert("Error Connecting to Server");
                }
            });
        } else {
            getView().showAlert("New Passwords Mismatch");
        }
    }
}
