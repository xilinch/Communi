package com.ht.communi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ht.communi.activity.R;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * 作用：朋友圈的adapter
 */
public class DynamicPhotoAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<BmobFile> mDatas;
    private Context context;
    private int mLayoutRes;

    public DynamicPhotoAdapter(Context context,int LayoutId, List<BmobFile> datas) {
        this.mLayoutRes=LayoutId;
        this.mDatas = datas;
        this.context = context;
        mInflater = LayoutInflater.from(context);
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
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_add_photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BmobFile bmobFile = mDatas.get(position);
        Glide.with(context)
                .load(bmobFile.getUrl())
                .placeholder(R.drawable.xiaolian)
                .error(R.drawable.xiaolian)
                .centerCrop()
                .into(holder.imageView);
        return convertView;
    }

    private final class ViewHolder {
        public ImageView imageView;
    }
}
