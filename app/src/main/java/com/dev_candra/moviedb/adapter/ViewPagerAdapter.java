package com.dev_candra.moviedb.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dev_candra.moviedb.fragment.FragmentFavoriteMovie;
import com.dev_candra.moviedb.fragment.FragmentFavoriteTv;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final Fragment[] tabFragment;

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager) {
        super(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        tabFragment = new Fragment[]{
                new FragmentFavoriteMovie(),
                new FragmentFavoriteTv()
        };
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return tabFragment[position];
    }

    @Override
    public int getCount() {
        return tabFragment.length;
    }
}
