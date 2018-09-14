package com.tripko.ui.trips.detail;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.tripko.R;
import com.tripko.app.Constants;
import com.tripko.databinding.ActivityTripDetailBinding;
import com.tripko.databinding.DialogDepositSlipBinding;
import com.tripko.databinding.DialogTicketBinding;
import com.tripko.model.data.BankAccount;
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
    BankAccount bankAccount;

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
        bankAccount = presenter.getBankAccount(reservation.getSchedule().getCompany().getCompanyId());
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

        switch (reservation.getSchedule().getCompany().getCompanyId()) {
            case 5:
                switch (reservation.getModePayment()) {
                    case "BDO":
                        binding.bankAccount.setText("Acct No. : 1431328430");
                        break;
                    case "BPI":
                        binding.bankAccount.setText("Acct No. : 1236-6321-12");
                        break;
                    case "Metrobank":
                        binding.bankAccount.setText("Acct No. : 8426945124");
                        break;
                }
                break;
            case 6:
                switch (reservation.getModePayment()) {
                    case "BDO":
                        binding.bankAccount.setText("Acct No. : 1431328431");
                        break;
                    case "BPI":
                        binding.bankAccount.setText("Acct No. : 1236-6321-13");
                        break;
                    case "Metrobank":
                        binding.bankAccount.setText("Acct No. : 8426945125");
                        break;
                }
                break;
            case 7:
                switch (reservation.getModePayment()) {
                    case "BDO":
                        binding.bankAccount.setText("Acct No. : 1431328432");
                        break;
                    case "BPI":
                        binding.bankAccount.setText("Acct No. : 1236-6321-14");
                        break;
                    case "Metrobank":
                        binding.bankAccount.setText("Acct No. : 8426945126");
                        break;
                }
                break;
            case 8:
                switch (reservation.getModePayment()) {
                    case "BDO":
                        binding.bankAccount.setText("Acct No. : 1431328433");
                        break;
                    case "BPI":
                        binding.bankAccount.setText("Acct No. : 1236-6321-15");
                        break;
                    case "Metrobank":
                        binding.bankAccount.setText("Acct No. : 8426945127");
                        break;
                }
                break;
            case 9:
                switch (reservation.getModePayment()) {
                    case "BDO":
                        binding.bankAccount.setText("Acct No. : 1431328434");
                        break;
                    case "BPI":
                        binding.bankAccount.setText("Acct No. : 1236-6321-16");
                        break;
                    case "Metrobank":
                        binding.bankAccount.setText("Acct No. : 8426945128");
                        break;
                }
                break;
        }
    }

    private void setReservationData(Reservation reservation) {
        binding.setReservation(reservation);
        if (reservation.getStatus().equals("R")) {
            binding.upload.setVisibility(View.VISIBLE);
        } else {
            binding.upload.setVisibility(View.GONE);
        }

        binding.refNo.setText("Reference No. : " + reservation.getReferenceNo());
        switch (reservation.getStatus()) {
            case "R":
                binding.statusBg.setBackgroundColor(ContextCompat.getColor(TripDetailActivity.this, R.color.orange));
                binding.reason.setVisibility(View.GONE);
                break;
            case "P":
                binding.statusBg.setBackgroundColor(ContextCompat.getColor(TripDetailActivity.this, R.color.blue));
                binding.reason.setVisibility(View.GONE);
                break;
            case "A":
                binding.statusBg.setBackgroundColor(ContextCompat.getColor(TripDetailActivity.this, R.color.greenSuccess));
                binding.reason.setVisibility(View.GONE);
                binding.refNo.setText("Ticket No. : " + reservation.getReferenceNo());
                break;
            case "D":
                binding.statusBg.setBackgroundColor(ContextCompat.getColor(TripDetailActivity.this, R.color.redFailed));
                binding.reason.setVisibility(View.VISIBLE);
                break;
            default:
                binding.statusBg.setBackgroundColor(ContextCompat.getColor(TripDetailActivity.this, R.color.redFailed));
                binding.reason.setVisibility(View.GONE);
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
                if (imageFile.getPath().substring(imageFile.getPath().lastIndexOf(".")).equals(".jpg")
                        || imageFile.getPath().substring(imageFile.getPath().lastIndexOf(".")).equals(".jpeg")) {

                    userImage = imageFile;
                    Glide.with(TripDetailActivity.this)
                            .load(userImage)
                            .centerCrop()
                            .dontAnimate()
                            .error(R.drawable.ic_user)
                            .into(dialogBinding.imageView);
                } else {
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
            case R.id.ticket_action:
                if (reservation.getStatus().equals("A")) {
                    showTicket();
                } else {
                    showAlert("You can't view your ticket yet");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showTicket() {
        final DialogTicketBinding dialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_ticket,
                null,
                false);
        String text = reservation.getReferenceNo()+""; // Whatever you need to encode in the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.CODABAR, 300, 180);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            dialogBinding.qr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        dialogBinding.setReservation(reservation);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogBinding.getRoot());
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reservation, menu);
        return true;
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
