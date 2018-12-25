package com.ht.communi.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ht.communi.customView.CustomViewPager;
import com.ht.communi.fragment.CommEventFragment;
import com.ht.communi.fragment.DynamicFragment;
import com.ht.communi.fragment.PersonalFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CustomViewPager viewPager;
    private List<Fragment> fragmentList;
    private BottomBar bottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initViewPager();
        initBottomBar();

    }
    private void initView(){
        bottomBar =  findViewById(R.id.bottomBar);
        viewPager =  findViewById(R.id.viewPager);
    }
    private void initBottomBar(){
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.tab_community:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.tab_talk:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.tab_personal:
                        viewPager.setCurrentItem(2);
                        break;
                    default:
                        viewPager.setCurrentItem(0);
                        break;
                }
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_talk) {

                }
            }
        });
    }
    private void initViewPager() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new CommEventFragment());
        fragmentList.add(new DynamicFragment());
        fragmentList.add(new PersonalFragment());
        //避免切回fragment1的时候，销毁fragment3，保存fragment3的状态
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }


            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomBar.selectTabAtPosition(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
