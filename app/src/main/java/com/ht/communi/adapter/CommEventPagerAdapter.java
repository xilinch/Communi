package com.ht.communi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ht.communi.fragment.CommunityFragment;
import com.ht.communi.fragment.EventFragment;

/**
 * Created by Administrator on 2018/5/3.
 */

public class CommEventPagerAdapter extends FragmentPagerAdapter {
    String[] tab_title;

    public CommEventPagerAdapter(FragmentManager manager) {
        super(manager);
        tab_title = new String[]{"全校社团","近期活动"};
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new CommunityFragment();
                break;
            case 1:
                fragment = new EventFragment();
                break;
        }
        return fragment;

    }

    @Override
    public int getCount() {
        return tab_title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab_title[position];
    }
}
