package io.github.abhishekwl.huntadmin.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.huntadmin.Helpers.ApiClient;
import io.github.abhishekwl.huntadmin.Helpers.ApiInterface;
import io.github.abhishekwl.huntadmin.Models.Store;
import io.github.abhishekwl.huntadmin.R;

public class MainActivity extends AppCompatActivity {

    private Unbinder unbinder;
    private FirebaseAuth firebaseAuth;
    private ApiInterface apiInterface;
    private Store currentStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initializeComponents();
        initializeViews();
    }

    private void initializeComponents() {
        unbinder = ButterKnife.bind(MainActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();
        apiInterface = ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);
        currentStore = getIntent().getParcelableExtra("STORE");
    }

    private void initializeViews() {
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
