package io.github.abhishekwl.huntadmin.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.huntadmin.Adapters.CategoryRecyclerViewAdapter;
import io.github.abhishekwl.huntadmin.Models.Category;
import io.github.abhishekwl.huntadmin.Models.Item;
import io.github.abhishekwl.huntadmin.R;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryFragment extends Fragment {


    @BindView(R.id.inventoryFragmentRecyclerView)
    RecyclerView inventoryFragmentRecyclerView;
    @BindView(R.id.inventoryFragmentAddItemFAB)
    FloatingActionButton inventoryFragmentAddItemFAB;

    private View rootView;
    private Unbinder unbinder;
    private ArrayList<Category> categoryArrayList = new ArrayList<>();
    private CategoryRecyclerViewAdapter categoryRecyclerViewAdapter;
    private FirebaseAuth firebaseAuth;
    private OkHttpClient okHttpClient;

    public InventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_inventory, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initializeComponents();
        initializeViews();
        return rootView;
    }

    private void initializeComponents() {
        firebaseAuth = FirebaseAuth.getInstance();
        okHttpClient = new OkHttpClient();
    }

    private void initializeViews() {
        categoryRecyclerViewAdapter = new CategoryRecyclerViewAdapter(rootView.getContext(), categoryArrayList);
        inventoryFragmentRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        inventoryFragmentRecyclerView.setHasFixedSize(true);
        inventoryFragmentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        inventoryFragmentRecyclerView.setAdapter(categoryRecyclerViewAdapter);
        performNetworkRequest();
    }

    private void performNetworkRequest() {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(getString(R.string.base_server_url).concat("items"))).newBuilder();
        urlBuilder.addQueryParameter("uid", firebaseAuth.getUid());
        Request request = new Request.Builder().url(urlBuilder.build().toString()).build();
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                Snackbar.make(inventoryFragmentRecyclerView, e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                try {
                    JSONObject allCategoriesJson = new JSONObject(Objects.requireNonNull(response.body()).string());
                    Iterator iterator = allCategoriesJson.keys();
                    categoryArrayList.clear();
                    while (iterator.hasNext()) {
                        String categoryName = (String) iterator.next();
                        JSONArray itemsInCategory = allCategoriesJson.getJSONArray(categoryName);
                        ArrayList<Item> itemArrayList = new ArrayList<>();
                        for (int i=0; i<itemsInCategory.length(); i++) {
                            JSONObject itemJson = itemsInCategory.getJSONObject(i);
                            Item item = convertItemJsonToPojo(itemJson);
                            if (item!=null) itemArrayList.add(item);
                        }
                        categoryArrayList.add(new Category(categoryName, itemArrayList));
                    }
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        categoryRecyclerViewAdapter.notifyDataSetChanged();
                    });
                } catch (JSONException ignored) { }
            }
        });
    }

    private Item convertItemJsonToPojo(JSONObject itemJson) {
        Gson gson = new Gson();
        return gson.fromJson(itemJson.toString(), Item.class);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        super.onStart();
        unbinder = ButterKnife.bind(this, rootView);
    }
}
