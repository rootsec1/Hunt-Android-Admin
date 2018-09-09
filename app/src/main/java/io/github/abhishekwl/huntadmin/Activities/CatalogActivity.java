package io.github.abhishekwl.huntadmin.Activities;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ramotion.fluidslider.FluidSlider;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.huntadmin.Adapters.ItemsGridViewAdapter;
import io.github.abhishekwl.huntadmin.Helpers.ApiClient;
import io.github.abhishekwl.huntadmin.Helpers.ApiInterface;
import io.github.abhishekwl.huntadmin.Models.Item;
import io.github.abhishekwl.huntadmin.Models.Store;
import io.github.abhishekwl.huntadmin.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogActivity extends AppCompatActivity {

    @BindView(R.id.catalogGridView) GridView itemsGridView;
    @BindView(R.id.catalogFAB) FloatingActionButton catalogFAB;
    @BindView(R.id.catalogSwipeRefreshLayout) SwipeRefreshLayout catalogSwipeRefreshLayout;
    @BindView(R.id.catalogLottieAnimationView) LottieAnimationView lottieAnimationView;

    private Unbinder unbinder;
    private Store currentStore;
    private ItemsGridViewAdapter itemsGridViewAdapter;
    private ArrayList<Item> itemArrayList = new ArrayList<>();
    private ArrayList<Item> masterItemArrayList = new ArrayList<>();
    private ApiInterface apiInterface;
    private FirebaseAuth firebaseAuth;
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        unbinder = ButterKnife.bind(CatalogActivity.this);
        initializeComponents();
        initializeViews();
    }

    private void initializeComponents() {
        currentStore = getIntent().getParcelableExtra("STORE");
        itemsGridViewAdapter = new ItemsGridViewAdapter(getApplicationContext(), itemArrayList, CatalogActivity.this);
        masterItemArrayList.clear();
        masterItemArrayList.addAll(itemArrayList);
        firebaseAuth = FirebaseAuth.getInstance();
        apiInterface = ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);
    }

    private void initializeViews() {
        itemsGridView.setAdapter(itemsGridViewAdapter);
        catalogSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorAccentSecondary);
        catalogSwipeRefreshLayout.setOnRefreshListener(this::performNetworkRequest);
        itemsGridView.setOnItemClickListener((parent, view, position, id) -> editItem(itemArrayList.get(position)));
        performNetworkRequest();
    }

    @SuppressLint("SetTextI18n")
    private void editItem(Item item) {
        materialDialog = new MaterialDialog.Builder(CatalogActivity.this)
                .title(item.getName())
                .customView(R.layout.item_price_discount_dialog, true)
                .positiveText("UPDATE")
                .negativeText("CANCEL")
                .positiveColorRes(R.color.colorAccent)
                .negativeColorRes(R.color.colorTextDark)
                .showListener(dialog -> {
                    EditText itemOriginalPriceEditText = (EditText) ((MaterialDialog) dialog).findViewById(R.id.itemDialogItemPriceEditText);
                    FluidSlider itemDiscountSlider = (FluidSlider) ((MaterialDialog) dialog).findViewById(R.id.itemDialogDiscountSlider);

                    itemOriginalPriceEditText.setText(Double.toString(item.getPrice()));
                    itemDiscountSlider.setPosition((float) item.getDiscount()/100);
                })
                .onPositive((dialog, which) -> {
                    EditText itemOriginalPriceEditText = (EditText) dialog.findViewById(R.id.itemDialogItemPriceEditText);
                    FluidSlider itemDiscountSlider = (FluidSlider) dialog.findViewById(R.id.itemDialogDiscountSlider);

                    double newItemPrice = Double.parseDouble(itemOriginalPriceEditText.getText().toString());
                    double newItemDiscount = roundTwoDecimals(itemDiscountSlider.getPosition())*100;

                    updateItemDetails(item, newItemPrice, newItemDiscount);
                })
                .onNegative((dialog, which) -> dialog.dismiss())
                .show();
    }

    double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    private void performNetworkRequest() {
        catalogSwipeRefreshLayout.setRefreshing(true);
        itemArrayList.clear();
        itemsGridViewAdapter.notifyDataSetChanged();

        apiInterface.getItems(firebaseAuth.getUid(), 1).enqueue(new Callback<ArrayList<Item>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Item>> call, @NonNull Response<ArrayList<Item>> response) {
                itemArrayList = new ArrayList<>(Objects.requireNonNull(response.body()));
                catalogSwipeRefreshLayout.setRefreshing(false);
                lottieAnimationView.setVisibility(View.GONE);
                itemsGridViewAdapter = new ItemsGridViewAdapter(getApplicationContext(), itemArrayList, CatalogActivity.this);
                itemsGridView.setAdapter(itemsGridViewAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Item>> call, @NonNull Throwable t) {
                Snackbar.make(itemsGridView, t.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.catalog_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.catalogSearch).getActionView();
        searchView.setSearchableInfo(Objects.requireNonNull(searchManager).getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search catalog");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                catalogSwipeRefreshLayout.setRefreshing(true);
                lottieAnimationView.setVisibility(View.GONE);
                itemArrayList.clear();
                itemsGridViewAdapter.notifyDataSetChanged();

                apiInterface.getItems(firebaseAuth.getUid(), query).enqueue(new Callback<ArrayList<Item>>() {
                    @Override
                    public void onResponse(@NonNull Call<ArrayList<Item>> call, @NonNull Response<ArrayList<Item>> response) {
                        itemArrayList.clear();
                        itemArrayList.addAll(response.body());
                        itemsGridViewAdapter = new ItemsGridViewAdapter(getApplicationContext(), itemArrayList, CatalogActivity.this);
                        catalogSwipeRefreshLayout.setRefreshing(false);
                        itemsGridView.setAdapter(itemsGridViewAdapter);

                        if (itemArrayList.isEmpty()) {
                            lottieAnimationView.setVisibility(View.VISIBLE);
                            Snackbar.make(itemsGridView, query.concat(" is currently unavailable"), Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ArrayList<Item>> call, @NonNull Throwable t) {
                        catalogSwipeRefreshLayout.setRefreshing(false);
                        lottieAnimationView.setVisibility(View.VISIBLE);
                        Snackbar.make(itemsGridView, t.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) performNetworkRequest();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    private void updateItemDetails(Item item, double newItemPrice, double newItemDiscount) {
        apiInterface.updateItem(item.getId(), newItemPrice, newItemDiscount).enqueue(new Callback<Item>() {
            @Override
            public void onResponse(@NonNull Call<Item> call, @NonNull Response<Item> response) {
                Snackbar.make(itemsGridView, "Item updated. Refreshing", Snackbar.LENGTH_SHORT).show();
                performNetworkRequest();
            }

            @Override
            public void onFailure(@NonNull Call<Item> call, @NonNull Throwable t) {
                Snackbar.make(itemsGridView, t.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent mainActivityIntent = new Intent(CatalogActivity.this, MainActivity.class);
                mainActivityIntent.putExtra("STORE", currentStore);
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
