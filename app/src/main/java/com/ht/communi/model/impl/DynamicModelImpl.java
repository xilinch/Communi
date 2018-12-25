package com.ht.communi.model.impl;

import com.ht.communi.javabean.Student;

public interface DynamicModelImpl {
    void getDynamicItem(BaseListener listener);
    void getDynamicItemByPhone(Student user, BaseListener listener);

    interface BaseListener<T> {
        void getSuccess(T t);

        void getFailure();
    }
}
