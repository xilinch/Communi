package com.ht.communi.model;

import android.util.Log;

import com.ht.communi.javabean.CommunityItem;
import com.ht.communi.javabean.EventItem;
import com.ht.communi.model.impl.EventModelImpl;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2018/5/7.
 */

public class EventModel implements EventModelImpl {

    /**
     * 创建社团
     *
     * @param eventItem 社团活动item
     */
    public void addEventItem(final EventItem eventItem, final BaseListener listener) {
        eventItem.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.i("htht", "社团活动创建创建成功！！！");
                    listener.getSuccess(null);
                } else {
                    listener.getFailure();
                    Log.i("htht", "社团活动创建失败！！！");
                }
            }
        });
    }


    /**
     * 获取所有的社团消息
     *
     * @param listener
     */
    public void getEventItem(final BaseListener listener) {
        BmobQuery<EventItem> query = new BmobQuery<>();
        query.order("-eventStart");
        query.include("community");
        query.findObjects(new FindListener<EventItem>() {
            @Override
            public void done(List<EventItem> list, BmobException e) {
                if (e == null) {
                    Log.i("htht", "done: 查询社团成功：共   " + list.size() + "  条数据。");
                    listener.getSuccess(list);
                } else {
                    Log.i("htht", "查询社团失败：" + e.getMessage() + "," + e.getErrorCode());
                    listener.getFailure();
                }
            }
        });
    }

    /**
     *
     * 获取某个社团的所有活动
     * @param communityItem
     * @param listener
     */
    public void getCommEventItem(CommunityItem communityItem ,final BaseListener listener) {
        BmobQuery<EventItem> query = new BmobQuery<>();
        query.order("-eventStart");
        query.include("community");
        query.addWhereEqualTo("community",new BmobPointer(communityItem));
        query.findObjects(new FindListener<EventItem>() {
            @Override
            public void done(List<EventItem> list, BmobException e) {
                if (e == null) {
                    Log.i("htht", "done: 查询某个社团的所有活动成功：共   " + list.size() + "  条数据。");
                    listener.getSuccess(list);
                } else {
                    Log.i("htht", "查询某个社团的所有活动失败：" + e.getMessage() + "," + e.getErrorCode());
                    listener.getFailure();
                }
            }
        });
    }
}
