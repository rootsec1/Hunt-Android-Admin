package io.github.abhishekwl.huntadmin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

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

    private Unbinder unbinder;
    private Store currentStore;
    private ItemsGridViewAdapter itemsGridViewAdapter;
    private ArrayList<Item> itemArrayList = new ArrayList<>();
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
        firebaseAuth = FirebaseAuth.getInstance();
        apiInterface = ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);
    }

    private void initializeViews() {
        itemsGridView.setAdapter(itemsGridViewAdapter);
        performNetworkRequest();
    }

    private void performNetworkRequest() {
        apiInterface.getItems(firebaseAuth.getUid(), 1).enqueue(new Callback<ArrayList<Item>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Item>> call, @NonNull Response<ArrayList<Item>> response) {
                itemArrayList.clear();
                itemArrayList.addAll(response.body());
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
