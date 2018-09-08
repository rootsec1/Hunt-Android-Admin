package io.github.abhishekwl.huntadmin.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.huntadmin.Adapters.MainViewPagerAdapter;
import io.github.abhishekwl.huntadmin.Helpers.ApiClient;
import io.github.abhishekwl.huntadmin.Helpers.ApiInterface;
import io.github.abhishekwl.huntadmin.Models.Store;
import io.github.abhishekwl.huntadmin.R;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mainTabLayout)
    TabLayout mainTabLayout;
    @BindView(R.id.mainViewPager)
    ViewPager mainViewPager;
    @BindColor(R.color.colorBackground) int colorBackground;
    @BindColor(android.R.color.white) int colorWhite;

    private Unbinder unbinder;
    private FirebaseAuth firebaseAuth;
    private ApiInterface apiInterface;
    private Store currentStore;
    private MainViewPagerAdapter mainViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);

        initializeComponents();
        initializeViews();
    }

    private void initializeComponents() {
        unbinder = ButterKnife.bind(MainActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();
        apiInterface = ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);
        currentStore = getIntent().getParcelableExtra("STORE");
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
    }

    private void initializeViews() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(currentStore.getName());
        getSupportActionBar().setElevation(0f);
        mainViewPager.setAdapter(mainViewPagerAdapter);
        mainTabLayout.setupWithViewPager(mainViewPager);
        Objects.requireNonNull(mainTabLayout.getTabAt(0)).setIcon(R.drawable.ic_shopping_cart_white_24dp);
        Objects.requireNonNull(mainTabLayout.getTabAt(1)).setIcon(R.drawable.ic_inbox_white_24dp);
        Objects.requireNonNull(mainTabLayout.getTabAt(2)).setIcon(R.drawable.ic_history_white_24dp);
        Objects.requireNonNull(mainTabLayout.getTabAt(3)).setIcon(R.drawable.ic_trending_up_white_24dp);
        Objects.requireNonNull(mainTabLayout.getTabAt(4)).setIcon(R.drawable.ic_person_white_24dp);
        Objects.requireNonNull(Objects.requireNonNull(mainTabLayout.getTabAt(0)).getIcon()).setColorFilter(colorWhite, PorterDuff.Mode.SRC_IN);
        Objects.requireNonNull(Objects.requireNonNull(mainTabLayout.getTabAt(1)).getIcon()).setColorFilter(colorBackground, PorterDuff.Mode.SRC_IN);
        Objects.requireNonNull(Objects.requireNonNull(mainTabLayout.getTabAt(2)).getIcon()).setColorFilter(colorBackground, PorterDuff.Mode.SRC_IN);
        Objects.requireNonNull(Objects.requireNonNull(mainTabLayout.getTabAt(3)).getIcon()).setColorFilter(colorBackground, PorterDuff.Mode.SRC_IN);
        Objects.requireNonNull(Objects.requireNonNull(mainTabLayout.getTabAt(4)).getIcon()).setColorFilter(colorBackground, PorterDuff.Mode.SRC_IN);
        mainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Objects.requireNonNull(tab.getIcon()).setColorFilter(colorWhite, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Objects.requireNonNull(tab.getIcon()).setColorFilter(colorBackground, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemSignOut:
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                finish();
                break;
            case R.id.searchMenuItem:
                Intent catalogIntent = new Intent(MainActivity.this, CatalogActivity.class);
                catalogIntent.putExtra("STORE", currentStore);
                startActivity(catalogIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public Store getCurrentStore() {
        return currentStore;
    }

    public void setCurrentStore(Store currentStore) {
        this.currentStore = currentStore;
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
