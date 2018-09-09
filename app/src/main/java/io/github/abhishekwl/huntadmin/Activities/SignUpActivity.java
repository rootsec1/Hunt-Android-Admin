package io.github.abhishekwl.huntadmin.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;
import java.util.UUID;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.github.abhishekwl.huntadmin.Helpers.ApiClient;
import io.github.abhishekwl.huntadmin.Helpers.ApiInterface;
import io.github.abhishekwl.huntadmin.Models.Store;
import io.github.abhishekwl.huntadmin.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.signUpProfilePictureImageView)
    CircleImageView signUpProfilePictureImageView;
    @BindView(R.id.signUpStoreNameEditText)
    TextInputEditText signUpStoreNameEditText;
    @BindView(R.id.signUpContactNumberEditText)
    TextInputEditText signUpContactNumberEditText;
    @BindView(R.id.signUpEmailAddressEditText)
    TextInputEditText signUpEmailAddressEditText;
    @BindView(R.id.signUpPasswordEditText)
    TextInputEditText signUpPasswordEditText;
    @BindView(R.id.signUpConfirmPasswordEditText)
    TextInputEditText signUpConfirmPasswordEditText;
    @BindView(R.id.signUpDepartmentSpinner)
    Spinner signUpDepartmentSpinner;
    @BindView(R.id.signUpDeliveryYesRadioButton)
    RadioButton signUpDeliveryYesRadioButton;
    @BindView(R.id.signUpDeliveryNoRadioButton)
    RadioButton signUpDeliveryNoRadioButton;
    @BindView(R.id.signUpButton)
    Button signUpButton;
    @BindColor(android.R.color.black) int colorBlack;

    private Location deviceLocation;
    private ApiInterface apiInterface;
    private Unbinder unbinder;
    private MaterialDialog materialDialog;
    private FirebaseAuth firebaseAuth;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int RC_PICK_IMAGE = 910;
    private StorageReference storageReference;
    private Uri profilePictureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        unbinder = ButterKnife.bind(SignUpActivity.this);
        initializeComponents();
        initializeViews();
    }

    private void initializeComponents() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(SignUpActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();
        retrieveDeviceLocation();
        apiInterface = ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);
        String randomInt = UUID.randomUUID().toString();
        storageReference = FirebaseStorage.getInstance().getReference("temp_stores").child(randomInt).child("profile_picture.jpg");
    }

    @SuppressLint("MissingPermission")
    private void retrieveDeviceLocation() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location!=null) deviceLocation = location;
        });
    }

    private void initializeViews() {
        Glide.with(getApplicationContext()).load(R.drawable.price_tag_black).into(signUpProfilePictureImageView);
        setupDepartmentsDropDown();
    }

    private void setupDepartmentsDropDown() {
        String[] departments = getResources().getStringArray(R.array.departments);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, departments);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        signUpDepartmentSpinner.setAdapter(spinnerAdapter);
    }

    @OnClick(R.id.signUpButton)
    public void onSignUpButtonPress() {
        String storeName = signUpStoreNameEditText.getText().toString();
        String contactNumber = signUpContactNumberEditText.getText().toString();
        String emailAddress = signUpEmailAddressEditText.getText().toString();
        String password = signUpPasswordEditText.getText().toString();
        String confirmPassword = signUpConfirmPasswordEditText.getText().toString();
        String department = signUpDepartmentSpinner.getSelectedItem().toString();
        boolean deliveryOffered = signUpDeliveryYesRadioButton.isChecked();

        if (TextUtils.isEmpty(storeName)) notifyMessage("Store name can't be empty.");
        else if (TextUtils.isEmpty(contactNumber) || !TextUtils.isDigitsOnly(contactNumber)) notifyMessage("Please enter a valid contact number.");
        else if (TextUtils.isEmpty(emailAddress) || !emailAddress.contains("@")) notifyMessage("Please enter a valid email address.");
        else if (!password.equals(confirmPassword)) notifyMessage("Passwords do not match.");
        else if (deviceLocation==null) {
            Snackbar.make(signUpButton, "Can't find where you are, make sure you've enabled location services and try again.", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.YELLOW)
                    .setAction("SETTINGS", view -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .show();
        }
        else createNewStore(storeName, contactNumber, emailAddress, password, department, deliveryOffered);
    }

    private void createNewStore(String storeName, String contactNumber, String emailAddress, String password, String department, boolean deliveryOffered) {
        materialDialog = new MaterialDialog.Builder(SignUpActivity.this)
                .title(R.string.app_name)
                .content("Creating new account")
                .progress(true, 0)
                .show();
        signUpButton.setEnabled(false);

        firebaseAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(firebaseAuth.getUid())
                        .setPhotoUri(profilePictureUri)
                        .build();
                Objects.requireNonNull(firebaseAuth.getCurrentUser()).updateProfile(userProfileChangeRequest).addOnCompleteListener(task2 -> {
                    if (task.isSuccessful() && materialDialog.isShowing()) {
                        apiInterface.createStore(firebaseAuth.getUid(), storeName, department, emailAddress, contactNumber, deviceLocation.getLatitude(), deviceLocation.getLongitude(), deliveryOffered)
                                .enqueue(new Callback<Store>() {
                                    @Override
                                    public void onResponse(@NonNull Call<Store> call, @NonNull Response<Store> response) {
                                        notifyMessage("Account created successfully");
                                        Intent mainActivityIntent = new Intent(SignUpActivity.this, MainActivity.class);
                                        mainActivityIntent.putExtra("STORE", response.body());
                                        startActivity(mainActivityIntent);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<Store> call, @NonNull Throwable t) {
                                        Objects.requireNonNull(firebaseAuth.getCurrentUser()).delete().addOnSuccessListener(aVoid -> notifyMessage(t.getMessage()));
                                    }
                                });
                    } else notifyMessage(Objects.requireNonNull(task.getException()).getMessage());
                });
            } else notifyMessage(Objects.requireNonNull(task.getException()).getMessage());
        });
    }

    private void notifyMessage(String message) {
        if (materialDialog.isShowing()) materialDialog.dismiss();
        if (!signUpButton.isEnabled()) signUpButton.setEnabled(true);
        Snackbar.make(signUpButton, message, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.signUpProfilePictureImageView)
    public void onProfilePicturePress() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a photo for your store"), RC_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_PICK_IMAGE && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            Uri selectedFileUri = data.getData();
            materialDialog = new MaterialDialog.Builder(SignUpActivity.this)
                    .title(R.string.app_name)
                    .content("Updating store photo")
                    .contentColor(colorBlack)
                    .progress(true, 0)
                    .show();
            storageReference.putFile(selectedFileUri)
                    .addOnFailureListener(e -> notifyMessage(e.getMessage()))
                    .addOnSuccessListener(taskSnapshot -> {
                        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            profilePictureUri = uri;
                            Glide.with(getApplicationContext()).load(profilePictureUri).into(signUpProfilePictureImageView);
                            notifyMessage("Profile picture updated.");
                        }).addOnFailureListener(e -> notifyMessage(e.getMessage()));
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveDeviceLocation();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
