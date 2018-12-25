package com.ht.communi.javabean;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2018/4/29.
 */

public class DynamicItem extends BmobObject implements Serializable{

    private Student Writer;

    //作者描述
    private String Text;

    //作者上传图片集合
    private List<BmobFile> PhotoList;

    //作者描述文字
    private String Detail;

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public List<BmobFile> getPhotoList() {
        return PhotoList;
    }

    public void setPhotoList(List<BmobFile> photoList) {
        PhotoList = photoList;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public Student getWriter() {
        return Writer;
    }

    public void setWriter(Student writer) {
        Writer = writer;
    }
}
