package com.example.k.zhihudaily.fragment;

import android.app.Fragment;
import android.content.Context;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.k.zhihudaily.R;
import com.example.k.zhihudaily.adapter.OtherListViewAdapter;
import com.example.k.zhihudaily.constants.MethodURL;
import com.example.k.zhihudaily.entity.ThemeStoryBean;
import com.example.k.zhihudaily.response.MoreThemeNewsResponse;
import com.example.k.zhihudaily.response.OtherThemeResponse;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by K on 2016/11/8.
 */

public class OthersFragment extends Fragment {

    private ListView listView;
    private SwipeRefreshLayout refreshLayout;

    private View headerView;
    private ImageView headerBackIv;
    private TextView headerTitleTv;

    private RequestQueue requestQueue;
    private Context context;

    private OtherListViewAdapter adapter;

    private int listViewHeight;
    private long themeId;

    private List<ThemeStoryBean> themeStoryList;

    private static final String TAG = "otherFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_others,container,false);

        initVariables();
        initView(view);
        initListView();
        getData();
        setListener();
        return view;
    }

    private void initVariables(){
        themeId = getArguments().getLong("ID",-1);
        requestQueue = Volley.newRequestQueue(context);
    }

    private void initView(View view){
        listView = (ListView)view.findViewById(R.id.others_fragment_lv);
        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.others_fragment_srl);
    }

    private void initListView(){
        headerView = LayoutInflater.from(context).inflate(R.layout.header_lv_others_fragment,null);
        if(listView.getHeaderViewsCount() == 0){
            listView.addHeaderView(headerView);
        }
        headerBackIv = (ImageView)headerView.findViewById(R.id.others_fragment_piv_iv);
        headerTitleTv = (TextView) headerView.findViewById(R.id.others_fragment_title_tv);
    }

    private void setListener(){
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
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
                        getMoreData(themeId,themeStoryList.get(themeStoryList.size()-1).getId());
                        Log.d(TAG,"请求的id是： "+themeStoryList.get(themeStoryList.size()-1).getId());
                    }
                }
            }
        });
    }

    private void getData(){
        StringRequest request = new StringRequest(MethodURL.NEWS_THEME + themeId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,themeId+" "+response);
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
        OtherThemeResponse response = gson.fromJson(jsonData,OtherThemeResponse.class);
        if (response != null){
            bindData2ListView(response);
            bindData2Header(response);
            if (refreshLayout.isRefreshing()){
                refreshLayout.setRefreshing(false);
            }
        }
    }

    private void bindData2ListView(OtherThemeResponse response){
        themeStoryList = response.getStories();
        adapter = new OtherListViewAdapter(context,R.layout.item_home_fragment_lv,themeStoryList);
        listView.setAdapter(adapter);
    }

    private void bindData2Header(OtherThemeResponse response){
        headerTitleTv.setText(response.getName());
        Glide.with(context).load(response.getImage())
                .into(headerBackIv);
    }

    //获取更多数据
    private void getMoreData(long themeId, long newsId){
        StringRequest request = new StringRequest(MethodURL.MORE_THEME_NEWS + themeId + "/before/" + newsId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"加载更多的结果是："+response);
                        handleMoreData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    private void handleMoreData(String jsonData){
        Gson gson = new Gson();
        MoreThemeNewsResponse response = gson.fromJson(jsonData,MoreThemeNewsResponse.class);
        if (response != null){
            Log.d(TAG,"长度是："+response.getStories().size());
            List<ThemeStoryBean> stories = response.getStories();
            if (!themeStoryList.containsAll(stories)){
                themeStoryList.addAll(stories);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
