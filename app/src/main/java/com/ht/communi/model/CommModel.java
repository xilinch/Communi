package com.ht.communi.model;

import android.util.Log;

import com.ht.communi.javabean.CommunityItem;
import com.ht.communi.javabean.Student;
import com.ht.communi.javabean.StudentCommunity;
import com.ht.communi.model.impl.CommModelImpl;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2018/5/3.
 */

public class CommModel implements CommModelImpl {
    /**
     * 创建社团
     *
     * @param communityItem 社团item
     */
    public void addCommItem(final CommunityItem communityItem, final BaseListener listener) {
        final BmobFile commIcon = communityItem.getCommIcon();
        if (commIcon != null) {
            commIcon.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.i("htht", "上传社团icon成功:");
                        communityItem.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    Log.i("htht", "社团创建成功，等待验证！！！");
                                    listener.getSuccess(null);
                                } else {
                                    Log.i("htht", "社团创建失败！！！");
                                }
                            }
                        });
                    } else {
                        listener.getFailure();
                    }

                }
            });
        } else {
            communityItem.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.i("htht", "社团创建成功，等待验证！！！");
                        listener.getSuccess(null);
                    } else {
                        listener.getFailure();
                        Log.i("htht", "社团创建失败！！！");
                    }
                }
            });
        }
    }

    /**
     * 修改社团信息
     * @param communityItem
     * @param listener
     */
    public void changeCommItem(final String oldCommItemId,final CommunityItem communityItem, final BaseListener listener){
        final BmobFile commIcon = communityItem.getCommIcon();
        if (commIcon != null) {
            commIcon.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.i("htht", "上传修改后的社团icon成功:");
                        communityItem.update(oldCommItemId, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Log.i("htht", "社团修改成功，等待验证！！！");
                                    listener.getSuccess(null);
                                } else {
                                    Log.i("htht", "社团修改失败！！！");
                                }
                            }
                        });
                    } else {
                        listener.getFailure();
                    }

                }
            });
        } else {
            communityItem.update(oldCommItemId, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.i("htht", "社团修改成功，等待验证！！！");
                        listener.getSuccess(null);
                    } else {
                        Log.i("htht", "社团修改失败！！！");
                    }
                }
            });
        }
    }


    //查询当前社团的申请列表
    public void getCommAppliers(CommunityItem communityItem,final BaseListener listener){

        BmobQuery<Student> query = new BmobQuery<>();
        query.addWhereRelatedTo("commApplies", new BmobPointer(communityItem));
        query.findObjects(new FindListener<Student>() {
            @Override
            public void done(List<Student> list, BmobException e) {
                if(e==null){
                    listener.getSuccess(list);
                    Log.i("htht","查询个数："+list.size());
                }else{
                    listener.getFailure();
                    Log.i("htht","失败："+e.getMessage());
                }
            }
        });
    }

    /**
     * 获取所有的社团消息
     *
     * @param listener
     */
    public void getCommItem(final BaseListener listener) {
        BmobQuery<CommunityItem> query = new BmobQuery<>();
        query.order("-createdAt");
        query.include("commLeader");
        query.findObjects(new FindListener<CommunityItem>() {
            @Override
            public void done(List<CommunityItem> list, BmobException e) {
                if (e == null) {
                    Log.i("htht", "done: 查询社团成功：共   " + list.size() + "  条数据。");
                    listener.getSuccess(list);
                } else {
                    Log.i("htht", "查询社团失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 查找某个社团的点赞数
     *
     * @param item
     * @param listener
     */
    public void getCommLikes(CommunityItem item, final BaseListener listener) {
        BmobQuery<CommunityItem> query = new BmobQuery<>();
        query.getObject(item.getObjectId(), new QueryListener<CommunityItem>() {
            @Override
            public void done(CommunityItem communityItem, BmobException e) {
                if (e == null) {
                    //获得playerName的信息
                    communityItem.getLikes();
                    listener.getSuccess(communityItem);
                } else {
                    listener.getFailure();
                    Log.i("htht", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 查询加入某个社团的所有用户
     *
     * @param item
     * @param listener
     */
    public void getMyStudentItem(CommunityItem item, final BaseListener listener) {
        BmobQuery<Student> query = new BmobQuery<>();
        query.addWhereRelatedTo("commMembers", new BmobPointer(item));
        query.findObjects(new FindListener<Student>() {
            @Override
            public void done(List<Student> list, BmobException e) {
                if (e == null) {
                    Log.i("htht", "done: 查询社团用户成功：共   " + list.size() + "  条数据。");
                    listener.getSuccess(list);
                } else {
                    listener.getFailure();
                    Log.i("htht", "查询社团用户失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

    }

    /**
     * 查询某个用户加入的所有社团
     *
     * @param listener
     */
    public void getMyCommItem( final BaseListener listener) {
        BmobQuery<StudentCommunity> queryStu = new BmobQuery<>();
        Student student =  BmobUser.getCurrentUser(Student.class);
        queryStu.addWhereEqualTo("student",new BmobPointer(student));
        queryStu.findObjects(new FindListener<StudentCommunity>() {
            @Override
            public void done(List<StudentCommunity> list, BmobException e) {
                if (e == null) {
                    BmobQuery<CommunityItem> query = new BmobQuery<>();
                    query.include("commLeader");
                    query.addWhereRelatedTo("communities",new BmobPointer(list.get(0)));
                    query.findObjects(new FindListener<CommunityItem>() {
                        @Override
                        public void done(List<CommunityItem> list, BmobException e) {
                            if (e == null) {
                                Log.i("htht", "done: 查询我的社团成功：共   " + list.size() + "  条数据。");
                                listener.getSuccess(list);
                            } else {
                                listener.getFailure();
                                Log.i("htht", "查询我的社团失败：" + e.getMessage() + "," + e.getErrorCode());
                            }
                        }
                    });
                    Log.i("htht", "done查到啦！！！ "+list.get(0).getObjectId());
                } else {
                    Log.i("htht", "222 " + e.getMessage() + e.getErrorCode());
                }
            }
        });


//        BmobQuery<CommunityItem> query = new BmobQuery<>();
//        Student student =  BmobUser.getCurrentUser(Student.class);
//        query.include("commLeader");
//        query.addWhereRelatedTo("communities",new BmobPointer(student));
//        query.findObjects(new FindListener<CommunityItem>() {
//            @Override
//            public void done(List<CommunityItem> list, BmobException e) {
//                if (e == null) {
//                    Log.i("htht", "done: 查询我的社团成功：共   " + list.size() + "  条数据。");
//                    listener.getSuccess(list);
//                } else {
//                    listener.getFailure();
//                    Log.i("htht", "查询我的社团失败：" + e.getMessage() + "," + e.getErrorCode());
//                }
//            }
//        });
    }


}
