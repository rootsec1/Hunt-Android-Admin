package io.github.abhishekwl.huntadmin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.huntadmin.Helpers.ApiClient;
import io.github.abhishekwl.huntadmin.Helpers.ApiInterface;
import io.github.abhishekwl.huntadmin.Models.Store;
import io.github.abhishekwl.huntadmin.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splashLogoImageView) ImageView splashLogoImageView;

    private static final long SPLASH_DELAY_LENGTH = 1000;
    private Unbinder unbinder;
    private FirebaseAuth firebaseAuth;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        setContentView(R.layout.activity_splash);

        unbinder = ButterKnife.bind(SplashActivity.this);
        initializeComponents();
    }

    private void initializeComponents() {
        Glide.with(getApplicationContext()).load(R.drawable.price_tag_white).into(splashLogoImageView);
        apiInterface = ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null) {
            new Handler().postDelayed(() -> navigateToNextActivity(null), SPLASH_DELAY_LENGTH);
        } else {
            apiInterface.getStore(firebaseAuth.getUid()).enqueue(new Callback<Store>() {
                @Override
                public void onResponse(@NonNull Call<Store> call, @NonNull Response<Store> response) {
                    sleepAndNavigateAhead(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<Store> call, @NonNull Throwable t) {
                    sleepAndNavigateAhead(null);
                }
            });
        }
    }

    private void sleepAndNavigateAhead(Store store) {
        new Handler().postDelayed(() -> navigateToNextActivity(store), SPLASH_DELAY_LENGTH);
    }

    private void navigateToNextActivity(Store store) {
        if (store==null) startActivity(new Intent(SplashActivity.this, SignInActivity.class));
        else {
            Intent nextActivityIntent = new Intent(SplashActivity.this, MainActivity.class);
            nextActivityIntent.putExtra("STORE", store);
            startActivity(nextActivityIntent);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
