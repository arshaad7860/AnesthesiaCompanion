package com.mo.anesthesiacompanion.Model;

public class AppliedModel {
    String bookingId, userId,appliedId,anesthetistID, drName, phone, surgeryType,description, hospitalName, room, surgeryTime,surgeryDate,anestheticType;
    Boolean isEmergency,isApplied,isApproved,isPending;

    public AppliedModel() {
    }

    public AppliedModel(String bookingId, String userId, String appliedId, String anesthetistID, String drName, String phone, String surgeryType, String description, String hospitalName, String room, String surgeryTime, String surgeryDate, String anestheticType, Boolean isEmergency, Boolean isApplied, Boolean isApproved, Boolean isPending) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.appliedId = appliedId;
        this.anesthetistID = anesthetistID;
        this.drName = drName;
        this.phone = phone;
        this.surgeryType = surgeryType;
        this.description = description;
        this.hospitalName = hospitalName;
        this.room = room;
        this.surgeryTime = surgeryTime;
        this.surgeryDate = surgeryDate;
        this.anestheticType = anestheticType;
        this.isEmergency = isEmergency;
        this.isApplied = isApplied;
        this.isApproved = isApproved;
        this.isPending = isPending;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppliedId() {
        return appliedId;
    }

    public void setAppliedId(String appliedId) {
        this.appliedId = appliedId;
    }

    public String getAnesthetistID() {
        return anesthetistID;
    }

    public void setAnesthetistID(String anesthetistID) {
        this.anesthetistID = anesthetistID;
    }

    public String getDrName() {
        return drName;
    }

    public void setDrName(String drName) {
        this.drName = drName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSurgeryType() {
        return surgeryType;
    }

    public void setSurgeryType(String surgeryType) {
        this.surgeryType = surgeryType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSurgeryTime() {
        return surgeryTime;
    }

    public void setSurgeryTime(String surgeryTime) {
        this.surgeryTime = surgeryTime;
    }

    public String getSurgeryDate() {
        return surgeryDate;
    }

    public void setSurgeryDate(String surgeryDate) {
        this.surgeryDate = surgeryDate;
    }

    public String getAnestheticType() {
        return anestheticType;
    }

    public void setAnestheticType(String anestheticType) {
        this.anestheticType = anestheticType;
    }

    public Boolean getEmergency() {
        return isEmergency;
    }

    public void setEmergency(Boolean emergency) {
        isEmergency = emergency;
    }

    public Boolean getApplied() {
        return isApplied;
    }

    public void setApplied(Boolean applied) {
        isApplied = applied;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public Boolean getPending() {
        return isPending;
    }

    public void setPending(Boolean pending) {
        isPending = pending;
    }
}
