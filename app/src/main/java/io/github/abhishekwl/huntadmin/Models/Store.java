package io.github.abhishekwl.huntadmin.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Store implements Parcelable {

    @SerializedName("uid") private String uid;
    @SerializedName("name") private String name;
    @SerializedName("department") private String department;
    @SerializedName("email") private String email;
    @SerializedName("phone") private String phone;
    @SerializedName("latitude") private double latitude;
    @SerializedName("longitude") private double longitude;
    @SerializedName("image") private String image;
    @SerializedName("delivery_service") private boolean deliveryService;
    @SerializedName("delivery_distance_threshold") private double deliveryDistanceThreshold;
    @SerializedName("extra_distance_unit_cost") private double extraDistanceUnitCost;
    @SerializedName("account_number") private String accountNumber;
    @SerializedName("account_holder_name") private String accountHolderName;
    @SerializedName("ifsc") private String accountIfsc;

    public Store() {
    }

    public Store(String uid, String name, String department, String email, String phone, double latitude, double longitude, String image, boolean deliveryService, double deliveryDistanceThreshold, double extraDistanceUnitCost) {
        this.uid = uid;
        this.name = name;
        this.department = department;
        this.email = email;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.deliveryService = deliveryService;
        this.deliveryDistanceThreshold = deliveryDistanceThreshold;
        this.extraDistanceUnitCost = extraDistanceUnitCost;
    }

    public Store(String uid, String name, String department, String email, String phone, double latitude, double longitude) {
        this.uid = uid;
        this.name = name;
        this.department = department;
        this.email = email;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Store(String uid, String name, String department, String email, String phone, double latitude, double longitude, String image, boolean deliveryService, double deliveryDistanceThreshold, double extraDistanceUnitCost, String accountNumber, String accountHolderName, String accountIfsc) {
        this.uid = uid;
        this.name = name;
        this.department = department;
        this.email = email;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.deliveryService = deliveryService;
        this.deliveryDistanceThreshold = deliveryDistanceThreshold;
        this.extraDistanceUnitCost = extraDistanceUnitCost;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.accountIfsc = accountIfsc;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isDeliveryService() {
        return deliveryService;
    }

    public void setDeliveryService(boolean deliveryService) {
        this.deliveryService = deliveryService;
    }

    public double getDeliveryDistanceThreshold() {
        return deliveryDistanceThreshold;
    }

    public void setDeliveryDistanceThreshold(double deliveryDistanceThreshold) {
        this.deliveryDistanceThreshold = deliveryDistanceThreshold;
    }

    public double getExtraDistanceUnitCost() {
        return extraDistanceUnitCost;
    }

    public void setExtraDistanceUnitCost(double extraDistanceUnitCost) {
        this.extraDistanceUnitCost = extraDistanceUnitCost;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getAccountIfsc() {
        return accountIfsc;
    }

    public void setAccountIfsc(String accountIfsc) {
        this.accountIfsc = accountIfsc;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.name);
        dest.writeString(this.department);
        dest.writeString(this.email);
        dest.writeString(this.phone);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.image);
        dest.writeByte(this.deliveryService ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.deliveryDistanceThreshold);
        dest.writeDouble(this.extraDistanceUnitCost);
        dest.writeString(this.accountNumber);
        dest.writeString(this.accountHolderName);
        dest.writeString(this.accountIfsc);
    }

    private Store(Parcel in) {
        this.uid = in.readString();
        this.name = in.readString();
        this.department = in.readString();
        this.email = in.readString();
        this.phone = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.image = in.readString();
        this.deliveryService = in.readByte() != 0;
        this.deliveryDistanceThreshold = in.readDouble();
        this.extraDistanceUnitCost = in.readDouble();
        this.accountNumber = in.readString();
        this.accountHolderName = in.readString();
        this.accountIfsc = in.readString();
    }

    public static final Parcelable.Creator<Store> CREATOR = new Parcelable.Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel source) {
            return new Store(source);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };
}
