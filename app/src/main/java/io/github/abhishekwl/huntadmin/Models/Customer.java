package io.github.abhishekwl.huntadmin.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Customer implements Parcelable {

    @SerializedName("uid") private String uid;
    @SerializedName("name") private String name;
    @SerializedName("email") private String email;
    @SerializedName("image") private String image;
    @SerializedName("phone") private String phone;
    @SerializedName("account_number") private String accountNumber;
    @SerializedName("account_holder_name") private String accountHolderName;
    @SerializedName("ifsc") private String ifscCode;

    public Customer(String uid, String name, String email, String image, String phone, String accountNumber, String accountHolderName, String ifscCode) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.image = image;
        this.phone = phone;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.ifscCode = ifscCode;
    }

    public Customer(String uid, String name, String email, String phone, String accountNumber, String accountHolderName, String ifscCode) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.ifscCode = ifscCode;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.image);
        dest.writeString(this.phone);
        dest.writeString(this.accountNumber);
        dest.writeString(this.accountHolderName);
        dest.writeString(this.ifscCode);
    }

    private Customer(Parcel in) {
        this.uid = in.readString();
        this.name = in.readString();
        this.email = in.readString();
        this.image = in.readString();
        this.phone = in.readString();
        this.accountNumber = in.readString();
        this.accountHolderName = in.readString();
        this.ifscCode = in.readString();
    }

    public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel source) {
            return new Customer(source);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };
}
