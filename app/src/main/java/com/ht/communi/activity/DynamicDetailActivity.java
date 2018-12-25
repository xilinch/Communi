package com.ht.communi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ht.communi.adapter.DynamicPhotoAdapter;
import com.ht.communi.customView.CircleImageView;
import com.ht.communi.customView.FixedListView;
import com.ht.communi.javabean.DynamicItem;
import com.ht.communi.javabean.Student;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 作用：朋友圈详细
 */
public class DynamicDetailActivity extends Activity {
    private CircleImageView writePhoto;
    private TextView writeName;
    private TextView writeDate;
    private TextView dynamicText;
    private FixedListView dynamicPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dynamicdetail_activity_main);
        initView();

        DynamicItem dynamicItem = (DynamicItem) getIntent().getSerializableExtra("DYNAMIC");
        BmobQuery<Student> query = new BmobQuery<>();
        Log.i("htht", "dynamicItem.getWriter().getObjectId(): "+dynamicItem.getWriter().getObjectId());
        query.addWhereEqualTo("objectId", dynamicItem.getWriter().getObjectId());
        query.setLimit(1);
        query.findObjects(new FindListener<Student>() {
            @Override
            public void done(List<Student> list, BmobException e) {
                if(e ==null){
                    if(list != null && list.size() != 0){
                        Log.i("htht", "list.get(0).getStuName(): "+list.get(0).getStuName());
                        writeName.setText(list.get(0).getStuName());
                        if(list.get(0).getUserIcon() != null) {
                            Glide.with(DynamicDetailActivity.this)
                                    .load(list.get(0).getUserIcon().getFileUrl())
                                    .placeholder(R.mipmap.ic_launcher)
                                    .error(R.mipmap.ic_launcher)
                                    .into(writePhoto);
                        }
                    }
                }else{
                    Log.i("htht", "DynamicAdapter...e.getErrorCode()=== "+e.getErrorCode()+"==="+e.getMessage());
                }
            }
        });


        writeDate.setText(dynamicItem.getCreatedAt());
        dynamicText.setText(dynamicItem.getText());
        dynamicPhoto.setAdapter(new DynamicPhotoAdapter(DynamicDetailActivity.this, R.layout.dynamicdetail_listview_item, dynamicItem.getPhotoList()));
    }

    private void initView(){
        ImageView back = findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        writePhoto = findViewById(R.id.cv_write_photo);
        writeName = findViewById(R.id.tv_write_name);
        writeDate = findViewById(R.id.tv_write_date);
        dynamicText = findViewById(R.id.tv_dynamic_text);
        dynamicPhoto = findViewById(R.id.fl_dynamic_photo);
    }

}
