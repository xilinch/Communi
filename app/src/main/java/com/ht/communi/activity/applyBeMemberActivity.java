package com.ht.communi.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.ht.communi.adapter.CommApplyAdapter;
import com.ht.communi.javabean.CommunityItem;
import com.ht.communi.javabean.Student;
import com.ht.communi.javabean.StudentCommunity;
import com.ht.communi.presenter.CommunityApplyPresenter;
import com.ht.communi.utils.NetUtil;
import com.ht.communi.view.ICommunityApply;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class applyBeMemberActivity extends AppCompatActivity implements ICommunityApply {
    private CommunityItem communityItem;
    private SwipeMenuListView rv_apply_list;

    // TODO: 2018/5/13 考虑要不要这个刷新的 
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommunityApplyPresenter mPresenter;
    private CommApplyAdapter commApplyAdapter;

    private List<Student> mList = new ArrayList<>();  //临时容器
    private List<Student> mApplyList;     //真正的社团数据
    private ImageView bImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_be_member);
        initView();
        initRefreshList();

        mPresenter = new CommunityApplyPresenter(this);
        communityItem = (CommunityItem) getIntent().getSerializableExtra("COMM_APPLY");
        Log.i("htht", "onCreate:communityItem=== " + communityItem);

        if (NetUtil.checkNet(this) && communityItem != null) {
            mPresenter.onRefresh(communityItem);
        }
    }

    private void initView() {
        rv_apply_list = findViewById(R.id.rv_apply_list);
        swipeRefreshLayout = findViewById(R.id.srl_apply);
        //左上角返回图标
        bImageView = findViewById(R.id.iv_apply_be_member_back);
        bImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRefreshList() {
        // 设置下拉进度的主题颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);

        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (communityItem != null)
                    mPresenter.onRefresh(communityItem);
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dip2px(90));
                // set a icon
                openItem.setIcon(R.drawable.ic_agree);
//                // set item title
//                openItem.setTitle("Agree");
//                // set item title fontsize
//                openItem.setTitleSize(18);
//                // set item title font color
//                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dip2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_refuse);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        rv_apply_list.setMenuCreator(creator);

        rv_apply_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                final Student student = mApplyList.get(position);
                //step1:社团的申请列表移除
                BmobRelation relationApply = new BmobRelation();
                relationApply.remove(student);
                switch (index) {
                    case 0:
                        Log.i("htht", "点击同意: " + student.getStuName());
                        //step1:加入到社团成员列表
                        BmobRelation relationMember = new BmobRelation();
                        relationMember.add(student);
                        communityItem.setCommApplies(relationApply);
                        communityItem.setCommMembers(relationMember);
                        communityItem.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    //step3:将此社团加入到该用户的  所加入社团目录   中
                                    BmobQuery<StudentCommunity> query = new BmobQuery<>();
                                    query.addWhereEqualTo("student", new BmobPointer(student));
                                    query.findObjects(new FindListener<StudentCommunity>() {
                                        @Override
                                        public void done(List<StudentCommunity> list, BmobException e) {
                                            if (e == null) {
                                                Log.i("htht", "done查到啦！！！ " + list.get(0).getObjectId());
                                                BmobRelation relationComm = new BmobRelation();
                                                relationComm.add(communityItem);
                                                StudentCommunity studentCommunity = new StudentCommunity();
                                                studentCommunity.setStudent(student);
                                                studentCommunity.setCommunities(relationComm);
                                                studentCommunity.update(list.get(0).getObjectId(), new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        if (e == null) {
                                                            Log.i("htht", "111同意了那个用户的  用户加入社团修改成功 ");
                                                            //step4:更新listView 数据
                                                            if (communityItem != null)
                                                                mPresenter.onRefresh(communityItem);
                                                        } else {
                                                            Log.i("htht", "222同意了那个用户的  用户加入社团修改失败 " + e.getMessage() + e.getErrorCode());
                                                        }
                                                    }
                                                });
                                            } else {
                                                Log.i("htht", "222 " + e.getMessage() + e.getErrorCode());
                                            }
                                        }
                                    });
                                    Log.i("htht", "333同意了那个用户的 社团信息  修改成功 ");

                                } else {
                                    Log.i("htht", "失败：" + e.getMessage());
                                }
                            }
                        });

                        break;
                    case 1:
                        Log.i("htht", "点击拒绝: " + student.getStuName());
                        // 社团的申请列表移除
                        communityItem.setCommApplies(relationApply);
                        communityItem.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Log.i("htht", "拒绝某个用户加入社团成功 ");
                                    //step2:更新listView 数据
                                    if (communityItem != null)
                                        mPresenter.onRefresh(communityItem);
                                } else {
                                    Log.i("htht", "拒绝某个用户加入社团成功" + e.getMessage());
                                }
                            }
                        });

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        commApplyAdapter = new CommApplyAdapter(this, mList);
        rv_apply_list.setAdapter(commApplyAdapter);
    }


    //view的回调
    @Override
    public void onLoadMore(List<Student> list) {

    }

    //view的回调
    @Override
    public void onRefresh(List<Student> list) {
        mApplyList = list;
        Log.i("htht", "此社团申请列表  刷新完成了 ");
        commApplyAdapter.setDatas(mApplyList);
        commApplyAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
//        if (list.size() == 0) {
//            loading.setVisibility(View.GONE);
//            tip.setVisibility(View.VISIBLE);
//            rv_my_community_list.setVisibility(View.GONE);
//        } else {
//            loading.setVisibility(View.GONE);
//            tip.setVisibility(View.GONE);
//            rv_my_community_list.setVisibility(View.VISIBLE);
//        }
    }

    public int dip2px(float dpValue) {
        Context context = this;
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
