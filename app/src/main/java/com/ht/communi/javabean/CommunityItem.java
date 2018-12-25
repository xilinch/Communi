package com.ht.communi.javabean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2018/5/3.
 */

public class CommunityItem extends BmobObject implements Serializable{
    private String commName;
    private String commDescription;
    private BmobFile commIcon;
    private Student commLeader;
    private String commSchool;  //属于哪个学校
    private BmobRelation commMembers;
    private BmobRelation commApplies;
    private Integer likes;
    private Boolean verify;  //审核是否同意创建该社团


    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public String getCommSchool() {
        return commSchool;
    }

    public void setCommSchool(String commSchool) {
        this.commSchool = commSchool;
    }

    public String getCommDescription() {
        return commDescription;
    }

    public void setCommDescription(String commDescription) {
        this.commDescription = commDescription;
    }

    public BmobFile getCommIcon() {
        return commIcon;
    }

    public void setCommIcon(BmobFile commIcon) {
        this.commIcon = commIcon;
    }

    public Student getCommLeader() {
        return commLeader;
    }

    public void setCommLeader(Student commLeader) {
        this.commLeader = commLeader;
    }

    public BmobRelation getCommMembers() {
        return commMembers;
    }

    public void setCommMembers(BmobRelation commMembers) {
        this.commMembers = commMembers;
    }

    public BmobRelation getCommApplies() {
        return commApplies;
    }

    public void setCommApplies(BmobRelation commApplies) {
        this.commApplies = commApplies;
    }

    public Boolean getVerify() {
        return verify;
    }

    public void setVerify(Boolean verify) {
        this.verify = verify;
    }
}
