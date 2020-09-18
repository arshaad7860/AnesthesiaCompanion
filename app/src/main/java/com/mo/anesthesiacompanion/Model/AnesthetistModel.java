package com.mo.anesthesiacompanion.Model;

public class AnesthetistModel {
    String aId,uId,appliedId, phone, email,firstName,lastName,qualification,experience,occupation,hospital;
    boolean isAccepted,isPending;

    public AnesthetistModel() {
    }

    public AnesthetistModel(String aId, String uId, String appliedId, String phone, String email, String firstName, String lastName, String qualification, String experience, String occupation, String hospital, boolean isAccepted, boolean isPending) {
        this.aId = aId;
        this.uId = uId;
        this.appliedId = appliedId;
        this.phone = phone;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.qualification = qualification;
        this.experience = experience;
        this.occupation = occupation;
        this.hospital = hospital;
        this.isAccepted = isAccepted;
        this.isPending = isPending;
    }

    public String getAppliedId() {
        return appliedId;
    }

    public void setAppliedId(String appliedId) {
        this.appliedId = appliedId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
