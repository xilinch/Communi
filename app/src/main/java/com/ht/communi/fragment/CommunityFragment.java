package com.ht.communi.fragment;

import android.content.Intent;
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

import com.github.clans.fab.FloatingActionButton;
import com.ht.communi.activity.CreateCommunityActivity;
import com.ht.communi.activity.MyCommunityActivity;
import com.ht.communi.activity.R;
import com.ht.communi.adapter.CommunityAdapter;
import com.ht.communi.javabean.CommunityItem;
import com.ht.communi.presenter.CommunityPresenter;
import com.ht.communi.utils.NetUtil;
import com.ht.communi.view.ICommunity;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment implements ICommunity {

    private SwipeRefreshLayout swipeRefreshLayout;  //刷新控件
    private RecyclerView rv_community_list;
    private CommunityAdapter communityAdapter;
    private CommunityPresenter mPresenter;
    private List<CommunityItem> mList = new ArrayList<>();  //临时容器
    private List<CommunityItem> mCommunityList;     //真正的社团数据
    private FloatingActionButton menu_add_comm;
    private FloatingActionButton menu_my_comm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initRefresh();

        mPresenter = new CommunityPresenter(this);
        communityAdapter = new CommunityAdapter(getContext(), mList);

        rv_community_list.setAdapter(communityAdapter);
        rv_community_list.setLayoutManager(new LinearLayoutManager(getContext()));

//        if (NetUtil.checkNet(getActivity())) {
//            mPresenter.onRefresh();
//        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (NetUtil.checkNet(getActivity())) {
            mPresenter.onRefresh();
        }
    }

    private void initView() {
        rv_community_list = getActivity().findViewById(R.id.rv_community_list);
        menu_add_comm = getActivity().findViewById(R.id.menu_add_comm);
        menu_add_comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateCommunityActivity.class);
                startActivity(intent);
            }
        });
        menu_my_comm = getActivity().findViewById(R.id.menu_my_comm);
        menu_my_comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), CreateEventActivity.class);
                Intent intent = new Intent(getContext(), MyCommunityActivity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout = getActivity().findViewById(R.id.srl_comm);
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
    public void onLoadMore(List<CommunityItem> list) {

    }

    //view的onRefresh
    @Override
    public void onRefresh(List<CommunityItem> list) {
        Log.i("htht", "CommunityItemonRefresh:刷新完成了 ");
        mCommunityList = list;
        communityAdapter.setDatas(mCommunityList);
        communityAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

}
