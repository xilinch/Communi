package com.ht.communi.javabean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/5/10.
 */

public class CommunityReplyItem extends BmobObject implements Serializable {
    private Student student;        //评论者
    private String content;     //评论内容
    private CommunityItem community;        //评论的社团


    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CommunityItem getCommunity() {
        return community;
    }

    public void setCommunity(CommunityItem community) {
        this.community = community;
    }
}
