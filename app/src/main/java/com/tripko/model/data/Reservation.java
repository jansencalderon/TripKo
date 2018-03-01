package com.tripko.model.data;

/**
 * Created by Jansen on 2/19/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Reservation extends RealmObject{

    @PrimaryKey
    @SerializedName("reference_no")
    @Expose
    private Integer referenceNo;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("schedule_id")
    @Expose
    private String scheduleId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("deposit_slip")
    @Expose
    private String depositSlip;
    @SerializedName("total_fare")
    @Expose
    private String totalFare;
    @SerializedName("seats_reserved")
    @Expose
    private String seatsReserved;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("date_approved")
    @Expose
    private Date dateApproved;
    @SerializedName("date_disapproved")
    @Expose
    private Date dateDisapproved;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("date_cancelled")
    @Expose
    private String dateCancelled;
    @SerializedName("schedule")
    @Expose
    private Schedule schedule;

    @SerializedName("mode_payment")
    @Expose
    private String modePayment;

    public Integer getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(Integer referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepositSlip() {
        return depositSlip;
    }

    public void setDepositSlip(String depositSlip) {
        this.depositSlip = depositSlip;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getSeatsReserved() {
        return seatsReserved;
    }

    public void setSeatsReserved(String seatsReserved) {
        this.seatsReserved = seatsReserved;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(Date dateApproved) {
        this.dateApproved = dateApproved;
    }

    public Date getDateDisapproved() {
        return dateDisapproved;
    }

    public void setDateDisapproved(Date dateDisapproved) {
        this.dateDisapproved = dateDisapproved;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDateCancelled() {
        return dateCancelled;
    }

    public void setDateCancelled(String dateCancelled) {
        this.dateCancelled = dateCancelled;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getModePayment() {
        return modePayment;
    }

    public void setModePayment(String modePayment) {
        this.modePayment = modePayment;
    }
}
