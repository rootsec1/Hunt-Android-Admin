package io.github.abhishekwl.huntadmin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.huntadmin.Models.Store;
import io.github.abhishekwl.huntadmin.R;

public class CatalogActivity extends AppCompatActivity {

    @BindView(R.id.catalogRecyclerView) RecyclerView catalogRecyclerView;
    @BindView(R.id.catalogFAB) FloatingActionButton catalogFAB;

    private Unbinder unbinder;
    private Store currentStore;

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
    }

    private void initializeViews() {

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
