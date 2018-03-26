package com.tripko.scheduleList;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tripko.model.data.Schedule;

import java.util.List;


/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

public interface ScheduleListView extends MvpView {

    void stopLoading();

    void startLoading();

    void showAlert(String s);

    void refreshList();

    void setList(List<Schedule> schedules);

    void onItemClicked(Schedule item);

    void addFilterData(List<String> destFromItems, List<String> destToItems, List<String> companyItems);
}
