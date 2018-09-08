package io.github.abhishekwl.huntadmin.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.huntadmin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryFragment extends Fragment {


    private View rootView;
    private Unbinder unbinder;

    public InventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_inventory, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initializeComponents();
        initializeViews();
        return rootView;
    }

    private void initializeComponents() {

    }

    private void initializeViews() {

    }

}
