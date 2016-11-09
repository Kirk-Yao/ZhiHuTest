package com.example.k.zhihudaily.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.k.zhihudaily.R;
import com.example.k.zhihudaily.constants.MethodURL;
import com.example.k.zhihudaily.response.NewsDetailResponse;
import com.example.k.zhihudaily.response.NewsExtraInfoResponse;
import com.google.gson.Gson;

/**
 * Created by K on 2016/11/8.
 */

public class NewsDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imageIv;
    private TextView titleTv;
    private TextView picFromTv;
    private WebView webView;

    private ImageView commentIV,likeIV;
    private TextView commentTV,likeTV;

    private RequestQueue requestQueue;
    private Context context;

    private long newsId;

    private int commentCount = 0;
    private int longCommentCount = 0;
    private int shortCommentCount = 0;


    private static final String TAG = "newsDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);

        initVariable();
        initView();
        initToolbar();
        loadData();
        loadExtraInfo();
        setListener();
    }

    private void initVariable(){
        newsId = getIntent().getLongExtra("ID",-1);
        context = getBaseContext();
        requestQueue = Volley.newRequestQueue(context);
    }

    private void initView(){
        toolbar = (Toolbar)findViewById(R.id.activity_news_detail_toolbar);
        imageIv = (ImageView)findViewById(R.id.header_news_detail_iv);
        titleTv = (TextView)findViewById(R.id.header_news_detail_title_tv);
        picFromTv = (TextView)findViewById(R.id.header_news_detail_from_tv);
        webView = (WebView)findViewById(R.id.activity_news_detail_webView);

        commentTV = (TextView) findViewById(R.id.activity_news_detail_comment_tv);
        commentIV = (ImageView)findViewById(R.id.activity_news_detail_comment_iv);
        likeTV = (TextView)findViewById(R.id.activity_news_detail_like_tv);
        likeIV = (ImageView)findViewById(R.id.activity_news_detail_like_iv);

    }

    private void initToolbar(){
        //需在设置toolbar之前调用
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //返回上级按钮
//        toolbar.setNavigationIcon(R.drawable.back_white);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
    }

    private void setListener(){
        commentIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,CommentsActivity.class);
                intent.putExtra("ID",newsId);
                intent.putExtra("COMMENT_COUNT",commentCount);
                intent.putExtra("SHORT_COMMENT_COUNT",shortCommentCount);
                startActivity(intent);
            }
        });
    }

    private void loadData(){
        StringRequest request = new StringRequest(MethodURL.NEWS_DETAIL + newsId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,response);
                        handleData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    private void handleData(String jsonData){
        Gson gson = new Gson();
        NewsDetailResponse response = gson.fromJson(jsonData,NewsDetailResponse.class);
        if (response == null){
            Log.d(TAG,"处理得到的数据为空");
            return;
        }
        Glide.with(context).load(response.getImage())
                .into(imageIv);
        titleTv.setText(response.getTitle());
        picFromTv.setText(response.getImage_source());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
//        webView.loadData(response.getBody(),null,"utf-8");
        webView.loadDataWithBaseURL(null,response.getCss().get(0)+response.getBody(),"text/html","utf-8",null);
    }

    private void loadExtraInfo(){
        StringRequest request = new StringRequest(MethodURL.NEWS_EXTRA_INFO + newsId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleExtraInfo(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    private void handleExtraInfo(String jsonData){
        Gson gson = new Gson();
        NewsExtraInfoResponse response = gson.fromJson(jsonData,NewsExtraInfoResponse.class);
        if (response != null){
            commentTV.setText(response.getComments()+"");
            likeTV.setText(response.getPopularity()+"");
            commentCount = response.getComments();
            longCommentCount = response.getLong_comments();
            shortCommentCount = response.getShort_comments();
        }
    }
}
