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
import com.example.k.zhihudaily.entity.ThemeStoryBean;

import java.util.List;
import java.util.TimerTask;

/**
 * Created by K on 2016/11/8.
 */

public class OtherListViewAdapter extends ArrayAdapter {

    private Context context;
    private List<ThemeStoryBean> list;
    private int resourceId;

    public OtherListViewAdapter(Context context, int resource, List<ThemeStoryBean> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
        this.resourceId = resource;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        ThemeStoryBean themeStory = list.get(position);
        if (convertView == null){
            view = LayoutInflater.from(context).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.picIv = (ImageView)view.findViewById(R.id.item_home_pic_iv);
            viewHolder.titleTv = (TextView)view.findViewById(R.id.item_home_title_tv);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.titleTv.setText(themeStory.getTitle());
        if (themeStory.getImages() != null) {
            Glide.with(context).load(themeStory.getImages().get(0))
                    .into(viewHolder.picIv);
            viewHolder.picIv.setVisibility(View.VISIBLE);
        } else {
            viewHolder.picIv.setVisibility(View.GONE);
        }
        return view;
    }

    class ViewHolder{
        ImageView picIv;;
        TextView titleTv;
    }
}
