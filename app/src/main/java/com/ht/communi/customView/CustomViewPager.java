package com.ht.communi.customView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 解决下viewpager滑动和其中的fragment点击所产生的冲突
 * <p>
 * Created by Administrator on 2018/4/29.
 */

public class CustomViewPager extends ViewPager {

    private float startY;
    private float startX;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置
                startY = ev.getRawY();
                startX = ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                float endY = ev.getRawY();
                float endX = ev.getRawX();
//                Log.i("htht", "onInterceptTouchEvent:=====startX=== " + startX);
//                Log.i("htht", "onInterceptTouchEvent:=====startY===" + startY);
//                Log.i("htht", "onInterceptTouchEvent:=====endY===" + endY);
//                Log.i("htht", "onInterceptTouchEvent:=====endX===" + endX);
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                if (getCurrentItem() != getAdapter().getCount()-1){
                    return super.onInterceptTouchEvent(ev);
                }
                if (distanceX == 0 && distanceY == 0) {
                    return false;
                } else {
                    return true;
                }

        }
        return super.onInterceptTouchEvent(ev);
    }

}
