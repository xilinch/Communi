package com.ht.communi.model.impl;

/**
 * Created by Administrator on 2018/5/3.
 */

public interface CommModelImpl {

    interface BaseListener<T> {
        void getSuccess(T t);

        void getFailure();
    }
}
