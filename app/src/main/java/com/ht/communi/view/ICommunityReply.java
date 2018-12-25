package com.ht.communi.view;

import com.ht.communi.javabean.CommunityReplyItem;

import java.util.List;

/**
 * Created by Administrator on 2018/5/10.
 */

public interface ICommunityReply {
    //加载更多
    void onLoadMore(List<CommunityReplyItem> list);

    //下拉刷新
    void onRefresh(List<CommunityReplyItem> list);

    //下拉刷新
    void onSendFinish();
}
