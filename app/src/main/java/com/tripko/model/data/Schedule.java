package com.tripko.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jansen on 2/19/2018.
 */

public class Schedule extends RealmObject{

    @PrimaryKey
    @SerializedName("schedule_id")
    @Expose
    private Integer scheduleId;
    @SerializedName("bus_id")
    @Expose
    private Integer busId;
    @SerializedName("destination_from")
    @Expose
    private Integer destinationFrom;
    @SerializedName("destination_to")
    @Expose
    private Integer destinationTo;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("company_id")
    @Expose
    private Integer companyId;
    @SerializedName("seats_taken")
    @Expose
    private String seats_taken;
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("seat_available")
    @Expose
    private Integer seatAvailable;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("fare")
    @Expose
    private Integer fare;
    @SerializedName("company")
    @Expose
    private Company company;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("dest_from")
    @Expose
    private DestFrom destFrom;
    @SerializedName("dest_to")
    @Expose
    private DestTo destTo;
    @SerializedName("bus")
    @Expose
    private Bus bus;

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getBusId() {
        return busId;
    }

    public void setBusId(Integer busId) {
        this.busId = busId;
    }

    public Integer getDestinationFrom() {
        return destinationFrom;
    }

    public void setDestinationFrom(Integer destinationFrom) {
        this.destinationFrom = destinationFrom;
    }

    public Integer getDestinationTo() {
        return destinationTo;
    }

    public void setDestinationTo(Integer destinationTo) {
        this.destinationTo = destinationTo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Integer getSeatAvailable() {
        return seatAvailable;
    }

    public void setSeatAvailable(Integer seatAvailable) {
        this.seatAvailable = seatAvailable;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getFare() {
        return fare;
    }

    public void setFare(Integer fare) {
        this.fare = fare;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DestFrom getDestFrom() {
        return destFrom;
    }

    public void setDestFrom(DestFrom destFrom) {
        this.destFrom = destFrom;
    }

    public DestTo getDestTo() {
        return destTo;
    }

    public void setDestTo(DestTo destTo) {
        this.destTo = destTo;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public String getSeats_taken() {
        return seats_taken;
    }

    public void setSeats_taken(String seats_taken) {
        this.seats_taken = seats_taken;
    }
}
