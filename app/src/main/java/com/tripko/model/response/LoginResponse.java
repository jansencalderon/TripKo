package com.tripko.model.response;


import com.google.gson.annotations.SerializedName;
import com.tripko.model.data.User;

/**
 * Created by Mark Jansen Calderon on 1/10/2017.
 */

public class LoginResponse extends BasicResponse {

    @SerializedName("data")
    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
