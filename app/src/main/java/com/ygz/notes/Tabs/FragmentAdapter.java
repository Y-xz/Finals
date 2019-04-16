package com.ygz.notes.Tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2018-01-15.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments;
    public FragmentAdapter(FragmentManager fm,Fragment[] fragments){
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position % fragments.length];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
   //     super.destroyItem(container, position, object);
    }
}
