package io.github.abhishekwl.huntadmin.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ramotion.fluidslider.FluidSlider;

import java.util.Objects;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.github.abhishekwl.huntadmin.Activities.MainActivity;
import io.github.abhishekwl.huntadmin.Helpers.ApiClient;
import io.github.abhishekwl.huntadmin.Helpers.ApiInterface;
import io.github.abhishekwl.huntadmin.Models.Store;
import io.github.abhishekwl.huntadmin.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.profileFragmentImageView)
    CircleImageView profileFragmentImageView;
    @BindView(R.id.profileFragmentStoreNameEditText)
    TextInputEditText profileFragmentStoreNameEditText;
    @BindView(R.id.profileFragmentStoreContactNumberEditText)
    TextInputEditText profileFragmentStoreContactNumberEditText;
    @BindView(R.id.profileFragmentStoreDepartmentSpinner)
    Spinner profileFragmentStoreDepartmentSpinner;
    @BindView(R.id.profileFragmentDeliveryYesRadioButton)
    RadioButton profileFragmentDeliveryYesRadioButton;
    @BindView(R.id.profileFragmentDeliveryNoRadioButton)
    RadioButton profileFragmentDeliveryNoRadioButton;
    @BindView(R.id.profileFragmentDeliveryDistanceThresholdSlider)
    FluidSlider profileFragmentDeliveryDistanceThresholdSlider;
    @BindView(R.id.profileFragmentExtraDistanceUnitCostSlider)
    FluidSlider profileFragmentExtraDistanceUnitCostSlider;
    @BindView(R.id.profileFragmentFreeDeliveryCostThresholdSlider)
    FluidSlider profileFragmentFreeDeliveryCostThresholdSlider;
    @BindView(R.id.profileFragmentUpdateSettingsCardView)
    CardView profileFragmentUpdateSettingsCardView;
    @BindColor(android.R.color.black) int colorBlack;

    private View rootView;
    private ApiInterface apiInterface;
    private Unbinder unbinder;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private MaterialDialog materialDialog;
    private Store currentStore;
    private static final int RC_PICK_IMAGE = 910;
    private ArrayAdapter<String> spinnerAdapter;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        initializeComponents();
        initializeViews();
        return rootView;
    }

    private void initializeComponents() {
        unbinder = ButterKnife.bind(this, rootView);
        apiInterface = ApiClient.getClient(rootView.getContext()).create(ApiInterface.class);
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("stores").child(Objects.requireNonNull(firebaseAuth.getUid())).child("profile_picture.jpg");
    }


    private void initializeViews() {
        setupDepartmentsDropDown();
        fetchStore(firebaseAuth.getUid());
    }

    private void fetchStore(String uid) {
        apiInterface.getStore(uid).enqueue(new Callback<Store>() {
            @Override
            public void onResponse(@NonNull Call<Store> call, @NonNull Response<Store> response) {
                renderStore(Objects.requireNonNull(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<Store> call, @NonNull Throwable t) {
                notifyMessage(t.getMessage());
            }
        });
    }

    private void renderStore(Store store) {
        currentStore = store;
        ((MainActivity) Objects.requireNonNull(getActivity())).setCurrentStore(currentStore);
        Glide.with(rootView.getContext()).load(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getPhotoUrl()).into(profileFragmentImageView);
        profileFragmentStoreNameEditText.setText(store.getName());
        profileFragmentStoreContactNumberEditText.setText(store.getPhone());
        profileFragmentDeliveryYesRadioButton.setChecked(store.isDeliveryService());
        profileFragmentDeliveryNoRadioButton.setChecked(!store.isDeliveryService());
        profileFragmentStoreDepartmentSpinner.setSelection(spinnerAdapter.getPosition(store.getDepartment()));
        profileFragmentDeliveryDistanceThresholdSlider.setPosition((float) store.getDeliveryDistanceThreshold()/100);
        profileFragmentExtraDistanceUnitCostSlider.setPosition((float) store.getExtraDistanceUnitCost()/100);
        profileFragmentFreeDeliveryCostThresholdSlider.setPosition((float) store.getFreeDeliveryCostThreshold()/100);
    }

    private void setupDepartmentsDropDown() {
        String[] departments = rootView.getContext().getResources().getStringArray(R.array.departments);
        spinnerAdapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_spinner_item, departments);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileFragmentStoreDepartmentSpinner.setAdapter(spinnerAdapter);
    }

    @OnClick(R.id.profileFragmentImageView)
    public void onChangeStoreImageButtonPress() {
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
            materialDialog = new MaterialDialog.Builder(rootView.getContext())
                    .title(R.string.app_name)
                    .content("Updating store photo")
                    .contentColor(colorBlack)
                    .progress(true, 0)
                    .show();
            storageReference.putFile(selectedFileUri)
                    .addOnFailureListener(e -> Snackbar.make(profileFragmentImageView, e.getMessage(), Snackbar.LENGTH_SHORT).show())
                    .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(this::uploadStoreImage));
        }
    }

    private void uploadStoreImage(Uri uri) {
        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(firebaseAuth.getUid())
                .setPhotoUri(uri)
                .build();
        Objects.requireNonNull(firebaseAuth.getCurrentUser()).updateProfile(userProfileChangeRequest).addOnCompleteListener(task -> {
            if (task.isSuccessful() && materialDialog.isShowing()) {
                materialDialog.dismiss();
                Snackbar.make(profileFragmentImageView, "Store image updated.", Snackbar.LENGTH_SHORT).show();
                apiInterface.getStore(firebaseAuth.getUid()).enqueue(new Callback<Store>() {
                    @Override
                    public void onResponse(@NonNull Call<Store> call, @NonNull Response<Store> response) {
                        renderStore(Objects.requireNonNull(response.body()));
                    }

                    @Override
                    public void onFailure(@NonNull Call<Store> call, @NonNull Throwable t) {
                        Snackbar.make(profileFragmentImageView, t.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
            } else {
                materialDialog.dismiss();
                Snackbar.make(profileFragmentImageView, Objects.requireNonNull(task.getException()).getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void notifyMessage(String message) {
        if (materialDialog.isShowing()) materialDialog.dismiss();
        if (!profileFragmentUpdateSettingsCardView.isEnabled()) profileFragmentUpdateSettingsCardView.setEnabled(true);
        Snackbar.make(profileFragmentUpdateSettingsCardView, message, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.profileFragmentUpdateSettingsCardView)
    public void onSettingsUpdateButton() {
        String storeName = profileFragmentStoreNameEditText.getText().toString();
        String storeContactNumber = profileFragmentStoreContactNumberEditText.getText().toString();
        String storeDepartment = profileFragmentStoreDepartmentSpinner.getSelectedItem().toString();
        boolean deliveryServiceOffered = profileFragmentDeliveryYesRadioButton.isChecked();
        double freeDeliveryDistanceThreshold = profileFragmentDeliveryDistanceThresholdSlider.getPosition()*100;
        double deliveryExtraDistanceUnitCost = profileFragmentExtraDistanceUnitCostSlider.getPosition()*100;
        double freeDeliveryCostThreshold = profileFragmentFreeDeliveryCostThresholdSlider.getPosition()*100;

        materialDialog = new MaterialDialog.Builder(rootView.getContext())
                .title(R.string.app_name)
                .content("Updating store details")
                .progress(true, 0)
                .contentColor(colorBlack)
                .show();

        apiInterface.updateStore(currentStore.getId(), storeName, storeContactNumber, storeDepartment, deliveryServiceOffered, freeDeliveryDistanceThreshold, deliveryExtraDistanceUnitCost, freeDeliveryCostThreshold)
                .enqueue(new Callback<Store>() {
                    @Override
                    public void onResponse(@NonNull Call<Store> call, @NonNull Response<Store> response) {
                        renderStore(Objects.requireNonNull(response.body()));
                        notifyMessage("Updated store details.");
                    }

                    @Override
                    public void onFailure(@NonNull Call<Store> call, @NonNull Throwable t) {
                        notifyMessage(t.getMessage());
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
