package com.ht.communi.model;

import android.util.Log;

import com.ht.communi.javabean.CommunityItem;
import com.ht.communi.javabean.CommunityReplyItem;
import com.ht.communi.model.impl.CommReplyModelImpl;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2018/5/10.
 */

public class CommReplyModel implements CommReplyModelImpl {

    /**
     * 获取所有的朋友圈消息
     *
     * @param listener
     */
    public void getCommReplyItem(CommunityItem communityItem, final BaseListener listener) {
        BmobQuery<CommunityReplyItem> query = new BmobQuery<>();
        query.addWhereEqualTo("community", new BmobPointer(communityItem));
        query.include("student");
        query.order("-createdAt");
        query.findObjects(new FindListener<CommunityReplyItem>() {
            @Override
            public void done(List<CommunityReplyItem> list, BmobException e) {
                if (e == null) {
                    Log.i("htht", "done: 查询社团评论成功：共   " + list.size() + "  条数据。");
                    listener.getSuccess(list);
                } else {
                    Log.i("htht", "查询西柚社广场失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });


    }

    public void addCommReplyItem(CommunityReplyItem communityReplyItem, final BaseListener baseListener) {
        communityReplyItem.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.i("htht", "社团评论回复成功！！！");
                    baseListener.getSuccess(null);
                } else {
                    baseListener.getFailure();
                    Log.i("htht", "社团评论回复失败！！！");
                }
            }
        });
    }
}
