package io.github.abhishekwl.huntadmin.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.github.abhishekwl.huntadmin.Fragments.AnalyticsFragment;
import io.github.abhishekwl.huntadmin.Fragments.CatalogFragment;
import io.github.abhishekwl.huntadmin.Fragments.HistoryFragment;
import io.github.abhishekwl.huntadmin.Fragments.OrdersFragment;
import io.github.abhishekwl.huntadmin.Fragments.ProfileFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0: return new OrdersFragment();
            case 1: return new CatalogFragment();
            case 2: return new HistoryFragment();
            case 3: return new AnalyticsFragment();
            default: return new ProfileFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    /*
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Orders";
            case 1: return "Catalog";
            case 2: return "History";
            case 3: return "Analytics";
            default: return "Profile";
        }
    }
    */
}
