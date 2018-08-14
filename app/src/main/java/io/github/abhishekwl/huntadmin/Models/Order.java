package io.github.abhishekwl.huntadmin.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Order implements Parcelable {

    @SerializedName("delivery_location_latitude") private double deliveryLocationLatitude;
    @SerializedName("delivery_location_longitude") private double getDeliveryLocationLongitude;
    @SerializedName("customer") private Customer customer;
    @SerializedName("items") private ArrayList<Item> itemArrayList;
    @SerializedName("status") private String status = "WAIT";
    @SerializedName("transaction_id") private String transactionId;

    public Order(double deliveryLocationLatitude, double getDeliveryLocationLongitude, Customer customer, ArrayList<Item> itemArrayList, String status, String transactionId) {
        this.deliveryLocationLatitude = deliveryLocationLatitude;
        this.getDeliveryLocationLongitude = getDeliveryLocationLongitude;
        this.customer = customer;
        this.itemArrayList = itemArrayList;
        this.status = status;
        this.transactionId = transactionId;
    }

    public Order(double deliveryLocationLatitude, double getDeliveryLocationLongitude, Customer customer, ArrayList<Item> itemArrayList, String status) {
        this.deliveryLocationLatitude = deliveryLocationLatitude;
        this.getDeliveryLocationLongitude = getDeliveryLocationLongitude;
        this.customer = customer;
        this.itemArrayList = itemArrayList;
        this.status = status;
    }

    public Order(double deliveryLocationLatitude, double getDeliveryLocationLongitude, Customer customer, ArrayList<Item> itemArrayList) {
        this.deliveryLocationLatitude = deliveryLocationLatitude;
        this.getDeliveryLocationLongitude = getDeliveryLocationLongitude;
        this.customer = customer;
        this.itemArrayList = itemArrayList;
    }

    public double getDeliveryLocationLatitude() {
        return deliveryLocationLatitude;
    }

    public void setDeliveryLocationLatitude(double deliveryLocationLatitude) {
        this.deliveryLocationLatitude = deliveryLocationLatitude;
    }

    public double getGetDeliveryLocationLongitude() {
        return getDeliveryLocationLongitude;
    }

    public void setGetDeliveryLocationLongitude(double getDeliveryLocationLongitude) {
        this.getDeliveryLocationLongitude = getDeliveryLocationLongitude;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ArrayList<Item> getItemArrayList() {
        return itemArrayList;
    }

    public void setItemArrayList(ArrayList<Item> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.deliveryLocationLatitude);
        dest.writeDouble(this.getDeliveryLocationLongitude);
        dest.writeParcelable(this.customer, flags);
        dest.writeTypedList(this.itemArrayList);
        dest.writeString(this.status);
        dest.writeString(this.transactionId);
    }

    private Order(Parcel in) {
        this.deliveryLocationLatitude = in.readDouble();
        this.getDeliveryLocationLongitude = in.readDouble();
        this.customer = in.readParcelable(Customer.class.getClassLoader());
        this.itemArrayList = in.createTypedArrayList(Item.CREATOR);
        this.status = in.readString();
        this.transactionId = in.readString();
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
