package io.github.abhishekwl.huntadmin.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.github.abhishekwl.huntadmin.Helpers.ApiClient;
import io.github.abhishekwl.huntadmin.Helpers.ApiInterface;
import io.github.abhishekwl.huntadmin.Models.Store;
import io.github.abhishekwl.huntadmin.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.signInLogoImageView)
    ImageView signInLogoImageView;
    @BindView(R.id.signInFAB)
    FloatingActionButton signInFAB;
    @BindView(R.id.signInEmailAddressEditText)
    AppCompatEditText signInEmailAddressEditText;
    @BindView(R.id.signInPasswordEditText)
    AppCompatEditText signInPasswordEditText;
    @BindView(R.id.signInSignUpTextView)
    TextView signInSignUpTextView;

    private Unbinder unbinder;
    private FirebaseAuth firebaseAuth;
    private MaterialDialog materialDialog;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentSecondary));
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentSecondary));
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        initializeComponents();
        initializeViews();
    }

    private void initializeComponents() {
        unbinder = ButterKnife.bind(SignInActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();
        apiInterface = ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);
    }

    private void initializeViews() {
        Glide.with(getApplicationContext()).load(R.drawable.price_tag_white).into(signInLogoImageView);
    }

    @OnClick(R.id.signInFAB)
    public void onSignInButtonPress() {
        String emailAddress = Objects.requireNonNull(signInEmailAddressEditText.getText()).toString();
        String password = Objects.requireNonNull(signInPasswordEditText.getText()).toString();
        if (TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password))
            notifyMessage("Don't leave the fields blank.");
        else {
            disableUi("Signing In...");
            firebaseAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) performNetworkRequest();
                else notifyMessage(Objects.requireNonNull(task.getException()).getMessage());
            });
        }
    }

    private void performNetworkRequest() {
        apiInterface.getStore(firebaseAuth.getUid()).enqueue(new Callback<Store>() {
            @Override
            public void onResponse(@NonNull Call<Store> call, @NonNull Response<Store> response) {
                if (response.body()==null) notifyMessage("Invalid credentials");
                else {
                    enableUi();
                    Intent mainActivityIntent = new Intent(SignInActivity.this, MainActivity.class);
                    mainActivityIntent.putExtra("STORE", response.body());
                    startActivity(mainActivityIntent);
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Store> call, @NonNull Throwable t) {
                notifyMessage(t.getMessage());
            }
        });
    }

    private void disableUi(String message) {
        materialDialog = new MaterialDialog.Builder(SignInActivity.this)
                .title(R.string.app_name)
                .content("Signing In...")
                .progress(true, 0)
                .titleColor(Color.BLACK)
                .contentColor(Color.BLACK)
                .show();
        signInFAB.setEnabled(false);
    }

    private void enableUi() {
        if (materialDialog != null && materialDialog.isShowing()) materialDialog.dismiss();
        if (!signInFAB.isEnabled()) signInFAB.setEnabled(true);
    }

    private void notifyMessage(String message) {
        enableUi();
        Snackbar.make(signInLogoImageView, message, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.signInSignUpTextView)
    public void onSignUpButtonPress() {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
