package com.example.administrator.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.myapplication.Adapter.MainAdapter;
import com.example.administrator.myapplication.Db.CacheDbHelper;
import com.example.administrator.myapplication.Fragment.MainFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private CacheDbHelper dbHelper;
    private SharedPreferences sp;
    private MainAdapter mAdapter;
    private boolean isLight;
    private String curId;
    private SwipeRefreshLayout sr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        dbHelper = new CacheDbHelper(this,1);
        isLight = sp.getBoolean("isLight",true);
        initImageLoader(getApplicationContext());
        sr = (SwipeRefreshLayout) findViewById(R.id.sr);
        loadLatest();


        initView();

    }

    private void initView() {
        sr = (SwipeRefreshLayout) findViewById(R.id.sr);
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /** 下拉刷新数据源*/
                replaceFragment();
                sr.setRefreshing(false);
            }
        });
    }

    public void replaceFragment() {
        if (curId.equals("latest")) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_content, new MainFragment(), "latest")
                    .commit();
        } else {

        }

    }
    /**  */
    public void setSwipeRefreshEnable(boolean enable) {

        sr.setEnabled(enable);
    }

    private void loadLatest() {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fl_content,new MainFragment(),"latest")
                .commit();
        curId = "latest";
    }

    private void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(config);
    }

    public boolean isLight() {
        return isLight;
    }

    public CacheDbHelper getCacheDbHelper() {
        return dbHelper;
    }



}
