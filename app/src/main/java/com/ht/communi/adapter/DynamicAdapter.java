package com.ht.communi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ht.communi.activity.R;
import com.ht.communi.customView.CircleImageView;
import com.ht.communi.customView.FixedGridView;
import com.ht.communi.javabean.DynamicItem;

import java.util.List;

/**
 * 作用：朋友圈的adapter
 */
public class DynamicAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<DynamicItem> mDatas;
    private int mLayoutRes;
    private Context mContext;

    public DynamicAdapter(Context context, int layoutRes, List<DynamicItem> datas) {
        this.mContext=context;
        this.mDatas = datas;
        this.mLayoutRes = layoutRes;
        mInflater = LayoutInflater.from(context);
    }


    public List<DynamicItem> returnmDatas() {
        return this.mDatas;
    }

    public void addAll(List<DynamicItem> mDatas) {
        this.mDatas.addAll(mDatas);
    }

    public void setDatas(List<DynamicItem> mDatas) {
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
            holder.write_photo = (CircleImageView) convertView.findViewById(R.id.write_photo);
            holder.write_name = (TextView) convertView.findViewById(R.id.write_name);
            holder.write_date = (TextView) convertView.findViewById(R.id.write_date);
            holder.dynamic_text = (TextView) convertView.findViewById(R.id.dynamic_text);
            holder.dynamic_photo = (FixedGridView) convertView.findViewById(R.id.dynamic_photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DynamicItem dynamicItem = mDatas.get(position);
        final ViewHolder viewHolder = holder;

        //不用自己再查一遍了，在官网发现了include。。。。
//        BmobQuery<Student> query = new BmobQuery<>();
//        query.addWhereEqualTo("objectId", dynamicItem.getWriter().getObjectId());
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
//                    Log.i("htht", "DynamicAdapter...e.getErrorCode()=== "+e.getErrorCode()+"==="+e.getMessage());
//                }
//            }
//        });
        if(dynamicItem.getWriter().getUserIcon()!= null) {
            Log.i("htht", "list.get(0).getUserIcon(): " + dynamicItem.getWriter().getUserIcon().getFileUrl());
            Glide.with(mContext)
                    .load(dynamicItem.getWriter().getUserIcon().getFileUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .dontAnimate()
                    .error(R.mipmap.ic_launcher)
                    .into(viewHolder.write_photo);
        } else {
            viewHolder.write_photo.setImageResource(R.mipmap.ic_launcher);
        }
        viewHolder.write_name.setText(dynamicItem.getWriter().getStuName());

        viewHolder.write_date.setText(dynamicItem.getCreatedAt());
        holder.dynamic_text.setText(dynamicItem.getText());
        holder.dynamic_photo.setAdapter(new DynamicPhotoAdapter(mContext,R.layout.dynamic_gridview_item,dynamicItem.getPhotoList()));
        return convertView;
    }

    private final class ViewHolder {
        CircleImageView write_photo;
        TextView write_name;
        TextView write_date;
        TextView dynamic_text;
        FixedGridView dynamic_photo;
    }
}
