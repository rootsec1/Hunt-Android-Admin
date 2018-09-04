package io.github.abhishekwl.huntadmin.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.ramotion.fluidslider.FluidSlider;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.github.abhishekwl.huntadmin.Activities.MainActivity;
import io.github.abhishekwl.huntadmin.Helpers.ApiClient;
import io.github.abhishekwl.huntadmin.Helpers.ApiInterface;
import io.github.abhishekwl.huntadmin.Models.Store;
import io.github.abhishekwl.huntadmin.R;

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
    @BindView(R.id.profileFragmentUpdateSettingsButton)
    Button profileFragmentUpdateSettingsButton;

    private View rootView;
    private ApiInterface apiInterface;
    private Unbinder unbinder;
    private Store store;

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
    }


    private void initializeViews() {
        setupDepartmentsDropDown();
        store = ((MainActivity) Objects.requireNonNull(getActivity())).getCurrentStore();
        renderStore(store);
    }

    private void renderStore(Store store) {
        Glide.with(rootView.getContext()).load(store.getImage()).into(profileFragmentImageView);
        profileFragmentStoreNameEditText.setText(store.getName());
        profileFragmentStoreContactNumberEditText.setText(store.getPhone());
        profileFragmentDeliveryYesRadioButton.setChecked(store.isDeliveryService());
        profileFragmentDeliveryNoRadioButton.setChecked(!store.isDeliveryService());
        profileFragmentFreeDeliveryCostThresholdSlider.setPosition((float) store.getFreeDeliveryCostThreshold());
        profileFragmentFreeDeliveryCostThresholdSlider.setBubbleText(Double.toString(store.getFreeDeliveryCostThreshold()));
    }

    private void setupDepartmentsDropDown() {
        String[] departments = rootView.getContext().getResources().getStringArray(R.array.departments);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_spinner_item, departments);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileFragmentStoreDepartmentSpinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
