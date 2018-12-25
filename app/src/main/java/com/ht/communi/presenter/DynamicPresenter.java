package com.ht.communi.presenter;


import com.ht.communi.javabean.DynamicItem;
import com.ht.communi.model.DynamicModel;
import com.ht.communi.model.impl.DynamicModelImpl;
import com.ht.communi.view.IDynamic;

import java.util.List;

/**
 * 加载朋友圈的数据
 * 朋友圈的Presenter
 */
public class DynamicPresenter {
    private DynamicModel mDynamicModel = new DynamicModel();
    private IDynamic mView;

    public DynamicPresenter(IDynamic mView) {
        this.mView = mView;
    }

    public void onRefresh(){
        mDynamicModel.getDynamicItem(0, DynamicModel.STATE_REFRESH, new DynamicModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                List<DynamicItem> list= (List<DynamicItem>) o;
                mView.onRefresh(list);
            }

            @Override
            public void getFailure() {

            }
        });
    }

    public void onLoadMore( int currPage){
        mDynamicModel.getDynamicItem(currPage, DynamicModel.STATE_MORE, new DynamicModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                List<DynamicItem> list= (List<DynamicItem>) o;
                mView.onLoadMore(list);
            }

            @Override
            public void getFailure() {

            }
        });
    }

}
