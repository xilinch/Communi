package com.ht.communi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.ht.communi.activity.R;
import com.ht.communi.javabean.EventItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NORMAL_ITEM = 0;
    private static final int GROUP_ITEM = 1;

    private Context mContext;
    private List<EventItem> mDataList;
    private LayoutInflater mLayoutInflater;

    public EventAdapter(Context mContext, List<EventItem> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setDatas(List<EventItem> mDatas) {
        mDataList.clear();
        mDataList.addAll(mDatas);
    }

    /**
     * 渲染具体的ViewHolder
     *
     * @param parent   ViewHolder的容器
     * @param viewType 一个标志，我们根据该标志可以实现渲染不同类型的ViewHolder
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NORMAL_ITEM) {
            return new NormalItemHolder(mLayoutInflater.inflate(R.layout.event_cardview_item, parent, false));
        } else {
            return new GroupItemHolder(mLayoutInflater.inflate(R.layout.event_cardview_title_item, parent, false));
        }
    }


    /**
     * 绑定ViewHolder的数据。
     *
     * @param viewHolder
     * @param i          数据源list的下标
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        final EventItem entity = mDataList.get(i);
        if (null == entity)
            return;

        if (viewHolder instanceof GroupItemHolder) {
            GroupItemHolder holder = (GroupItemHolder) viewHolder;
            holder.eventTitle.setText(entity.getEventName());
            holder.eventDescription.setText(entity.getEventContent());
            holder.eventPlace.setText(entity.getEventPlace());
            holder.eventStart.setText(entity.getEventStart().getDate().toString());
            holder.eventEnd.setText(entity.getEventEnd().getDate().toString());
            holder.eventTime.setText(FriendlyDate(entity.getEventStart().getDate()));
            if (entity.getCommunity() != null) {
                holder.eventComm.setText(entity.getCommunity().getCommName());
            }

        } else if (viewHolder instanceof NormalItemHolder) {
            NormalItemHolder holder = (NormalItemHolder) viewHolder;
            holder.eventTitle.setText(entity.getEventName());
            holder.eventDescription.setText(entity.getEventContent());
            holder.eventPlace.setText(entity.getEventPlace());
            holder.eventStart.setText(entity.getEventStart().getDate().toString());
            holder.eventEnd.setText(entity.getEventEnd().getDate().toString());
            if (entity.getCommunity() != null) {
                holder.eventComm.setText(entity.getCommunity().getCommName());
            }
        }
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 决定元素的布局使用哪种类型
     *
     * @param position 数据源List的下标
     * @return 一个int型标志，传递给onCreateViewHolder的第二个参数
     */
    @Override
    public int getItemViewType(int position) {
        //第一个要显示时间
        if (position == 0)
            return GROUP_ITEM;

        String currentDate = mDataList.get(position).getEventStart().getDate();
        Log.i("htht", "currentDate: " + currentDate + "          position:" + position);
        Log.i("htht", "currentDate: " + currentDate + "          position:" + position);
        int prevIndex = position - 1;
        int isDifferent = daysOfTwo(strToDate(mDataList.get(prevIndex).getEventStart().getDate()), strToDate(currentDate));
//        boolean isDifferent = !mDataList.get(prevIndex).getEventStart().getDate().equals(currentDate);
        return isDifferent != 0 ? GROUP_ITEM : NORMAL_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 新闻标题
     */
    public class NormalItemHolder extends RecyclerView.ViewHolder {
        TextView eventTitle;
        ReadMoreTextView eventDescription;
        LinearLayout eventRoot;
        TextView eventPlace;
        TextView eventStart;
        TextView eventEnd;
        TextView eventComm;


        public NormalItemHolder(View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.event_item_title);
            eventDescription = itemView.findViewById(R.id.event_item_content);
            eventRoot = itemView.findViewById(R.id.event_item_container);
            eventPlace = itemView.findViewById(R.id.event_item_place);
            eventStart = itemView.findViewById(R.id.event_item_start_time);
            eventEnd = itemView.findViewById(R.id.event_item_end_time);
            eventComm = itemView.findViewById(R.id.event_item_comm);
        }
    }


    /**
     * 带日期新闻标题
     */
    public class GroupItemHolder extends NormalItemHolder {
        TextView eventTime;

        public GroupItemHolder(View itemView) {
            super(itemView);
            eventTime = (TextView) itemView.findViewById(R.id.event_group_time);
        }
    }


    public static int daysOfTwo(Date originalDate, Date compareDateDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(originalDate);
        int originalDay = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(compareDateDate);
        int compareDay = aCalendar.get(Calendar.DAY_OF_YEAR);

        return originalDay - compareDay;
    }

    public static String FriendlyDate(String strDate) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
//        Date date = new Date();
//        Date nowDate = new Date();
//        try {
//            date = sdf.parse(strDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        Date nowDate = new Date();
        Date date = strToDate(strDate);
        int dayDiff = daysOfTwo(nowDate, date);
        if (dayDiff == 0) {
            return "今日";
        } else if (dayDiff < 0) {
            return Math.abs(dayDiff) + "天后";
        } else {
            return Math.abs(dayDiff) + "天前";
        }
    }

    //将字符串转换为Date
    public static Date strToDate(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
        Date date = new Date();
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
