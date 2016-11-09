package com.example.k.zhihudaily.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.k.zhihudaily.R;
import com.example.k.zhihudaily.adapter.CommentAdapter;
import com.example.k.zhihudaily.constants.MethodURL;
import com.example.k.zhihudaily.entity.CommentBean;
import com.example.k.zhihudaily.response.CommentResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by K on 2016/11/9.
 */

public class CommentsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private ImageView backIV,addCommentIV;
    private TextView allCommentTV;

    private Context context;
    private RequestQueue requestQueue;
    private long newsId;
    private int commentCount;
    private int shortCommentCount;
    private List<CommentBean> commentList;

    private CommentAdapter adapter;

    private static final String LONG_COMMENTS = "/long-comments";
    private static final String SHORT_COMMENTS = "/short-comments";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_comment);

        initVariable();
        initView();
        loadLongData();
        setListener();
    }

    private void initVariable(){
        newsId = getIntent().getLongExtra("ID",-1);
        commentCount = getIntent().getIntExtra("COMMENT_COUNT",0);
        shortCommentCount = getIntent().getIntExtra("SHORT_COMMENT_COUNT",0);
        context = getBaseContext();
        requestQueue = Volley.newRequestQueue(context);
    }

    private void initView(){
        toolbar = (Toolbar)findViewById(R.id.activity_comment_toolbar);
        listView = (ListView)findViewById(R.id.activity_comment_lv);
        backIV = (ImageView)findViewById(R.id.activity_comment_back_iv);
        allCommentTV = (TextView)findViewById(R.id.activity_comment_all_comment_tv);
        addCommentIV = (ImageView)findViewById(R.id.activity_comment_add_comment_iv);

        allCommentTV.setText(commentCount+" 条点评");
    }

    private void loadLongData(){
        //获取长评论
        StringRequest longRequest = new StringRequest(MethodURL.COMMENT + newsId + LONG_COMMENTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleLongData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(longRequest);
    }

    private void handleLongData(String jsonData){
        Gson gson = new Gson();
        CommentResponse response = gson.fromJson(jsonData,CommentResponse.class);
        if (response != null){
            //为空，则添加第一条长评论总结
            if (commentList == null){
                commentList = new ArrayList<>();
                CommentBean commentBean = new CommentBean();
                commentBean.setId(-1);
                commentBean.setLikes(response.getComments().size());
                commentList.add(commentBean);
            }
            commentList.addAll(response.getComments());
            //添加短评数量
            CommentBean commentBean = new CommentBean();
            commentBean.setId(-1);
            commentBean.setLikes(shortCommentCount);
            commentList.add(commentBean);

            //设置adapter
            adapter = new CommentAdapter(context,1,commentList);
            listView.setAdapter(adapter);
        }
    }

    private void loadShortData(){
        //获取短评论
        StringRequest shortRequest = new StringRequest(MethodURL.COMMENT + newsId + SHORT_COMMENTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleShortData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(shortRequest);
    }

    private void handleShortData(String jsonData){
        Gson gson = new Gson();
        CommentResponse response = gson.fromJson(jsonData,CommentResponse.class);
        if (response != null){
            commentList.addAll(response.getComments());
            adapter.notifyDataSetChanged();
        }
    }

    private void setListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //判断是否点击展开短评
                CommentBean commentBean = commentList.get(i);
                if (i == (commentList.size()-1) && commentBean.getId() == -1){
                    loadShortData();
                }
            }
        });
    }
}
