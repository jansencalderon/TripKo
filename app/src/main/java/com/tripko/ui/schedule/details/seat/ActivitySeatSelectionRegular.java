package com.tripko.ui.schedule.details.seat;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tripko.R;
import com.tripko.app.Constants;
import com.tripko.databinding.ActivitySeatSelectionRegularBinding;
import com.tripko.model.data.Schedule;
import com.tripko.model.temp_data.TempReservation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivitySeatSelectionRegular extends MvpActivity<SeatSelectionView, SeatSelectionPresenter> implements SeatSelectionView, View.OnClickListener {

    ActivitySeatSelectionRegularBinding binding;
    private String TAG = ActivitySeatSelectionRegular.class.getSimpleName();
    private ArrayList<TextView> textViewArrayList = new ArrayList<>();
    private ArrayList<TextView> seats = new ArrayList<>();
    private int scheduleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.onStart();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_seat_selection_regular);
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        scheduleId = getIntent().getIntExtra(Constants.ID, -1);
        Schedule schedule = presenter.getSchedule(scheduleId);

        Log.e(TAG, schedule.getSeats_taken() == null ? "" : schedule.getSeats_taken());
        TextView[] textViews = {
                binding.s1A,
                binding.s1B,
                binding.s1C,
                binding.s1D,
                binding.s2A,
                binding.s2B,
                binding.s2C,
                binding.s2D,
                binding.s3A,
                binding.s3B,
                binding.s3C,
                binding.s3D,
                binding.s4A,
                binding.s4B,
                binding.s4C,
                binding.s4D,
                binding.s5A,
                binding.s5B,
                binding.s5C,
                binding.s5D,
                binding.s6A,
                binding.s6B,
                binding.s6C,
                binding.s6D,
                binding.s7A,
                binding.s7B,
                binding.s7C,
                binding.s7D, binding.s8A,
                binding.s8B,
                binding.s8C,
                binding.s8D, binding.s9A,
                binding.s9B,
                binding.s9C,
                binding.s9D, binding.s10A,
                binding.s10B,
                binding.s10C,
                binding.s10D, binding.s11A,
                binding.s11B,
                binding.s11C,
                binding.s11M,
                binding.s11D, binding.s12A,
                binding.s12B,
                binding.s12C,
                binding.s12D, binding.s13A,
                binding.s13B,
                binding.s13C,
                binding.s13D, binding.s14A,
                binding.s14B,
                binding.s14C,
                binding.s14D, binding.s15A,
                binding.s15B,
                binding.s15C,
                binding.s15D
        };


        textViewArrayList.addAll(Arrays.asList(textViews));
        for (int i = 0; i < textViewArrayList.size(); i++) {
            textViewArrayList.get(i).setOnClickListener(this);
        }
        int data = getIntent().getIntExtra(Constants.DATA, -1);
        if (data == 0) {
            TempReservation tempReservation = presenter.tempReservation();
            List<String> myList = new ArrayList<>(Arrays.asList(tempReservation.getSeatsReserved().split(",")));
            for (int i = 0; i < myList.size(); i++) {
                Log.d(TAG, myList.get(i).trim());
                TextView textView = null;
                for (int j = 0; j < textViewArrayList.size(); j++) {
                    if (textViewArrayList.get(j).getText().toString().equals(myList.get(i).trim()))
                        textView = textViewArrayList.get(j);
                }
                if (textView != null) {
                    if (seats.contains(textView)) {
                        //showAlert("Removed " + textView.getText());
                        textView.setBackgroundColor(ContextCompat.getColor(ActivitySeatSelectionRegular.this, R.color.colorPrimary));
                        seats.remove(textView);
                    } else {
                        //oast.makeText(this, textView.getText() + " selected", Toast.LENGTH_SHORT).show();
                        textView.setBackgroundColor(ContextCompat.getColor(ActivitySeatSelectionRegular.this, R.color.greenSuccess));
                        seats.add(textView);
                    }
                    binding.seatSelected.setText(seats.size() + " seats selected");
                }
            }
        }


        if (schedule.getSeats_taken() != null) {
            List<String> myList2 = new ArrayList<>(Arrays.asList(schedule.getSeats_taken().split(",")));
            for (int i = 0; i < myList2.size(); i++) {
                Log.d(TAG, myList2.get(i).trim());
                TextView textView = null;
                for (int j = 0; j < textViewArrayList.size(); j++) {
                    if (textViewArrayList.get(j).getText().toString().equals(myList2.get(i).trim()))
                        textView = textViewArrayList.get(j);
                }
                if (textView != null) {
                    textView.setBackgroundColor(ContextCompat.getColor(ActivitySeatSelectionRegular.this, R.color.redFailed));
                    textView.setClickable(false);
                }
            }
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
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
    }

    @NonNull
    @Override
    public SeatSelectionPresenter createPresenter() {
        return new SeatSelectionPresenter();
    }

    @Override
    public void onClick(View view) {
        TextView textView = null;

        for (int i = 0; i < textViewArrayList.size(); i++) {
            if (textViewArrayList.get(i).getId() == view.getId())
                textView = textViewArrayList.get(i);
        }
        if (textView != null) {
            if (seats.contains(textView)) {
                //showAlert("Removed " + textView.getText());
                textView.setBackgroundColor(ContextCompat.getColor(ActivitySeatSelectionRegular.this, R.color.colorPrimary));
                seats.remove(textView);
            } else {
                if (seats.size() == 3) {
                    showAlert("You can only reserve up to 3 slots");
                    return;
                }
                //oast.makeText(this, textView.getText() + " selected", Toast.LENGTH_SHORT).show();
                textView.setBackgroundColor(ContextCompat.getColor(ActivitySeatSelectionRegular.this, R.color.greenSuccess));
                seats.add(textView);
            }
            binding.seatSelected.setText(seats.size() + " seats selected");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_done:
                if (seats.size() > 0) {
                    presenter.saveData(seats, scheduleId);
                    finish();
                } else {
                    showAlert("Please select at least one seat");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
