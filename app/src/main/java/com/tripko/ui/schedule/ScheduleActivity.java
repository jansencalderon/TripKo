package com.tripko.ui.schedule;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tripko.R;
import com.tripko.app.App;
import com.tripko.app.Constants;
import com.tripko.databinding.ActivityScheduleBinding;
import com.tripko.model.data.Schedule;
import com.tripko.ui.adapters.SeatsAdapter;
import com.tripko.ui.schedule.details.BookingActivity;
import com.tripko.ui.tripStatus.TripStatusActivity;

import java.util.Arrays;

public class ScheduleActivity extends MvpActivity<ScheduleView, SchedulePresenter> implements ScheduleView {

    ActivityScheduleBinding binding;
    Schedule schedule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule);
        presenter.onStart();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getIntExtra(Constants.ID, -1);
        presenter.getSchedule(id);
        binding.setView(getMvpView());

        binding.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScheduleActivity.this, BookingActivity.class);
                intent.putExtra(Constants.ID, schedule.getScheduleId());
                intent.putExtra("fromMain", true);
                startActivity(intent);
            }
        });

        binding.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScheduleActivity.this, TripStatusActivity.class);
                intent.putExtra(Constants.ID, schedule.getScheduleId());
                intent.putExtra("fromMain", true);
                startActivity(intent);
            }
        });

        if (App.isPassenger()) {
            binding.book.setVisibility(View.VISIBLE);
            binding.status.setVisibility(View.GONE);
        } else {
            binding.book.setVisibility(View.GONE);
            binding.status.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {
    }

    @Override
    public void showAlert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
        binding.setSchedule(schedule);
        SeatsAdapter seatsAdapter = new SeatsAdapter(Arrays.asList(schedule.getSeats_taken().split(",")));
        binding.recyclerView.setAdapter(seatsAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
    }

    @NonNull
    @Override
    public SchedulePresenter createPresenter() {
        return new SchedulePresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
