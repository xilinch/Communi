package com.ht.communi.presenter;

import com.ht.communi.javabean.CommunityItem;
import com.ht.communi.javabean.CommunityReplyItem;
import com.ht.communi.model.CommReplyModel;
import com.ht.communi.model.impl.CommReplyModelImpl;
import com.ht.communi.view.ICommunityReply;

import java.util.List;

/**
 * 加载社团的评论数据
 * Created by Administrator on 2018/5/10.
 */

public class CommunityReplyPresenter {
    private CommReplyModel commReplyModel = new CommReplyModel();
    private ICommunityReply mView;

    public CommunityReplyPresenter(ICommunityReply mView) {
        this.mView = mView;
    }

    public void onRefresh(CommunityItem CommunityItem){
        commReplyModel.getCommReplyItem(CommunityItem, new CommReplyModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                List<CommunityReplyItem> list= (List<CommunityReplyItem>) o;
                mView.onRefresh(list);
            }

            @Override
            public void getFailure() {

            }
        });

    }

    public void onLoadMore(){
    }

    public void onSendReply(CommunityReplyItem communityReplyItem){
        commReplyModel.addCommReplyItem(communityReplyItem, new CommReplyModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                mView.onSendFinish();
            }

            @Override
            public void getFailure() {
            }
        });
    }
}
