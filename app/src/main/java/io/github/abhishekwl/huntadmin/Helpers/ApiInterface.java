package io.github.abhishekwl.huntadmin.Helpers;

import java.util.ArrayList;

import io.github.abhishekwl.huntadmin.Models.Item;
import io.github.abhishekwl.huntadmin.Models.Store;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    //Stores
    @GET("stores")
    Call<Store> getStore(@Query("uid") String uid);
    @FormUrlEncoded
    @POST("stores")
    Call<Store> createStore(
            @Field("uid") String uid,
            @Field("name") String name,
            @Field("department") String department,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("delivery_service") boolean deliveryService,
            @Field("image") String image
    );


    //Items
    @GET("items")
    Call<ArrayList<Item>> getItems(@Query("uid") String uid, @Query("all") int all);
}
