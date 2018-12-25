package com.ht.communi.view;

import com.ht.communi.javabean.Student;

import java.util.List;

/**
 * Created by Administrator on 2018/5/13.
 */

public interface ICommunityApply {
    //加载更多
    void onLoadMore(List<Student> list);

    //下拉刷新
    void onRefresh(List<Student> list);
}
