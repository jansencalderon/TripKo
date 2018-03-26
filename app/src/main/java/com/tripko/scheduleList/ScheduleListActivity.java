package com.tripko.scheduleList;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tripko.R;
import com.tripko.app.App;
import com.tripko.app.Constants;
import com.tripko.databinding.ActivityScheduleListBinding;
import com.tripko.databinding.DialogFilterBinding;
import com.tripko.model.data.Company;
import com.tripko.model.data.DestFrom;
import com.tripko.model.data.DestTo;
import com.tripko.model.data.Schedule;
import com.tripko.ui.schedule.ScheduleActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

public class ScheduleListActivity extends MvpActivity<ScheduleListView, ScheduleListPresenter> implements ScheduleListView {
    private ActivityScheduleListBinding binding;
    public String TAG = this.getClass().getSimpleName();
    private ScheduleListAdapter scheduleListAdapter;
    List<String> companyItems = new ArrayList<>();
    List<String> destFromItems = new ArrayList<>();
    List<String> destToItems = new ArrayList<>();
    private String filterFrom = "";
    private String filterFromValue = "";
    private String filterTo = "";
    private String filterToValue = "";
    private String filterCompanyId = "";
    private String filterCompanyIdValue = "";
    private String filterNoOfPassenger = "";
    private String filterDate = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.onStart();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_list);
        binding.setView(getMvpView());
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scheduleListAdapter = new ScheduleListAdapter(getMvpView());
        binding.recyclerView.setAdapter(scheduleListAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent i = getIntent();
        filterDate = i.getStringExtra("filterDate");
        filterFrom = i.getStringExtra("filterFrom");
        filterTo = i.getStringExtra("filterTo");
        filterFromValue = i.getStringExtra("filterFromValue");
        filterToValue = i.getStringExtra("filterToValue");

        Log.d(TAG, "Date" +filterDate +"\n"
                    +"From " +filterFrom+ filterFromValue +"\n"
                    +"To " +filterTo + filterToValue);

        presenter.getSchedules(filterCompanyId + "", filterDate, filterFrom + "", filterTo + "", filterNoOfPassenger + "");

    }


    @Override
    public void startLoading() {
        binding.swipeRefreshLayout.setRefreshing(true);
    }


    @Override
    public void stopLoading() {
        if (binding.swipeRefreshLayout.isRefreshing())
            binding.swipeRefreshLayout.setRefreshing(false);
    }


    @NonNull
    @Override
    public ScheduleListPresenter createPresenter() {
        return new ScheduleListPresenter();
    }


    @Override
    public void showAlert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void refreshList() {

    }

    @Override
    public void setList(List<Schedule> schedules) {
        if (App.getUser() == null) {
            return;
        }
        if (schedules.size() <= 0) {
            binding.resultLayout.noResultLayout.setVisibility(View.VISIBLE);
            if (App.isPassenger()) {
                binding.resultLayout.resultText.setText("Oops! No Result\nTry Clearing Filters");
            } else {
                binding.resultLayout.resultText.setText("Oops! No Assigned Bus Yet");
            }
            binding.recyclerView.setVisibility(View.GONE);
        } else {
            binding.resultLayout.noResultLayout.setVisibility(View.GONE);
            Log.e(TAG, "Schedules is Visible");
            binding.recyclerView.setVisibility(View.VISIBLE);
        }
        scheduleListAdapter.setList(schedules);
    }

    @Override
    public void onItemClicked(Schedule item) {
        Intent intent = new Intent(this, ScheduleActivity.class);
        intent.putExtra(Constants.ID, item.getScheduleId());
        intent.putExtra("fromMain", true);
        startActivity(intent);
    }

    @Override
    public void addFilterData(List<String> destFromItems, List<String> destToItems, List<String> companyItems) {
        this.destFromItems = destFromItems;
        this.destToItems = destToItems;
        this.companyItems = companyItems;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (App.getUser().getRole().equals("Passenger")) {
            getMenuInflater().inflate(R.menu.filter, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_filter:
                showFilterDialog();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showFilterDialog() {
        final Dialog dialog = new Dialog(this);
        final DialogFilterBinding dialogFilterBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_filter,
                null,
                false);

        dialogFilterBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialogFilterBinding.filterNumber.setText(filterNoOfPassenger);
        dialogFilterBinding.etBday.setText(filterDate);
        dialogFilterBinding.clearDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFilterBinding.etBday.getText().clear();
            }
        });

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getSupportActionBar().getThemedContext(), R.layout.spinner_list_style, companyItems);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        dialogFilterBinding.spinnerCompany.setAdapter(adapter);
        dialogFilterBinding.spinnerCompany.setSelection(adapter.getPosition(filterCompanyIdValue));
        dialogFilterBinding.spinnerCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    Realm realm = Realm.getDefaultInstance();
                    Integer companyId = realm.where(Company.class).equalTo("companyName", adapter.getItem(position)).findFirst().getCompanyId();
                    filterCompanyId = companyId + "";
                    Log.e(TAG, "Company ID" + companyId);
                    realm.close();
                }
                filterCompanyIdValue = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final ArrayAdapter<String> adapterFrom = new ArrayAdapter<>(getSupportActionBar().getThemedContext(), R.layout.spinner_list_style, destFromItems);
        adapterFrom.setDropDownViewResource(R.layout.spinner_dropdown_item);
        dialogFilterBinding.spinnerDestinationFrom.setAdapter(adapterFrom);
        dialogFilterBinding.spinnerDestinationFrom.setSelection(adapterFrom.getPosition(filterFromValue.trim()));
        dialogFilterBinding.spinnerDestinationFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    Realm realm = Realm.getDefaultInstance();
                    Integer fromId = realm.where(DestFrom.class).equalTo("terminalName", adapterFrom.getItem(position)).findFirst().getTerminalId();
                    filterFrom = fromId + "";
                    Log.e(TAG, "From ID " + fromId);
                    realm.close();
                }
                filterFromValue = adapterFrom.getItem(position);
                Log.e(TAG, "From ID Value" + filterFromValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        final ArrayAdapter<String> adapterTo = new ArrayAdapter<>(getSupportActionBar().getThemedContext(), R.layout.spinner_list_style, destToItems);
        adapterTo.setDropDownViewResource(R.layout.spinner_dropdown_item);
        dialogFilterBinding.spinnerDestinationTo.setAdapter(adapterTo);
        dialogFilterBinding.spinnerDestinationTo.setSelection(adapterTo.getPosition(filterToValue.trim()));
        dialogFilterBinding.spinnerDestinationTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    Realm realm = Realm.getDefaultInstance();
                    Integer toId = realm.where(DestTo.class).equalTo("terminalName", adapterTo.getItem(position)).findFirst().getTerminalId();
                    filterTo = toId + "";
                    Log.e(TAG, "To ID " + toId);
                    realm.close();
                }
                filterToValue = adapterTo.getItem(position);
                Log.e(TAG, "To ID Value" + filterToValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        dialogFilterBinding.etBday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(ScheduleListActivity.this, new DatePickerDialog.OnDateSetListener() {
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        dialogFilterBinding.etBday.setText(dateFormatter.format(newDate.getTime()));
                        filterDate = dateFormatter.format(newDate.getTime());
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });


        dialogFilterBinding.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDate = dialogFilterBinding.etBday.getText().toString();

                filterNoOfPassenger = dialogFilterBinding.filterNumber.getText().toString().trim();
                presenter.getSchedules(filterCompanyId + "", filterDate, filterFrom + "", filterTo + "", filterNoOfPassenger + "");
                dialog.dismiss();
            }
        });


        dialog.setContentView(dialogFilterBinding.getRoot());
        dialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
    }
}
