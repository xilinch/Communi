package com.ht.communi.model.impl;

/**
 * Created by Administrator on 2018/5/10.
 */

public interface CommReplyModelImpl {
    interface BaseListener<T> {
        void getSuccess(T t);

        void getFailure();
    }
}
