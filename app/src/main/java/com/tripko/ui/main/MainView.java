package com.tripko.ui.main;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tripko.model.data.Schedule;
import com.tripko.model.data.User;

import java.util.List;


/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

public interface MainView extends MvpView {

    void stopLoading();

    void startLoading();

    void displayUserData(User user);

    void showAlert(String s);

    void refreshList();

    void onLogout();

    void setList(List<Schedule> schedules);

    void onItemClicked(Schedule item);

    void addFilterData(List<String> destFromItems, List<String> destToItems, List<String> companyItems);
}
