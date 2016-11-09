package com.example.k.zhihudaily.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.k.zhihudaily.MainActivity;
import com.example.k.zhihudaily.R;
import com.example.k.zhihudaily.constants.MethodURL;
import com.example.k.zhihudaily.response.SplashResponse;
import com.google.gson.Gson;

/**
 * Created by K on 2016/11/7.
 */

public class SplashActivity extends Activity {

    private ImageView splashIv;
    private TextView authorTv;

    private RequestQueue requestQueue;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        requestQueue = Volley.newRequestQueue(this);
        initView();
        initHandler();
        getData();
    }

    private void initView() {
        splashIv = (ImageView) findViewById(R.id.splash_iv);
        authorTv = (TextView) findViewById(R.id.splash_author_tv);
        getData();
    }

    private void initHandler(){
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1){
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    private void getData() {
        StringRequest request = new StringRequest(MethodURL.GET_START_IMAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("开始请求","");
                        handleData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("请求启动页失败","");
            }
        });
        requestQueue.add(request);
    }

    private void handleData(String jsonData){
        Log.d("请求得到的数据是",jsonData);
        Gson gson = new Gson();
        SplashResponse splashResponse = gson.fromJson(jsonData,SplashResponse.class);
        if (splashResponse == null){
            Log.d("启动页","处理得到的json为空");
            return;
        }
        Glide.with(SplashActivity.this)
                .load(splashResponse.getImg())
                .into(splashIv);
        authorTv.setText(splashResponse.getText());

        //跳转到主页面
        handler.sendEmptyMessageDelayed(1,2000);
    }
}
