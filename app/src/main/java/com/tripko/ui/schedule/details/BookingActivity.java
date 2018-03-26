package com.tripko.ui.schedule.details;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tripko.R;
import com.tripko.app.Constants;
import com.tripko.databinding.ActivityBookingBinding;
import com.tripko.databinding.DialogConfirmBinding;
import com.tripko.model.data.DropOff;
import com.tripko.model.data.Schedule;
import com.tripko.model.temp_data.TempReservation;
import com.tripko.ui.main.MainActivity;
import com.tripko.ui.schedule.details.seat.ActivitySeatSelectionFirstClass;
import com.tripko.ui.schedule.details.seat.ActivitySeatSelectionRegular;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookingActivity extends MvpActivity<BookingView, BookingPresenter> implements BookingView {

    private ActivityBookingBinding binding;
    private Schedule schedule;
    private String modeOfPayment;
    private String selectedPassengerType;
    private int numberOfSeats;
    private String actualSeats;
    private ProgressDialog progressDialog;
    private DropOff selectedDropOff;
    private double passengerTypeDiscount;
    private double totalFare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.onStart();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking);
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getIntExtra(Constants.ID, -1);
        schedule = presenter.getSchedule(id);
        if(schedule == null){
            finish();
            showAlert(getString(R.string.oops));
        }
        binding.setSchedule(schedule);
        //binding.setView(getMvpView());


        //spinner
        Spinner dropdown = binding.spinner;
        final String[] items = new String[]{
                "BDO",
                "BPI",
                "Metrobank"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getSupportActionBar().getThemedContext(), R.layout.spinner_list_style, items);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modeOfPayment = items[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //drop off
        Spinner dropOffSpinner = binding.spinnerDropOff;
        final ArrayAdapter<DropOff> dataAdapter = new ArrayAdapter<DropOff>(getSupportActionBar().getThemedContext(), R.layout.spinner_list_style, schedule.getDropOffRealmList());
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        dropOffSpinner.setAdapter(dataAdapter);
        dropOffSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DropOff dropOff = dataAdapter.getItem(position);
                selectedDropOff = dropOff;
                onResume();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //passengerType
        Spinner passengerTypeSpinner = binding.spinnerPassengerType;
        final String[] types = new String[]{
                "Adult",
                "Children",
                "Senior Citizen",
                "Student"
        };

        ArrayAdapter<String> adapterPassengerType = new ArrayAdapter<String>(getSupportActionBar().getThemedContext(), R.layout.spinner_list_style, types);
        adapterPassengerType.setDropDownViewResource(R.layout.spinner_dropdown_item);
        passengerTypeSpinner.setAdapter(adapterPassengerType);
        passengerTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (types[position].equals("Adult")) {
                    passengerTypeDiscount = 0;
                } else {
                    passengerTypeDiscount = 0.20;
                }
                selectedPassengerType = types[position];
                onResume();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.selectSeats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if (schedule.getBus().getClassification().trim().equals("STANDARD")) {
                    i = new Intent(BookingActivity.this, ActivitySeatSelectionRegular.class);
                } else {
                    i = new Intent(BookingActivity.this, ActivitySeatSelectionFirstClass.class);
                }
                if (!binding.seats.getText().toString().equals("SEATS")) {
                    i.putExtra(Constants.DATA, 0);
                }
                i.putExtra(Constants.ID, schedule.getScheduleId());
                startActivity(i);
            }
        });

        binding.proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modeOfPayment.equals("")) {
                    showAlert("Please select mode of payment");
                } else if (binding.seats.getText().toString().equals("SEATS")) {
                    showAlert("Please select at least one seat");
                } else if (selectedDropOff == null) {
                    showAlert("Please select your drop off");
                } else {
                    showConfirmDialog();
                }
            }
        });

    }

    @Override
    public void showAlert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void showConfirmDialog() {
        final DialogConfirmBinding dialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_confirm,
                null,
                false);
        final Dialog dialog = new Dialog(this);
        dialogBinding.confirm.setEnabled(false);
        dialogBinding.destTo.setText(selectedDropOff.getName());
        dialogBinding.dialogMode.setText(modeOfPayment);
        dialogBinding.dialogTotalFare.setText("PHP " + totalFare);
        dialogBinding.dialogPassengerType.setText(selectedPassengerType);
        dialogBinding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    dialogBinding.confirm.setEnabled(true);
                else
                    dialogBinding.confirm.setEnabled(false);
            }
        });
        dialogBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialogBinding.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                presenter.onReserve(schedule.getScheduleId(), actualSeats, numberOfSeats, modeOfPayment, selectedDropOff.getDropOffId(), selectedPassengerType, totalFare + "");
            }
        });
        dialogBinding.setSchedule(schedule);
        dialog.setContentView(dialogBinding.getRoot());

        dialog.show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
    }

    @NonNull
    @Override
    public BookingPresenter createPresenter() {
        return new BookingPresenter();
    }

    @Override
    public void setTempReservation(TempReservation tempReservation) {
        if (binding != null) {
            List<String> myList = new ArrayList<>(Arrays.asList(tempReservation.getSeatsReserved().split(",")));
            binding.seats.setText(tempReservation.getSeatsReserved() + "\n\n " + myList.size() + " reservations");
            binding.selectSeats.setText("ADD MORE SEATS");
            numberOfSeats = myList.size();
            actualSeats = tempReservation.getSeatsReserved();


            binding.seatsInfo.setVisibility(View.VISIBLE);

            binding.seatsCount.setText(numberOfSeats + "");
            binding.seats.setText(actualSeats);

            if(selectedDropOff != null) {
                float fare = Float.parseFloat(selectedDropOff.getFare());
                binding.fare.setText("PHP " + fare);
                //fare computation
                double discount = (fare * numberOfSeats) * passengerTypeDiscount;
                totalFare = (fare * numberOfSeats) - discount;
                binding.total.setText("PHP " + totalFare);
            }
        }
    }


    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.please_waits));
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void reservationSuccess() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Reservation Successful");
        alertDialog.setMessage("Thank you for booking with us. Unpaid reservations will be voided 2 hours before the trip. Payments should be made through over the bank deposit.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent(BookingActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });
        alertDialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        TempReservation tempReservation = presenter.tempReservation();
        if (tempReservation != null) {
            setTempReservation(tempReservation);
        }
    }
}
