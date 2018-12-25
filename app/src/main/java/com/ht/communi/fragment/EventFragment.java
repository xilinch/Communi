package com.ht.communi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ht.communi.activity.R;
import com.ht.communi.adapter.EventAdapter;
import com.ht.communi.javabean.EventItem;
import com.ht.communi.presenter.EventPresenter;
import com.ht.communi.utils.NetUtil;
import com.ht.communi.view.IEvent;

import java.util.ArrayList;
import java.util.List;

public class EventFragment extends Fragment implements IEvent{

    private SwipeRefreshLayout swipeRefreshLayout;  //刷新控件
    private RecyclerView rv_event_list;
    private EventAdapter eventAdapter;
    private EventPresenter mPresenter;
    private List<EventItem> mList = new ArrayList<>();  //临时容器
    private List<EventItem> mEventList;     //真正的社团数据

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initRefresh();

        mPresenter = new EventPresenter(this);

        eventAdapter = new EventAdapter(getContext(), mList);

        rv_event_list.setAdapter(eventAdapter);
        rv_event_list.setLayoutManager(new LinearLayoutManager(getContext()));

        if (NetUtil.checkNet(getActivity())) {
            mPresenter.onRefresh();
        }
    }

    private void initView() {
        rv_event_list = getActivity().findViewById(R.id.rv_event_list);
        swipeRefreshLayout = getActivity().findViewById(R.id.srl_event);
    }

    private void initRefresh(){
        // 设置下拉进度的主题颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);

        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.onRefresh();
            }
        });
    }


    //view的onLoadMore
    @Override
    public void onLoadMore(List<EventItem> list) {

    }

    //view的onRefresh
    @Override
    public void onRefresh(List<EventItem> list) {
        Log.i("htht", "onRefresh:刷新成功并玩成了11 "+list.get(0).getEventName());

        mEventList = list;
        eventAdapter.setDatas(mEventList);
        eventAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}
