package io.github.abhishekwl.huntadmin.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Item implements Parcelable {

    @SerializedName("product_id") private String productId;
    @SerializedName("_id") private String id;
    @SerializedName("name") private String name;
    @SerializedName("subcategory") private String subcategory;
    @SerializedName("category") private String category;
    @SerializedName("price") private double price;
    @SerializedName("discount") private double discount;
    @SerializedName("priority") private double priority;
    @SerializedName("rating") private double rating;
    @SerializedName("image") private String image;
    @SerializedName("order_count") private int orderCount;
    @SerializedName("store") private Store store;
    @SerializedName("colors") private String colors[];

    public Item(String productId, String name, String subcategory, String category, double price, double discount, double priority, double rating, String image, int orderCount, Store store, String colors[]) {
        this.productId = productId;
        this.name = name;
        this.subcategory = subcategory;
        this.category = category;
        this.price = price;
        this.discount = discount;
        this.priority = priority;
        this.rating = rating;
        this.image = image;
        this.orderCount = orderCount;
        this.store = store;
        this.colors = colors;
    }

    public Item(String productId, String name, String subcategory, String category, double price, Store store, String colors[]) {
        this.productId = productId;
        this.name = name;
        this.subcategory = subcategory;
        this.category = category;
        this.price = price;
        this.store = store;
        this.colors = colors;
    }

    public Item(String productId, String name, String subcategory, String category, double price, String image, Store store, String colors[]) {
        this.productId = productId;
        this.name = name;
        this.subcategory = subcategory;
        this.category = category;
        this.price = price;
        this.image = image;
        this.store = store;
        this.colors = colors;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String[] getColors() {
        return colors;
    }

    public void setColors(String[] colors) {
        this.colors = colors;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productId);
        dest.writeString(this.name);
        dest.writeString(this.subcategory);
        dest.writeString(this.category);
        dest.writeDouble(this.price);
        dest.writeDouble(this.discount);
        dest.writeDouble(this.priority);
        dest.writeDouble(this.rating);
        dest.writeString(this.image);
        dest.writeInt(this.orderCount);
        dest.writeParcelable(this.store, flags);
        dest.writeStringArray(this.colors);
    }

    private Item(Parcel in) {
        this.productId = in.readString();
        this.name = in.readString();
        this.subcategory = in.readString();
        this.category = in.readString();
        this.price = in.readDouble();
        this.discount = in.readDouble();
        this.priority = in.readDouble();
        this.rating = in.readDouble();
        this.image = in.readString();
        this.orderCount = in.readInt();
        this.store = in.readParcelable(Store.class.getClassLoader());
        this.colors = in.createStringArray();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
