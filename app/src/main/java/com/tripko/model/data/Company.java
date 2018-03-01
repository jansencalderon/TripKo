package com.tripko.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jansen on 2/19/2018.
 */

public class Company extends RealmObject{

    @PrimaryKey
    @SerializedName("company_id")
    @Expose
    private Integer companyId;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("date_joined")
    @Expose
    private String dateJoined;
    @SerializedName("contract_expiration")
    @Expose
    private String contractExpiration;
    @SerializedName("contact_person")
    @Expose
    private String contactPerson;
    @SerializedName("contact_no")
    @Expose
    private String contactNo;
    @SerializedName("image")
    @Expose
    private String image;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(String dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getContractExpiration() {
        return contractExpiration;
    }

    public void setContractExpiration(String contractExpiration) {
        this.contractExpiration = contractExpiration;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}

