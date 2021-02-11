package com.dev_candra.moviedb.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.dev_candra.moviedb.R;
import com.dev_candra.moviedb.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class FragmentFavorite extends Fragment {

    public  FragmentFavorite(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite,container,false);

        ViewPager viewPager = view.findViewById(R.id.vpFavorite);
        viewPager.setAdapter(new ViewPagerAdapter((getChildFragmentManager())));

        TabLayout tabLayout = view.findViewById(R.id.tabFavorite);
        tabLayout.setupWithViewPager(viewPager);

        (tabLayout.getTabAt(0)).setText("Movie");
        (tabLayout.getTabAt(1)).setText("TV Show");
        return view;
    }
}
