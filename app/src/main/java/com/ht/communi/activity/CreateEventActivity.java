package com.ht.communi.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ht.communi.javabean.CommunityItem;
import com.ht.communi.javabean.EventItem;
import com.ht.communi.model.EventModel;
import com.ht.communi.model.impl.EventModelImpl;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.datatype.BmobDate;

public class CreateEventActivity extends AppCompatActivity implements OnDateSetListener {

    private EditText ed_event_name;
    private EditText ed_event_description;
    private EditText ed_event_place;
    private TextView tv_font_count;
    private Button btn_create_event;
    private Context context;

    private SimpleDateFormat sdf_show = new SimpleDateFormat("MM-dd HH:mm");
    private TimePickerDialog mDialogAll;
    private EditText ed_event_star_time;
    private EditText ed_event_end_time;

    private Date start_date;
    private Date end_date;
    private TextInputLayout til_event_star_time;
    private TextInputLayout til_event_end_time;
    private CommunityItem communityItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_event);
        context = this;
        initView();

        //获得当前社团
        communityItem = (CommunityItem) getIntent().getSerializableExtra("CREATE_COMM_EVENT");

        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("选择日期")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(12)
                .build();
    }

    private void initView() {
        ed_event_name = findViewById(R.id.ed_event_name);
        ed_event_description = findViewById(R.id.ed_event_description);
        ed_event_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_font_count.setText(s.length() + "/1000");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ed_event_place = findViewById(R.id.ed_event_place);
        tv_font_count = findViewById(R.id.tv_font_count);
        btn_create_event = findViewById(R.id.btn_create_event);
        btn_create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
            }
        });
        til_event_star_time = findViewById(R.id.til_event_star_time);
        til_event_end_time = findViewById(R.id.til_event_end_time);

        ed_event_star_time = findViewById(R.id.ed_event_star_time);
        ed_event_star_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("htht", "mDialogAll.isResumed(): " + mDialogAll.isResumed());
                if (!mDialogAll.isResumed()) {
                    mDialogAll.show(getSupportFragmentManager(), "start");
                }
            }
        });
        ed_event_end_time = findViewById(R.id.ed_event_end_time);
        ed_event_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDialogAll.isResumed()) {
                    mDialogAll.show(getSupportFragmentManager(), "end");
                }
            }
        });
    }


    private void createEvent() {
        if (!validate()) {
            onSignupFailed();
            Log.i("htht", "createEvent: 直接退出了");
            return;
        }

        btn_create_event.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(context,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("创建社团活动中...");
        progressDialog.show();

        String name = ed_event_name.getText().toString();
        String place = ed_event_place.getText().toString();
        String description = ed_event_description.getText().toString();

        EventItem eventItem = new EventItem();

        if (communityItem != null) {
            eventItem.setCommunity(communityItem);
        }
        eventItem.setEventContent(description);
        eventItem.setEventName(name);
        eventItem.setEventPlace(place);
        if (start_date != null) {
            eventItem.setEventStart(new BmobDate(start_date));
        }
        if (end_date != null) {
            eventItem.setEventEnd(new BmobDate(end_date));
        }

        new EventModel().addEventItem(eventItem, new EventModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                onSignupSuccess();
            }

            @Override
            public void getFailure() {
                onSignupFailed();
            }
        });
    }

    private boolean validate() {
        boolean valid = true;
        String name = ed_event_name.getText().toString();
        String place = ed_event_place.getText().toString();
        String description = ed_event_description.getText().toString();
        String start_time = ed_event_star_time.getText().toString();
        String end_time = ed_event_end_time.getText().toString();

        if (name.isEmpty() || name.length() > 20) {
            ed_event_name.setError("请输入小于20位社团主题名");
            valid = false;
        } else {
            ed_event_name.setError(null);
        }

        if (place.isEmpty()) {
            ed_event_place.setError("请输入活动地点");
            valid = false;
        } else {
            ed_event_place.setError(null);
        }

        if (description.isEmpty()) {
            ed_event_description.setError("请输入活动详细描述");
            valid = false;
        } else {
            ed_event_description.setError(null);
        }

        if (start_time.isEmpty()) {
            til_event_star_time.setError("请输入活动开始时间");
            valid = false;
        } else {
            til_event_star_time.setError(null);
        }

        if (end_time.isEmpty()) {
            til_event_end_time.setError("请输入活动结束时间");
            valid = false;
        } else {
            til_event_end_time.setError(null);
        }

        if (!start_time.isEmpty() && !end_time.isEmpty() && start_time.compareTo(end_time) > 0.) {
            til_event_star_time.setError("起止时间颠倒");
            til_event_end_time.setError("起止时间颠倒");
            valid = false;
        }

        return valid;
    }

    private void onSignupSuccess() {
        btn_create_event.setEnabled(true);
        finish();
    }

    private void onSignupFailed() {
        btn_create_event.setEnabled(true);
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerDialog, long millseconds) {
        String text = getDateToString(millseconds);
        String tag = timePickerDialog.getTag();
        if (tag.equals("start")) {
            start_date = new Date(millseconds);
            ed_event_star_time.setText(text);
        } else {
            end_date = new Date(millseconds);
            ed_event_end_time.setText(text);
        }
    }

    public String getDateToString(long time) {
        Date d = new Date(time);
        return sdf_show.format(d);
    }
}
