package com.example.k.zhihudaily.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.k.zhihudaily.R;
import com.example.k.zhihudaily.entity.StoryBean;

import java.util.List;

/**
 * Created by K on 2016/11/7.
 */

public class HomeListViewAdapter extends ArrayAdapter {

    private Context context;
    private List<StoryBean> storiesList;
    private int resourceId;

    private final int TYPE_DATE = -1;
    private final int TYPE_NORMAL = 0;

    public HomeListViewAdapter(Context context, int resource, List<StoryBean> list) {
        super(context, resource, list);
        this.context = context;
        this.storiesList = list;
        this.resourceId = resource;
    }


    @Override
    public int getCount() {
        return storiesList.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StoryBean story = storiesList.get(position);

        if (getItemViewType(position) == TYPE_DATE){
            View view = LayoutInflater.from(context).inflate(R.layout.item_home_fragment_lv_date,null);
            TextView date = (TextView) view.findViewById(R.id.item_home_date_tv);
            date.setText(story.getTitle());
            return view;
        }

        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(context).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.titleTv = (TextView) view.findViewById(R.id.item_home_title_tv);
            viewHolder.picIv = (ImageView)view.findViewById(R.id.item_home_pic_iv);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.titleTv.setText(story.getTitle());
        Glide.with(context).load(story.getImages().get(0))
                .into(viewHolder.picIv);
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return storiesList.get(position).getType();
    }

    class ViewHolder{
        TextView titleTv;
        ImageView picIv;
    }
}
