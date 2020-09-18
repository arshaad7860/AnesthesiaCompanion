package com.mo.anesthesiacompanion.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class BookingModel implements Parcelable {
    String bookingId, userId, drName, phone, surgeryType,description, hospitalName, room, surgeryTime,surgeryDate,anestheticType,qualificationType;
    Boolean isEmergency;

    public BookingModel() {
    }

    public BookingModel(String bookingId, String userId, String drName, String phone, String surgeryType, String description, String hospitalName, String room, String surgeryTime, String surgeryDate, String anestheticType, String qualificationType, Boolean isEmergency) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.drName = drName;
        this.phone = phone;
        this.surgeryType = surgeryType;
        this.description = description;
        this.hospitalName = hospitalName;
        this.room = room;
        this.surgeryTime = surgeryTime;
        this.surgeryDate = surgeryDate;
        this.anestheticType = anestheticType;
        this.qualificationType = qualificationType;
        this.isEmergency = isEmergency;
    }

    protected BookingModel(Parcel in) {
        bookingId = in.readString();
        userId = in.readString();
        drName = in.readString();
        phone = in.readString();
        surgeryType = in.readString();
        description = in.readString();
        hospitalName = in.readString();
        room = in.readString();
        surgeryTime = in.readString();
        surgeryDate = in.readString();
        anestheticType = in.readString();
        qualificationType = in.readString();
        byte tmpIsEmergency = in.readByte();
        isEmergency = tmpIsEmergency == 0 ? null : tmpIsEmergency == 1;
    }

    public static final Creator<BookingModel> CREATOR = new Creator<BookingModel>() {
        @Override
        public BookingModel createFromParcel(Parcel in) {
            return new BookingModel(in);
        }

        @Override
        public BookingModel[] newArray(int size) {
            return new BookingModel[size];
        }
    };

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

    public String getQualificationType() {
        return qualificationType;
    }

    public void setQualificationType(String qualificationType) {
        this.qualificationType = qualificationType;
    }

    public Boolean getEmergency() {
        return isEmergency;
    }

    public void setEmergency(Boolean emergency) {
        isEmergency = emergency;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookingId);
        dest.writeString(userId);
        dest.writeString(drName);
        dest.writeString(phone);
        dest.writeString(surgeryType);
        dest.writeString(description);
        dest.writeString(hospitalName);
        dest.writeString(room);
        dest.writeString(surgeryTime);
        dest.writeString(surgeryDate);
        dest.writeString(anestheticType);
        dest.writeString(qualificationType);
        dest.writeByte((byte) (isEmergency == null ? 0 : isEmergency ? 1 : 2));
    }
}
