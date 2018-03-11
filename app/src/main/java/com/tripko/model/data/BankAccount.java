package com.tripko.model.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jansen on 2/19/2018.
 */

public class BankAccount extends RealmObject{

    @PrimaryKey
    private int BankAccountId;
    private String BDO;
    private String Metrobank;
    private String BPI;

    public int getBankAccountId() {
        return BankAccountId;
    }

    public void setBankAccountId(int bankAccountId) {
        BankAccountId = bankAccountId;
    }

    public String getBDO() {
        return BDO;
    }

    public void setBDO(String BDO) {
        this.BDO = BDO;
    }

    public String getMetrobank() {
        return Metrobank;
    }

    public void setMetrobank(String metrobank) {
        Metrobank = metrobank;
    }

    public String getBPI() {
        return BPI;
    }

    public void setBPI(String BPI) {
        this.BPI = BPI;
    }
}
