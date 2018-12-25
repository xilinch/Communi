package com.ht.communi.javabean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 此表表示，某个用户的加入的所有社团社团
 * 最开始是因为将此字段直接加入到用户表中，但是用户表只有当前登陆的用户
 * 能够修改当前用户的值。不能修改其他用户的值，于是想到了这个方案。
 * Created by Administrator on 2018/5/13.
 */

public class StudentCommunity extends BmobObject implements Serializable{
    private Student student;            //某个用户
    private BmobRelation communities;           //此用户加入的所有社团

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public BmobRelation getCommunities() {
        return communities;
    }

    public void setCommunities(BmobRelation communities) {
        this.communities = communities;
    }
}
