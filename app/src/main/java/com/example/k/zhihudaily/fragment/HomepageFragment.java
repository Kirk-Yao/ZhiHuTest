package com.example.k.zhihudaily.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.k.zhihudaily.R;
import com.example.k.zhihudaily.activity.NewsDetailActivity;
import com.example.k.zhihudaily.adapter.HeaderPagerAdapter;
import com.example.k.zhihudaily.adapter.HomeListViewAdapter;
import com.example.k.zhihudaily.constants.MethodURL;
import com.example.k.zhihudaily.entity.StoryBean;
import com.example.k.zhihudaily.entity.TopStoryBean;
import com.example.k.zhihudaily.response.BeforeNewsResponse;
import com.example.k.zhihudaily.response.LatestNewsResponse;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by K on 2016/11/7.
 */

public class HomepageFragment extends Fragment {

    private ListView listView;
    private SwipeRefreshLayout refreshLayout;

    //listView相关
    private ViewPager headerPager;
    private LinearLayout dotLL;
    private HeaderPagerAdapter pagerAdapter;
    private List<View> viewList;
    private List<StoryBean> storyList;
    private HomeListViewAdapter listViewAdapter;

    //listView高度
    private int listViewHeight;

    private Context context;
    private RequestQueue requestQueue;

    //获取消息时的标志日期
    private String newsDate;

    private static final String TAG = "homeFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        initVaribles();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_homepage,container,false);
        initView(view);
        initListView();
        setListener();
        getData();
        return view;
    }

    private void initVaribles(){
        requestQueue = Volley.newRequestQueue(context);
        viewList = new ArrayList<>();
    }

    private void initView(View view){
        listView = (ListView)view.findViewById(R.id.home_fragment_listView);
        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.home_fragment_refresh_srl);
    }

    private void initListView(){
        if (listView.getHeaderViewsCount() == 0) {
            View lvHeader = LayoutInflater.from(context).inflate(R.layout.main_listview_header, null);
            listView.addHeaderView(lvHeader);
            headerPager = (ViewPager)lvHeader.findViewById(R.id.main_viewPager);
        }
    }

    private void setListener(){

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("ID",storyList.get(i-1).getId());
                Log.d(TAG,storyList.get(i-1).getId()+" sdfa");
                startActivity(intent);
            }
        });

        //获取listView的高度
        listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                listViewHeight = listView.getHeight();

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        //监听滑动到底部
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if ((i + i1) == i2){
                    View lastVisibleItem = listView.getChildAt(listView.getChildCount()-1);
                    if (lastVisibleItem != null && lastVisibleItem.getBottom() == listViewHeight){
                        Log.d(TAG,"滚动到底部");
                        getBeforeNews(newsDate);
                    }
                }
            }
        });
    }

    //获取以前的消息
    private void getBeforeNews(String date){
        StringRequest request = new StringRequest(MethodURL.HOME_BEFORE_NEWS + date,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleBeforeData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    private void handleBeforeData(String jsonData){
        Gson gson = new Gson();
        BeforeNewsResponse beforeNewsResponse = gson.fromJson(jsonData,BeforeNewsResponse.class);
        if (beforeNewsResponse != null){
            newsDate = beforeNewsResponse.getDate();
            //添加日期item,type -1表示该条是一个日期
            StoryBean storyBean = new StoryBean();
            storyBean.setType(-1);
            String tempDate = newsDate.substring(4,6)+"月"+newsDate.substring(6)+"日";
            Log.d(TAG,"日期是"+tempDate);
            storyBean.setTitle(tempDate);

            List<StoryBean> list = beforeNewsResponse.getStories();
            list.add(0,storyBean);

            storyList.addAll(list);
            listViewAdapter.notifyDataSetChanged();
        }
    }

    //从网络获取数据
    private void getData(){
        StringRequest request = new StringRequest(MethodURL.HOME_LAST_NEWS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"请求到的数据是:"+response);
                        handleLatestData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"请求数据出错",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }

    //处理获取到的数据
    private void handleLatestData(String jsonData){

        Gson gson = new Gson();

        LatestNewsResponse latestNewsResponse = gson.fromJson(jsonData,LatestNewsResponse.class);
        if (latestNewsResponse == null){
            Log.d(TAG,"处理得到的数据为空");
            return;
        }
        newsDate = latestNewsResponse.getDate();
        bindData2ListView(latestNewsResponse);
        bindData2Pager(latestNewsResponse);
    }

    private void bindData2ListView(LatestNewsResponse latestNewsResponse){
        storyList = latestNewsResponse.getStories();
        //设置日期item
        StoryBean storyBean = new StoryBean();
        storyBean.setType(-1);
        storyBean.setTitle("今日热闻");
        storyList.add(0,storyBean);
        //设置listView
        listViewAdapter = new HomeListViewAdapter(context,R.layout.item_home_fragment_lv,storyList);
        listView.setAdapter(listViewAdapter);

        //刷新完毕
        if (refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
    }

    //设置header的viewPager
    private void bindData2Pager(LatestNewsResponse latestNewsResponse){
        final List<TopStoryBean> topStoryList = latestNewsResponse.getTop_stories();
        viewList.clear();
        for (int i = 0; i < topStoryList.size(); i++){
            View v = LayoutInflater.from(context).inflate(R.layout.item_header_pager,null);
            /*if (dotLL == null){
                dotLL = (LinearLayout)v.findViewById(R.id.header_dot_ll);
            }
            ImageView iv = new ImageView(context);
            iv.setImageDrawable(getResources().getDrawable(R.drawable.dot_white));
            dotLL.addView(iv);*/

            ImageView picIv = (ImageView)v.findViewById(R.id.item_pager_pic_iv);
            TextView infoTv = (TextView)v.findViewById(R.id.item_pager_title_tv);

            infoTv.setText(topStoryList.get(i).getTitle());
            Glide.with(context).load(topStoryList.get(i).getImage())
                    .into(picIv);
            viewList.add(v);
            final int finalI = i;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,NewsDetailActivity.class);
                    intent.putExtra("ID",topStoryList.get(finalI).getId());
                    startActivity(intent);
                }
            });
        }
        pagerAdapter = new HeaderPagerAdapter(viewList);
        headerPager.setAdapter(pagerAdapter);
        for (int i = 0; i < topStoryList.size(); i++){

        }
    }
}
