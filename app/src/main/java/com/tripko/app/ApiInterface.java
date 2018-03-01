package com.tripko.app;


import com.tripko.model.data.Notification;
import com.tripko.model.data.Reservation;
import com.tripko.model.data.Schedule;
import com.tripko.model.response.BasicResponse;
import com.tripko.model.response.LoginResponse;
import com.tripko.model.response.ReservationResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @FormUrlEncoded
    @POST("user/login")
    Call<LoginResponse> login(@Field(Constants.EMAIL) String username,
                              @Field(Constants.PASSWORD) String password,
                              @Header(Constants.ACCEPT) String accept);

    @FormUrlEncoded
    @POST("user")
    Call<BasicResponse> register(@Field(Constants.EMAIL) String username,
                                 @Field(Constants.PASSWORD) String password,
                                 @Field(Constants.FIRST_NAME) String firstName,
                                 @Field(Constants.LAST_NAME) String lastName,
                                 @Field(Constants.CONTACT) String contact,
                                 @Field(Constants.BIRTHDAY) String birthday,
                                 @Field(Constants.ADDRESS) String address,
                                 @Field(Constants.GENDER) String gender,
                                 @Field(Constants.MIDDLE_NAME) String middleName,
                                 @Header(Constants.ACCEPT) String accept
    );


    @Multipart
    @POST("user/image")
    Call<LoginResponse> updateImage(@Header(Constants.AUTHORIZATION) String authorization,
                                    @Part MultipartBody.Part image,
                                    @Header(Constants.ACCEPT) String json);

    @FormUrlEncoded
    @POST("user/update")
    Call<LoginResponse> updateProfile(@Header(Constants.AUTHORIZATION) String authorization, @Field(Constants.EMAIL) String username,
                                      @Field(Constants.FIRST_NAME) String firstName,
                                      @Field(Constants.MIDDLE_NAME) String middleName,
                                      @Field(Constants.LAST_NAME) String lastName,
                                      @Field(Constants.BIRTHDAY) String birthday,
                                      @Field(Constants.GENDER) String gender,
                                      @Field(Constants.ADDRESS) String address,
                                      @Field(Constants.CONTACT) String contact,
                                      @Header(Constants.ACCEPT) String accept);


    @FormUrlEncoded
    @POST("user/update_pass")
    Call<LoginResponse> changePassword(@Header(Constants.AUTHORIZATION) String authorization,
                                       @Field(Constants.PASSWORD) String password,
                                       @Header(Constants.ACCEPT) String accept);

    @GET("reservation")
    Call<List<Reservation>> getReservations(@Header(Constants.AUTHORIZATION) String authorization,
                                            @Header(Constants.ACCEPT) String json);


    @Multipart
    @POST("reservation/deposit_slip")
    Call<ReservationResponse> uploadDepositSlip(@Header(Constants.AUTHORIZATION) String authorization,
                                                @Part MultipartBody.Part image,
                                                @Part("reference_no") RequestBody refNo,
                                                @Header(Constants.ACCEPT) String json);

    @FormUrlEncoded
    @POST("reservation")
    Call<BasicResponse> addReservation(@Header(Constants.AUTHORIZATION) String authorization,
                                       @Field("schedule_id") int scheduleId,
                                       @Field("seats_reserved") String seatsReserved,
                                       @Field("qty") int qty,
                                       @Field("mode_payment") String modeOfPayment,
                                       @Header(Constants.ACCEPT) String json);


    @FormUrlEncoded
    @POST("reservation/cancel/{reference_no}")
    Call<BasicResponse> cancelReservation(@Header(Constants.AUTHORIZATION) String authorization,
                                          @Path("reference_no") int scheduleId,
                                          @Header(Constants.ACCEPT) String json);

    @FormUrlEncoded
    @POST("notification")
    Call<List<Notification>> addNotif(@Header(Constants.AUTHORIZATION) String authorization,
                                      @Field("schedule_id") String schedId,
                                      @Field("message") String message,
                                      @Header(Constants.ACCEPT) String json);

    @GET("schedule")
    Call<List<Schedule>> getSchedules(@Header(Constants.AUTHORIZATION) String authorization,
                                      @Query("company_id") String companyId,
                                      @Query("date") String date,
                                      @Query("no_passenger") String noOfPassenger,
                                      @Query("destination_from") String destFrom,
                                      @Query("destination_to") String destTo,
                                      @Header(Constants.ACCEPT) String json);

    @GET("schedule/bus_assistant")
    Call<List<Schedule>> getSchedulesBusAssistant(@Header(Constants.AUTHORIZATION) String authorization,
                                      @Header(Constants.ACCEPT) String json);



    @POST("user/logout")
    Call<BasicResponse> logout(@Header(Constants.AUTHORIZATION) String authorization,
                               @Header(Constants.ACCEPT) String json);
}
