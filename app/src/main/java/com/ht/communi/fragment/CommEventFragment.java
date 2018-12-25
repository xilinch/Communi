package com.ht.communi.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ht.communi.activity.R;
import com.ht.communi.adapter.CommEventPagerAdapter;

public class CommEventFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CommEventPagerAdapter viewpageradapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_comm_event, container, false);
        initView(v);
        return v;
    }

    private void initView(View view) {
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpager);
        viewpageradapter = new CommEventPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewpageradapter);
        viewPager.setOffscreenPageLimit(0);
        tabLayout.setupWithViewPager(viewPager);
    }


}
