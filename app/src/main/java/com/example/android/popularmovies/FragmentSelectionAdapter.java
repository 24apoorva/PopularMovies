package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentSelectionAdapter extends FragmentPagerAdapter {
    private Context mContext;

    FragmentSelectionAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OverviewFragment();
            case 1:
                return new TrailersFragment();
            default:
                return new ReviewsFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return (mContext.getString(R.string.overView));
            case 1:
                return (mContext.getString(R.string.trailer));
            default:
                return (mContext.getString(R.string.reviews));
        }
    }
}
