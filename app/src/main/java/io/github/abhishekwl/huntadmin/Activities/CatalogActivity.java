package io.github.abhishekwl.huntadmin.Activities;

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
import android.widget.GridView;
import android.widget.SearchView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;

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
        itemsGridViewAdapter = new ItemsGridViewAdapter(getApplicationContext(), itemArrayList);
        masterItemArrayList.clear();
        masterItemArrayList.addAll(itemArrayList);
        firebaseAuth = FirebaseAuth.getInstance();
        apiInterface = ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);
    }

    private void initializeViews() {
        itemsGridView.setAdapter(itemsGridViewAdapter);
        itemsGridView.setOnItemClickListener((parent, view, position, id) -> {
            Item selectedItem = itemArrayList.get(position);
        });
        catalogSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorAccentSecondary);
        performNetworkRequest();
    }

    private void performNetworkRequest() {
        catalogSwipeRefreshLayout.setRefreshing(true);
        itemArrayList.clear();
        itemsGridViewAdapter.notifyDataSetChanged();

        apiInterface.getItems(firebaseAuth.getUid(), 1).enqueue(new Callback<ArrayList<Item>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Item>> call, @NonNull Response<ArrayList<Item>> response) {
                itemArrayList.clear();
                itemArrayList.addAll(response.body());
                catalogSwipeRefreshLayout.setRefreshing(false);
                lottieAnimationView.setVisibility(View.GONE);
                itemsGridViewAdapter.notifyDataSetChanged();
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
                        itemsGridViewAdapter = new ItemsGridViewAdapter(getApplicationContext(), itemArrayList);
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
