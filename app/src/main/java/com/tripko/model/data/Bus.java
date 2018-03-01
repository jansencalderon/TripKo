package com.tripko.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jansen on 2/19/2018.
 */

public class Bus extends RealmObject{

    @PrimaryKey
    @SerializedName("bus_id")
    @Expose
    private Integer busId;
    @SerializedName("company_id")
    @Expose
    private Integer companyId;
    @SerializedName("classification_id")
    @Expose
    private Integer classificationId;
    @SerializedName("plate_no")
    @Expose
    private String plateNo;
    @SerializedName("route")
    @Expose
    private String route;
    @SerializedName("total_seats")
    @Expose
    private Integer totalSeats;
    @SerializedName("classification_name")
    @Expose
    private String classificationName;
    @SerializedName("classification")
    @Expose
    private Classification classification;

    public Integer getBusId() {
        return busId;
    }

    public void setBusId(Integer busId) {
        this.busId = busId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(Integer classificationId) {
        this.classificationId = classificationId;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

}
