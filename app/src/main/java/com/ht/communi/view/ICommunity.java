package com.ht.communi.view;

import com.ht.communi.javabean.CommunityItem;

import java.util.List;

/**
 * Created by Administrator on 2018/5/3.
 */

public interface ICommunity {
    //加载更多
    void onLoadMore(List<CommunityItem> list);

    //下拉刷新
    void onRefresh(List<CommunityItem> list);
}
