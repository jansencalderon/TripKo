package com.tripko.ui.trips.detail;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tripko.R;
import com.tripko.app.Constants;
import com.tripko.databinding.ActivityTripDetailBinding;
import com.tripko.databinding.DialogDepositSlipBinding;
import com.tripko.model.data.Reservation;

import java.io.File;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class TripDetailActivity extends MvpActivity<TripDetailView, TripDetailPresenter> implements TripDetailView {


    ActivityTripDetailBinding binding;
    Reservation reservation;
    private int PICK_IMAGE_REQUEST = 1;
    private File userImage;
    private ProgressDialog progressDialog;
    private DialogDepositSlipBinding dialogBinding;
    private Dialog dialog;
    String TAG = TripDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trip_detail);
        presenter.onStart();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getIntExtra(Constants.ID, -1);
        if (id == -1)
            finish();

        reservation = presenter.getReservation(id);
        setReservationData(reservation);
        binding.setView(getMvpView());

        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogUploadPic();
            }
        });

        dialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_deposit_slip,
                null,
                false);
        dialog = new Dialog(this);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }

    private void setReservationData(Reservation reservation) {
        binding.setReservation(reservation);
        if (reservation.getStatus().equals("R")) {
            binding.upload.setVisibility(View.VISIBLE);
        } else {
            binding.upload.setVisibility(View.GONE);
        }

        switch (reservation.getStatus()) {
            case "R":
                binding.status.setBackgroundColor(ContextCompat.getColor(TripDetailActivity.this, R.color.orange));
                break;
            case "P":
                binding.status.setBackgroundColor(ContextCompat.getColor(TripDetailActivity.this, R.color.blue));
                break;
            case "A":
                binding.status.setBackgroundColor(ContextCompat.getColor(TripDetailActivity.this, R.color.greenSuccess));
                break;
            case "D":
                binding.status.setBackgroundColor(ContextCompat.getColor(TripDetailActivity.this, R.color.redFailed));
                break;
            default:
                binding.status.setBackgroundColor(ContextCompat.getColor(TripDetailActivity.this, R.color.redFailed));
                break;
        }
    }

    private void showDialogUploadPic() {


        dialogBinding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openGallery(TripDetailActivity.this, PICK_IMAGE_REQUEST);
            }
        });

        dialogBinding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TripDetailActivity.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure this is the correct picture of the deposit slip?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.updateImage(userImage, reservation.getReferenceNo());
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                if (userImage == null)
                    showAlert("Please pick an image");
                else
                    alert.show();
            }
        });
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                showAlert(getString(R.string.oops));
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                if(imageFile.getPath().substring(imageFile.getPath().lastIndexOf(".")).equals(".jpg")
                        ||imageFile.getPath().substring(imageFile.getPath().lastIndexOf(".")).equals(".jpeg")){

                    userImage = imageFile;
                    Glide.with(TripDetailActivity.this)
                            .load(userImage)
                            .centerCrop()
                            .error(R.drawable.ic_user)
                            .into(dialogBinding.imageView);
                }else {
                    showAlert("Only JPEG is allowed");
                }


            }

        });
    }

    @Override
    public void showAlert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateReservation() {
        reservation = presenter.getReservation(reservation.getReferenceNo());
        setReservationData(reservation);

    }

    @Override
    public void dismissDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void reservationCancelled() {

    }

    @NonNull
    @Override
    public TripDetailPresenter createPresenter() {
        return new TripDetailPresenter();
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


    @Override
    public void startLoading() {
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        progressDialog.dismiss();
    }
}
