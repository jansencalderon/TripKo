package com.tripko.scheduleList;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tripko.app.App;
import com.tripko.app.Constants;
import com.tripko.model.data.Company;
import com.tripko.model.data.DestFrom;
import com.tripko.model.data.DestTo;
import com.tripko.model.data.Schedule;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mark Jansen Calderon on 1/11/2017.
 */

class ScheduleListPresenter extends MvpNullObjectBasePresenter<ScheduleListView> {
    private Realm realm;
    private String TAG = ScheduleListPresenter.class.getSimpleName();
    private RealmResults<Schedule> listRealmResults;

    public void onStart() {
        realm = Realm.getDefaultInstance();

        listRealmResults = realm.where(Schedule.class).findAllAsync();
        listRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Schedule>>() {
            @Override
            public void onChange(RealmResults<Schedule> schedules) {
                getView().setList(schedules);
                getFilterData();
            }
        });


        //add bank accounts
        /*realm.executeTransaction(new Realm.Transaction() {
			@Override
			public void execute(Realm realm) {
				//victory
				BankAccount bankAccount = new BankAccount();
				bankAccount.setBankAccountId(5);
				bankAccount.setBDO("1431328430");
				bankAccount.setBPI("1236-6321-12");
				bankAccount.setMetrobank("8426945124");
				realm.copyToRealmOrUpdate(bankAccount);

				//baliwag
				BankAccount bankAccount2 = new BankAccount();
				bankAccount2.setBankAccountId(6);
				bankAccount2.setBDO("1431328431");
				bankAccount2.setBPI("1236-6321-13");
				bankAccount2.setMetrobank("8426945125");
				realm.copyToRealmOrUpdate(bankAccount2);

				//es transport
				BankAccount bankAccount3 = new BankAccount();
				bankAccount3.setBankAccountId(7);
				bankAccount3.setBDO("1431328432");
				bankAccount3.setBPI("1236-6321-14");
				bankAccount3.setMetrobank("8426945126");
				realm.copyToRealmOrUpdate(bankAccount3);

				//five star
				BankAccount bankAccount4 = new BankAccount();
				bankAccount4.setBankAccountId(8);
				bankAccount4.setBDO("1431328433");
				bankAccount4.setBPI("1236-6321-15");
				bankAccount4.setMetrobank("8426945127");
				realm.copyToRealmOrUpdate(bankAccount4);

				//joy bus
				BankAccount bankAccount5 = new BankAccount();
				bankAccount5.setBankAccountId(9);
				bankAccount5.setBDO("1431328434");
				bankAccount5.setBPI("1236-6321-16");
				bankAccount5.setMetrobank("8426945128");
				realm.copyToRealmOrUpdate(bankAccount5);
			}
		});*/
    }

    void getFilterData() {
        final List<DestFrom> destFroms = realm.where(DestFrom.class).distinctValues("terminalName").findAll();
        final List<String> destFromItems = new ArrayList<>();
        if (!destFromItems.isEmpty()) {
            destFromItems.clear();
        }
        destFromItems.add("");
        for (DestFrom destFrom : destFroms) {
            destFromItems.add(destFrom.getTerminalName());
        }

        final List<DestTo> destTos = realm.where(DestTo.class).distinctValues("terminalName").findAll();
        final List<String> destToItems = new ArrayList<>();
        if (!destToItems.isEmpty()) {
            destToItems.clear();
        }
        destToItems.add("");
        for (DestTo destTo : destTos) {
            destToItems.add(destTo.getTerminalName());
        }

        final List<Company> companies = realm.where(Company.class).distinctValues("companyName").findAll();
        final List<String> companyItems = new ArrayList<>();
        if (!companyItems.isEmpty()) {
            companyItems.clear();
        }
        companyItems.add("");
        for (Company company : companies) {
            companyItems.add(company.getCompanyName());
        }

        getView().addFilterData(destFromItems, destToItems, companyItems);


    }

    void getSchedules(String companyId, String date, String destFrom, String destTo, String noOfPassenger) {
        getView().startLoading();
        App.getInstance().getApiInterface().getSchedules(Constants.BEARER + App.getUser().getApiToken(),
                companyId + "", date, noOfPassenger + "", destFrom + "", destTo + "",
                Constants.APPJSON).enqueue(new Callback<List<Schedule>>() {
            @Override
            public void onResponse(@NonNull Call<List<Schedule>> call, @NonNull final Response<List<Schedule>> response) {
                getView().stopLoading();
                final Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        realm.delete(Schedule.class);
                        realm.copyToRealmOrUpdate(response.body());
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        realm.close();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(@NonNull Throwable error) {
                        realm.close();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t) {
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().stopLoading();
                getView().showAlert("Error Connecting to Server");

            }
        });
    }


    public void onStop() {
        if (listRealmResults.isValid())
            listRealmResults.removeAllChangeListeners();
        realm.removeAllChangeListeners();
        realm.close();
    }


    public void getSchedulesBusAssistant() {
        getView().startLoading();
        App.getInstance().getApiInterface().getSchedulesBusAssistant(Constants.BEARER + App.getUser().getApiToken(),
                Constants.APPJSON).enqueue(new Callback<List<Schedule>>() {
            @Override
            public void onResponse(@NonNull Call<List<Schedule>> call, @NonNull final Response<List<Schedule>> response) {
                getView().stopLoading();
                final Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        realm.delete(Schedule.class);
                        realm.copyToRealmOrUpdate(response.body());
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        realm.close();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(@NonNull Throwable error) {
                        realm.close();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t) {
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().stopLoading();
                getView().showAlert("Error Connecting to Server");

            }
        });
    }
}
