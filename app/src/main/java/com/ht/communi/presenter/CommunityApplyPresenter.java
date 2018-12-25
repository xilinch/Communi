package com.ht.communi.presenter;

import com.ht.communi.javabean.CommunityItem;
import com.ht.communi.javabean.Student;
import com.ht.communi.model.CommModel;
import com.ht.communi.model.impl.CommModelImpl;
import com.ht.communi.view.ICommunityApply;

import java.util.List;

/**
 * Created by Administrator on 2018/5/13.
 */

public class CommunityApplyPresenter {
    private CommModel mCommModel = new CommModel();
    private ICommunityApply mView;

    public CommunityApplyPresenter(ICommunityApply mView) {
        this.mView = mView;
    }

    public void onRefresh(CommunityItem communityItemn){
        mCommModel.getCommAppliers(communityItemn, new CommModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                List<Student> list = (List<Student>)o;
                mView.onRefresh(list);
            }

            @Override
            public void getFailure() {

            }
        });
    }

    public void onLoadMore( int currPage){
    }
}
