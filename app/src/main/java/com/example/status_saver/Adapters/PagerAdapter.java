package com.example.status_saver.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.status_saver.fragments.ImageFragments;
import com.example.status_saver.fragments.VideoFragments;

public class PagerAdapter extends FragmentPagerAdapter {
    private ImageFragments imageFragments;
    private VideoFragments videoFragments;

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        imageFragments = new ImageFragments();
        videoFragments = new VideoFragments();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return imageFragments;
        } else {
            return videoFragments;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "images";
        } else {
            return "videos";
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
