package com.ht.communi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ht.communi.activity.R;
import com.ht.communi.customView.CircleImageView;
import com.ht.communi.javabean.CommunityReplyItem;

import java.util.List;

/**
 * Created by Administrator on 2018/5/10.
 */

public class CommunityReplyAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<CommunityReplyItem> mDatas;
    private int mLayoutRes;
    private Context mContext;

    public CommunityReplyAdapter(Context context, int layoutRes, List<CommunityReplyItem> datas) {
        this.mContext = context;
        this.mDatas = datas;
        this.mLayoutRes = layoutRes;
        mInflater = LayoutInflater.from(context);
    }


    public List<CommunityReplyItem> returnmDatas() {
        return this.mDatas;
    }

    public void addAll(List<CommunityReplyItem> mDatas) {
        this.mDatas.addAll(mDatas);
    }

    public void setDatas(List<CommunityReplyItem> mDatas) {
        this.mDatas.clear();
        this.mDatas.addAll(mDatas);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            holder = new ViewHolder();
            holder.write_photo = (CircleImageView) convertView.findViewById(R.id.comm_reply_stuIcon_item);
            holder.write_name = (TextView) convertView.findViewById(R.id.comm_reply_stuName_item);
            holder.write_date = (TextView) convertView.findViewById(R.id.comm_reply_time_item);
            holder.dynamic_text = (TextView) convertView.findViewById(R.id.comm_reply_text_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CommunityReplyItem CommunityReplyItem = mDatas.get(position);
        final ViewHolder viewHolder = holder;

        //不用再查一遍了，在官网发现了include。。。。
//        BmobQuery<Student> query = new BmobQuery<>();
//        query.addWhereEqualTo("objectId", CommunityReplyItem.getStudent().getObjectId());
//        query.setLimit(1);
//        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
//        query.findObjects(new FindListener<Student>() {
//            @Override
//            public void done(List<Student> list, BmobException e) {
//                if(e ==null){
//                    if(list != null && list.size() != 0){
//                        viewHolder.write_name.setText(list.get(0).getStuName());
//                        if(list.get(0).getUserIcon()!= null) {
//                            Log.i("htht", "list.get(0).getUserIcon(): "+list.get(0).getUserIcon().getFileUrl());
//                            Glide.with(mContext)
//                                    .load(list.get(0).getUserIcon().getFileUrl())
//                                    .placeholder(R.mipmap.ic_launcher)
//                                    .dontAnimate()
//                                    .error(R.mipmap.ic_launcher)
//                                    .into(viewHolder.write_photo);
//                        }else{
//                            viewHolder.write_photo.setImageResource(R.mipmap.ic_launcher);
//                        }
//                    }
//                }else{
//                    Log.i("htht", "CommunityReplyAdapter...e.getErrorCode()=== "+e.getErrorCode()+"==="+e.getMessage());
//                }
//            }
//        });


        if (CommunityReplyItem.getStudent().getUserIcon() != null) {
            Glide.with(mContext)
                    .load(CommunityReplyItem.getStudent().getUserIcon().getFileUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .dontAnimate()
                    .error(R.mipmap.ic_launcher)
                    .into(viewHolder.write_photo);
        }else {
            viewHolder.write_photo.setImageResource(R.mipmap.ic_launcher);
        }
        viewHolder.write_name.setText(CommunityReplyItem.getStudent().getStuName());
        viewHolder.write_date.setText(CommunityReplyItem.getCreatedAt());
        holder.dynamic_text.setText(CommunityReplyItem.getContent());
        return convertView;
    }

    private final class ViewHolder {
        CircleImageView write_photo;
        TextView write_name;
        TextView write_date;
        TextView dynamic_text;
    }
}
