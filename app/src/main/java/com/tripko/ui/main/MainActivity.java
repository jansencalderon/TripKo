package com.tripko.ui.main;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tripko.R;
import com.tripko.app.App;
import com.tripko.app.Constants;
import com.tripko.databinding.ActivityMainBinding;
import com.tripko.databinding.DialogFilterBinding;
import com.tripko.model.data.Company;
import com.tripko.model.data.DestFrom;
import com.tripko.model.data.DestTo;
import com.tripko.model.data.Schedule;
import com.tripko.model.data.User;
import com.tripko.profile.ProfileActivity;
import com.tripko.scheduleList.ScheduleListActivity;
import com.tripko.ui.login.LoginActivity;
import com.tripko.ui.schedule.ScheduleActivity;
import com.tripko.ui.trips.TripsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

public class MainActivity extends MvpActivity<MainView, MainPresenter> implements MainView, NavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding binding;
    public String TAG = this.getClass().getSimpleName();
    private MainListAdapter mainListAdapter;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setView(getMvpView());
        setSupportActionBar(binding.toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        binding.navigationView.setNavigationItemSelectedListener(this);


        //display data
        binding.navigationView.getHeaderView(0).findViewById(R.id.viewProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        //assign adapter
        mainListAdapter = new MainListAdapter(getMvpView());


    }

    @Override
    protected void onResume() {
        super.onResume();
        displayUserData(App.getUser());
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
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void displayUserData(User user) {
        // TextView email = (TextView) binding.navigationView.getHeaderView(0).findViewById(R.id.email);
        TextView name = binding.navigationView.getHeaderView(0).findViewById(R.id.name);
        CircleImageView circleImageView = binding.navigationView.getHeaderView(0).findViewById(R.id.userImage);
        // email.setText(user.getEmail());
        name.setText(user.getFirstName());
        Log.e(TAG, Constants.URL_IMAGE + user.getImage());
        /*Glide.with(this)
                .load(Constants.URL_IMAGE + user.getImage())
                .error(R.drawable.ic_user)
                .into(circleImageView);*/

        if (binding.navigationView.getMenu().size() <= 0) {
            if (!user.getRole().equals("Passenger")) {
                binding.navigationView.inflateMenu(R.menu.activity_main_bus_assistant);
            } else {
                binding.navigationView.inflateMenu(R.menu.activity_main_user);
            }
        }
        binding.navigationView.getMenu().getItem(0).setChecked(true);

        if (!user.getRole().equals("Passenger")) {
            binding.toolbar.setTitle("Bus Schedules");
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.frameLayout.setVisibility(View.GONE);
            presenter.getSchedulesBusAssistant();
            binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    presenter.getSchedulesBusAssistant();
                }
            });
            binding.navigationView.getMenu().getItem(0).setChecked(true);
        } else {
            presenter.getSchedules("", "", "", "", "");
            binding.frameLayout.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
            binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    presenter.getSchedules("", "", "", "", "");
                }
            });

        }
        binding.recyclerView.setAdapter(mainListAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void showAlert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void refreshList() {

    }

    @Override
    public void onLogout() {
        // Do nothing but close the dialog
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realm.close();
                // TODO: 12/4/2016 add flag to clear all task
                Intent intent = new Intent(new Intent(MainActivity.this, LoginActivity.class));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                MainActivity.this.finish();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                realm.close();
                Log.e(TAG, "onError: Error Logging out (deleting all data)", error);
            }
        });
    }

    @Override
    public void setList(List<Schedule> schedules) {
        if (App.getUser() == null) {
            return;
        }
        if (!App.isPassenger()) {
            if (schedules.size() <= 0) {
                binding.resultLayout.noResultLayout.setVisibility(View.VISIBLE);
                if (App.isPassenger()) {
                    binding.resultLayout.resultText.setText("Oops! No Result\nTry Clearing Filters");
                } else {
                    binding.resultLayout.resultText.setText("Oops! No Assigned Bus Yet");
                }
                binding.recyclerView.setVisibility(View.GONE);
            } else{
                binding.resultLayout.noResultLayout.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
            }
        }
        mainListAdapter.setList(schedules);
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


        final ArrayAdapter<String> adapterFrom = new ArrayAdapter<>(getSupportActionBar().getThemedContext(), R.layout.spinner_list_style, destFromItems);
        adapterFrom.setDropDownViewResource(R.layout.spinner_dropdown_item);
        binding.spinnerDestinationFrom.setAdapter(adapterFrom);
        binding.spinnerDestinationFrom.setSelection(adapterFrom.getPosition(filterFromValue.trim()));
        binding.spinnerDestinationFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        binding.spinnerDestinationTo.setAdapter(adapterTo);
        binding.spinnerDestinationTo.setSelection(adapterTo.getPosition(filterToValue.trim()));
        binding.spinnerDestinationTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        binding.clearDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etBday.getText().clear();
            }
        });

        binding.etBday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        binding.etBday.setText(dateFormatter.format(newDate.getTime()));
                        filterDate = dateFormatter.format(newDate.getTime());
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });


        binding.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDate = binding.etBday.getText().toString();

                //presenter.getSchedules(filterCompanyId + "", filterDate, filterFrom + "", filterTo + "", filterNoOfPassenger + "");
                Intent i = new Intent(MainActivity.this, ScheduleListActivity.class);
                i.putExtra("filterDate", filterDate);
                i.putExtra("filterFrom", filterFrom);
                i.putExtra("filterTo", filterTo);
                i.putExtra("filterToValue", filterToValue);
                i.putExtra("filterFromValue", filterFromValue);

                Log.e(TAG, "Filter Date " + filterDate);
                Log.e(TAG, "Filter From " + filterFromValue);
                Log.e(TAG, "Filter To " + filterToValue);
                startActivity(i);
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (App.getUser().getRole().equals("Passenger")) {
            // getMenuInflater().inflate(R.menu.filter, menu);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
        } else if (id == R.id.trip_finder) {
            binding.navigationView.getMenu().getItem(0).setChecked(true);
        } else if (id == R.id.my_trips) {
            binding.navigationView.getMenu().getItem(0).setChecked(true);
            startActivity(new Intent(this, TripsActivity.class));
        } else if (id == R.id.logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Log Out");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    presenter.logout();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (id == R.id.team) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_team);
            dialog.show();
        } else if (id == R.id.about_team) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_about);
            dialog.show();
        } else if (id == R.id.faqs) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_faqs);
            dialog.show();
        } else if (id == R.id.bus_assistant_trips) {
            binding.navigationView.getMenu().getItem(0).setChecked(true);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
    }
}
