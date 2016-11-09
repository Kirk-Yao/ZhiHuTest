package com.example.k.zhihudaily;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.k.zhihudaily.fragment.HomepageFragment;
import com.example.k.zhihudaily.fragment.OthersFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;

    //fragment相关
    private Fragment fragment;
    private android.app.FragmentManager fragmentManager;
    private int currPosition = -1;
    private int[] themes = {13,12,3,11,4,5,6,10,2,7,9,8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initData(){
        fragmentManager = getFragmentManager();
    }

    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.nav_home:
                setFragment(0);
                break;
            case R.id.nav_daily_psychology:
                setFragment(1);
                break;
            case R.id.nav_user_rec:
                setFragment(2);
                break;
            case R.id.nav_daily_movie:
                setFragment(3);
                break;
            case R.id.nav_no_boring:
                setFragment(4);
                break;
            case R.id.nav_daily_design:
                setFragment(5);
                break;
            case R.id.nav_daily_big_company:
                setFragment(6);
                break;
            case R.id.nav_daily_finance:
                setFragment(7);
                break;
            case R.id.nav_internet_safe:
                setFragment(8);
                break;
            case R.id.nav_start_game:
                setFragment(9);
                break;
            case R.id.nav_daily_music:
                setFragment(10);
                break;
            case R.id.nav_daily_comic:
                setFragment(11);
                break;
            case R.id.nav_daily_sport:
                setFragment(12);
                break;
        }
        if (id == R.id.nav_home){
            setFragment(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(int position){
        if (currPosition == position){
            return;
        }
        android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        //需要先隐藏
        if (fragment != null){
            transaction.hide(fragment);
        }
        fragment = new HomepageFragment();
        currPosition = position;
        if (position != 0){
            fragment = new OthersFragment();
            Bundle bundle = new Bundle();
            bundle.putLong("ID",themes[position-1]);
            fragment.setArguments(bundle);
        }
        transaction.add(R.id.content_main_ll,fragment);
        transaction.show(fragment);
        transaction.commit();
    }
}
