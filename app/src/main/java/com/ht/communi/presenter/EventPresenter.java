package com.ht.communi.presenter;

import com.ht.communi.javabean.CommunityItem;
import com.ht.communi.javabean.EventItem;
import com.ht.communi.model.EventModel;
import com.ht.communi.model.impl.EventModelImpl;
import com.ht.communi.view.IEvent;

import java.util.List;

/**
 * 加载社团活动数据
 * Created by Administrator on 2018/5/7.
 */

public class EventPresenter {
    private EventModel mEventModel = new EventModel();
    private IEvent mView;

    public EventPresenter(IEvent mView) {
        this.mView = mView;
    }

    /**
     * 获取所有社团的活动的方法
     */
    public void onRefresh() {
        mEventModel.getEventItem(new EventModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                List<EventItem> list = (List<EventItem>) o;
                mView.onRefresh(list);
            }

            @Override
            public void getFailure() {

            }
        });
    }


    /**
     * 获取某个社团的所有社团活动的方法
     */
    public void onRefreshxact(CommunityItem communityItem) {
        mEventModel.getCommEventItem(communityItem, new EventModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                List<EventItem> list = (List<EventItem>) o;
                mView.onRefresh(list);
            }

            @Override
            public void getFailure() {

            }
        });

    }


    public void onLoadMore(int currPage) {
    }
}
