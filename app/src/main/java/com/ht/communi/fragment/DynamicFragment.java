package com.ht.communi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ht.communi.activity.DynamicDetailActivity;
import com.ht.communi.activity.LoginActivity;
import com.ht.communi.activity.R;
import com.ht.communi.activity.SendDynamicActivity;
import com.ht.communi.adapter.DynamicAdapter;
import com.ht.communi.javabean.DynamicItem;
import com.ht.communi.javabean.Student;
import com.ht.communi.presenter.DynamicPresenter;
import com.ht.communi.utils.NetUtil;
import com.ht.communi.view.IDynamic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import me.maxwin.view.XListView;

public class DynamicFragment extends Fragment implements IDynamic,XListView.IXListViewListener{

    private ImageView publish;
    private TextView title;
    private XListView xListView;
    private RelativeLayout loading;
    private LinearLayout tip;

    private DynamicPresenter mPresenter;
    private DynamicAdapter mAdapter;
    private List<DynamicItem> mList = new ArrayList<>();
    private List<DynamicItem> mDynamicList;

    private int currPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dynamic, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        currPage = 0;   //初始化当前页为0
        xListView.setPullRefreshEnable(true);
        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(this);

        mPresenter = new DynamicPresenter(this);
        mAdapter = new DynamicAdapter(getActivity(), R.layout.dynamic_listviewother_item, mList);
        xListView.setAdapter(mAdapter);

//        mPresenter.onRefresh();
        if (NetUtil.checkNet(getActivity())) {
            mPresenter.onRefresh();
        } else {
            loading.setVisibility(View.GONE);
            tip.setVisibility(View.VISIBLE);
            xListView.setVisibility(View.GONE);
        }
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DynamicItem item = mDynamicList.get(position-1);
                Log.i("htht", "onItemClick:====item ====="+item.getCreatedAt());
                Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("DYNAMIC", item);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

    private void initView(){
        publish = getActivity().findViewById(R.id.publish);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student student = BmobUser.getCurrentUser(Student.class);
                if (student != null) {
                    Intent intent = new Intent(getActivity(), SendDynamicActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });

        title = getActivity().findViewById(R.id.title);
        xListView = getActivity().findViewById(R.id.xListView);
        loading = getActivity().findViewById(R.id.loading);
        tip = getActivity().findViewById(R.id.tip);

    }

    //IDynamicFragment回调
    @Override
    public void onLoadMore(List<DynamicItem> list) {
        if(list.size() == 0){
            xListView.setPullLoadEnable(false);
            Toast.makeText(getContext(),"没有更多数据了",Toast.LENGTH_SHORT).show();
            return;
        }
        mDynamicList.addAll(list);
        mAdapter.setDatas(mDynamicList);
        mAdapter.notifyDataSetChanged();
        onLoad();
    }

    //IDynamicFragment回调
    @Override
    public void onRefresh(List<DynamicItem> list) {
        xListView.setPullLoadEnable(true);
        mDynamicList = list;
        mAdapter.setDatas(mDynamicList);
        mAdapter.notifyDataSetChanged();
        onLoad();
    }

    //xList回调
    @Override
    public void onRefresh() {
        currPage = 0;
        mPresenter.onRefresh();
    }

    //xList回调
    @Override
    public void onLoadMore() {
        currPage ++;
        mPresenter.onLoadMore(currPage);
    }

    private void onLoad() {
        loading.setVisibility(View.GONE);
        tip.setVisibility(View.GONE);
        xListView.setVisibility(View.VISIBLE);

        xListView.stopRefresh();
        xListView.stopLoadMore();

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss");//设置日期显示格式
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);// 将时间装换为设置好的格式
        xListView.setRefreshTime(str);//设置时间
    }
}
