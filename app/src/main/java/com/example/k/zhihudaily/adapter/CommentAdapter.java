package com.example.k.zhihudaily.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.k.zhihudaily.R;
import com.example.k.zhihudaily.entity.CommentBean;
import com.example.k.zhihudaily.view.RoundImageView;

import java.util.List;

/**
 * Created by K on 2016/11/9.
 */

public class CommentAdapter extends ArrayAdapter{

    private Context context;
    private List<CommentBean> list;

    private static final int CATEGORY = -1;
    private static final int NORMAL = 0;

    public CommentAdapter(Context context, int resource, List<CommentBean> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        CommentBean comment = list.get(position);
        if (getItemViewType(position) == CATEGORY){
            view = LayoutInflater.from(context).inflate(R.layout.item_comment_category,parent,false);
            TextView count = (TextView)view.findViewById(R.id.item_comment_category_count);
            //长评论
            if (position == 0){
                count.setText(comment.getLikes()+" 条长评");
            } else {
                count.setText(comment.getLikes()+" 条短评");
            }
            return view;
        }
        if (convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
            holder = new ViewHolder();
            holder.headIcon = (RoundImageView)view.findViewById(R.id.item_comment_head_iv);
            holder.nameTV = (TextView)view.findViewById(R.id.item_comment_name_tv);
            holder.contentTV = (TextView)view.findViewById(R.id.item_comment_content_tv);
            holder.timeTV = (TextView)view.findViewById(R.id.item_comment_date_tv);
            holder.likeIV = (ImageView)view.findViewById(R.id.item_comment_like_iv);
            holder.likeCountTV = (TextView)view.findViewById(R.id.item_comment_like_tv);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.nameTV.setText(comment.getAuthor());
        holder.contentTV.setText(comment.getContent());
        holder.timeTV.setText(comment.getTime()+"");
        holder.likeCountTV.setText(comment.getLikes()+"");
        Glide.with(context).load(comment.getAvatar())
                .into(holder.headIcon);

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getId() == -1){
            return CATEGORY;
        }
        return NORMAL;
    }

    class ViewHolder{
        RoundImageView headIcon;
        TextView nameTV,contentTV,timeTV,likeCountTV;
        ImageView likeIV;
    }
}
