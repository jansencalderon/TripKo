package com.tripko.profile;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tripko.R;
import com.tripko.app.App;
import com.tripko.app.Constants;
import com.tripko.databinding.ActivityProfileBinding;
import com.tripko.databinding.DialogChangePasswordBinding;
import com.tripko.databinding.DialogUpdateProfilePicBinding;
import com.tripko.model.data.User;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

public class ProfileActivity extends MvpActivity<ProfileView, ProfilePresenter> implements ProfileView {

    private ActivityProfileBinding binding;
    private int PICK_IMAGE_REQUEST = 0;
    private File userImage;
    private ProgressDialog progressDialog;
    private String TAG = ProfileActivity.class.getSimpleName();
    private User user;
    private Dialog dialogUpdateProfilePic;
    private DialogUpdateProfilePicBinding updateProfilePicBinding;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.setView(getMvpView());
        presenter.onStart();
        Nammu.init(this);
        binding.toolbar.setTitle("Profile");
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = App.getUser();
        binding.setUser(user);
        updateProfilePicBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_update_profile_pic,
                null,
                false);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.setCancelable(false);


        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                @Override
                public void permissionGranted() {
                    //Nothing, this sample saves to Public gallery so it needs permission
                }

                @Override
                public void permissionRefused() {
                    finish();
                }
            });
        }


        binding.changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog == null)
                    dialog = new Dialog(ProfileActivity.this);
                final DialogChangePasswordBinding dialogBinding = DataBindingUtil.inflate(
                        getLayoutInflater(),
                        R.layout.dialog_change_password,
                        null,
                        false);
                dialogBinding.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialogBinding.send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.changePassword(dialogBinding.etCurrPassword.getText().toString(),
                                dialogBinding.etNewPassword.getText().toString(),
                                dialogBinding.etConfirmPass.getText().toString());
                    }
                });
                dialog.setContentView(dialogBinding.getRoot());
                dialog.setCancelable(false);
                dialog.show();

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onResume() {
        super.onResume();
        /*Glide.with(this)
                .load(Constants.URL_IMAGE + user.getImage())
                .error(R.drawable.ic_user)
                .into(binding.userImage);*/
    }

    /***
     * Start of MvpViewStateActivity
     ***/

    @NonNull
    @Override
    public ProfilePresenter createPresenter() {
        return new ProfilePresenter();
    }


    /***
     * End of MvpViewStateActivity
     ***/


    /***
     * Start of ProfileView
     ***/
    @Override
    public void onEdit() {
        presenter.updateUser(
                binding.firstName.getText().toString(),
                binding.lastName.getText().toString(),
                binding.contact.getText().toString(),
                binding.birthday.getText().toString(),
                binding.address.getText().toString());
    }

    @Override
    public void showAlert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void startLoading() {
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void finishAct() {
        finish();
        showAlert("Profile Updated");
    }

    @Override
    public void onBirthdayClicked() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                binding.birthday.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    @Override
    public void onPhotoClicked() {
        if (dialogUpdateProfilePic == null) {
            dialogUpdateProfilePic = new Dialog(this);
            dialogUpdateProfilePic.setContentView(updateProfilePicBinding.getRoot());
        }

        /*Glide.with(this)
                .load(Constants.URL_IMAGE + user.getImage())
                .centerCrop()
                .error(R.drawable.ic_user)
                .placeholder(R.drawable.ic_user)
                .into(updateProfilePicBinding.userImage);*/
        updateProfilePicBinding.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openGallery(ProfileActivity.this, PICK_IMAGE_REQUEST);
            }
        });
        updateProfilePicBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUpdateProfilePic.dismiss();
            }
        });
        updateProfilePicBinding.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userImage == null)
                    showAlert("Please pick new image");
                else
                    presenter.updateImage(userImage);


            }
        });

        dialogUpdateProfilePic.show();
    }

    @Override
    public void updateImage() {
        if (dialogUpdateProfilePic.isShowing())
            dialogUpdateProfilePic.dismiss();
        Glide.with(this)
                .load(Constants.URL_IMAGE + user.getImage())
                .centerCrop()
                .dontAnimate()
                .into(binding.userImage);
    }

    @Override
    public void onPasswordChanged() {
        if (dialog.isShowing()) {
            dialog.dismiss();
            showAlert("Password Successfully Changed!");
        }
    }


    /***
     * End of ProfileView
     ***/


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
                userImage = imageFile;
                Log.d(TAG, "image: " + userImage.getAbsolutePath());
                Glide.with(ProfileActivity.this)
                        .load(userImage.getPath())
                        .centerCrop()
                        .dontAnimate()
                        .into(updateProfilePicBinding.userImage);
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_edit:
                onEdit();
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
}
