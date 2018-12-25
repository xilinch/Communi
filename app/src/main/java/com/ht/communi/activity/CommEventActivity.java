package com.ht.communi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ht.communi.adapter.EventAdapter;
import com.ht.communi.javabean.CommunityItem;
import com.ht.communi.javabean.EventItem;
import com.ht.communi.presenter.EventPresenter;
import com.ht.communi.utils.NetUtil;
import com.ht.communi.view.IEvent;

import java.util.ArrayList;
import java.util.List;

public class CommEventActivity extends AppCompatActivity implements IEvent {
    private SwipeRefreshLayout swipeRefreshLayout;  //刷新控件
    private RecyclerView rv_event_list;
    private EventAdapter eventAdapter;
    private EventPresenter mPresenter;
    private List<EventItem> mList = new ArrayList<>();  //临时容器
    private List<EventItem> mEventList;     //真正的社团数据
    private ImageView bImageView;
    private CommunityItem communityItem;
    private TextView tv_comm_event_title;
    private ImageView iv_comm_event_add;
    private boolean isLeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_event);
        initView();
        initRefresh();

        //获取是否为本社团管理员
        isLeader = getIntent().getBooleanExtra("IS_LEADER",false);
        if(isLeader){
            iv_comm_event_add.setVisibility(View.VISIBLE);
        }else{
            iv_comm_event_add.setVisibility(View.GONE);
        }

        //获得当前社团
        communityItem = (CommunityItem) getIntent().getSerializableExtra("COMM_EVENT");
        if (communityItem != null) {
            tv_comm_event_title.setText(communityItem.getCommName() + "的活动");
        }

        mPresenter = new EventPresenter(this);
        eventAdapter = new EventAdapter(this, mList);

        rv_event_list.setAdapter(eventAdapter);
        rv_event_list.setLayoutManager(new LinearLayoutManager(this));

        if (NetUtil.checkNet(this) && communityItem != null) {
//            mPresenter.onRefresh();
            mPresenter.onRefreshxact(communityItem);
        }
    }


    private void initView() {
        rv_event_list = findViewById(R.id.rv_comm_event_list);
        swipeRefreshLayout = findViewById(R.id.srl_comm_event);
        tv_comm_event_title = findViewById(R.id.tv_comm_event_title);

        //添加一个社团活动，这里只有是社团管理员才能看到。
        iv_comm_event_add = findViewById(R.id.iv_comm_event_add);
        iv_comm_event_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommEventActivity.this, CreateEventActivity.class);
                Bundle bundle = new Bundle();
                if (communityItem != null) {
                    bundle.putSerializable("CREATE_COMM_EVENT", communityItem);
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        //左上角返回图标
        bImageView = findViewById(R.id.iv_comm_event_back);
        //左上角返回
        bImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRefresh() {
        // 设置下拉进度的主题颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);

        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                mPresenter.onRefresh();
                if (communityItem != null) {
                    mPresenter.onRefreshxact(communityItem);
                }
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
        Log.i("htht", "onRefresh:刷新成功并玩成了11 " + list.get(0).getEventName());

        mEventList = list;
        eventAdapter.setDatas(mEventList);
        eventAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}
