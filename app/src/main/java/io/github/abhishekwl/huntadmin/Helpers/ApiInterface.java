package io.github.abhishekwl.huntadmin.Helpers;

import java.util.ArrayList;

import io.github.abhishekwl.huntadmin.Models.Item;
import io.github.abhishekwl.huntadmin.Models.Store;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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
            @Field("delivery_service") boolean deliveryService
    );
    @FormUrlEncoded
    @PUT("stores/{id}")
    Call<Store> updateStore(
            @Path("id") String storeId,
            @Field("name") String name,
            @Field("phone") String contactNumber,
            @Field("department") String department,
            @Field("delivery_service") boolean deliveryService,
            @Field("delivery_distance_threshold") double deliveryDistanceThreshold,
            @Field("extra_distance_unit_cost") double extraDistanceUnitCost,
            @Field("free_delivery_cost_threshold") double freeDeliveryCostThreshold
    );


    //Items
    @GET("items")
    Call<ArrayList<Item>> getItems(@Query("uid") String uid, @Query("all") int all);
    @GET("items")
    Call<ArrayList<Item>> getItems(@Query("uid") String uid, @Query("query") String query);
    @FormUrlEncoded
    @PUT("items/{id}")
    Call<Item> updateItem(@Path("id") String id, @Field("price") double price, @Field("discount") double discount);
}
