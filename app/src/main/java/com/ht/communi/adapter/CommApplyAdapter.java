package com.ht.communi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ht.communi.activity.R;
import com.ht.communi.javabean.Student;

import java.util.List;

/**
 * Created by Administrator on 2018/5/13.
 */

public class CommApplyAdapter extends BaseAdapter {
    private Context mContext;
    private List<Student> mDataList;

    public CommApplyAdapter(Context mContext, List<Student> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    public void setDatas(List<Student> datas){
        mDataList = datas;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Student getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.activity_comm_apply_item, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        Student student = getItem(position);
        if(student.getUserIcon()!= null) {
            Log.i("htht", "list.get(0).getUserIcon(): " + student.getUserIcon().getFileUrl());
            Glide.with(mContext)
                    .load(student.getUserIcon().getFileUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .dontAnimate()
                    .error(R.mipmap.ic_launcher)
                    .into(holder.iv_icon);
        } else {
            holder.iv_icon.setImageResource(R.mipmap.ic_launcher);
        }

        holder.tv_name.setText(student.getStuName());
        holder.iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("htht", "iv_icon onClick: ");
            }
        });
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("htht", "tv_name iv_icon onClick: ");
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;

        public ViewHolder(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(this);
        }
    }
}
