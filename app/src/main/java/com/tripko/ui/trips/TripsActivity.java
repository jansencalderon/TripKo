package com.tripko.ui.trips;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tripko.R;
import com.tripko.app.Constants;
import com.tripko.databinding.ActivityTripsBinding;
import com.tripko.model.data.Reservation;
import com.tripko.ui.trips.detail.TripDetailActivity;

import java.util.List;

public class TripsActivity extends MvpActivity<TripsView, TripsPresenter> implements TripsView {

    ActivityTripsBinding binding;
    TripsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trips);
        presenter.onStart();
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getTrips();
            }
        });


        adapter = new TripsAdapter(getMvpView());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onItemClicked(Reservation item) {
        Intent intent = new Intent(this, TripDetailActivity.class);
        intent.putExtra(Constants.ID, item.getReferenceNo());
        intent.putExtra("fromMain", true);
        startActivity(intent);
    }

    @Override
    public void startLoading() {
        binding.swipeRefreshLayout.setRefreshing(true);

    }


    @Override
    public void stopLoading() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showAlert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setList(List<Reservation> reservations) {
        adapter.setList(reservations);
        if (reservations.size() <= 0) {
            binding.result.noResultLayout.setVisibility(View.VISIBLE);
            binding.result.resultText.setText("No Trips Yet");
            binding.recyclerView.setVisibility(View.GONE);
        } else {
            binding.result.noResultLayout.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
    }

    @NonNull
    @Override
    public TripsPresenter createPresenter() {
        return new TripsPresenter();
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
