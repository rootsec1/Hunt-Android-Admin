package io.github.abhishekwl.huntadmin.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Category implements Parcelable {

    private String categoryName;
    private ArrayList<Item> itemArrayList;

    public Category(String categoryName, ArrayList<Item> itemArrayList) {
        this.categoryName = categoryName;
        this.itemArrayList = itemArrayList;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<Item> getItemArrayList() {
        return itemArrayList;
    }

    public void setItemArrayList(ArrayList<Item> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryName);
        dest.writeTypedList(this.itemArrayList);
    }

    private Category(Parcel in) {
        this.categoryName = in.readString();
        this.itemArrayList = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
