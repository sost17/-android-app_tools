package com.example.administrator.myapplication.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.myapplication.Adapter.MainAdapter;
import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.model.Latest;
import com.example.administrator.myapplication.model.StoriesEntity;
import com.example.administrator.myapplication.uilt.Constant;
import com.example.administrator.myapplication.uilt.HttpUtils;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;

/**
 * Created by Administrator on 16-3-20.
 */
public class MainFragment extends BaseFragment {

    private ListView mListView;
    private MainAdapter mAdapter;
    private boolean isLoading = false;
    private String date;
    private Latest latest;
    private Handler handler = new Handler();

    private ImageView mImg;

    @Override
    protected void initData() {
        super.initData();
        loadFirst();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_contnet,container,false);
        mListView = (ListView) view.findViewById(R.id.lv_main);

        View header = inflater.inflate(R.layout.viewimagve,mListView,false);
        mImg = (ImageView) header.findViewById(R.id.title_img);
        mListView.addHeaderView(header);
        mAdapter = new MainAdapter(mActivity);
        mListView.setAdapter(mAdapter);


        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            /**
             * 滚动时调用
             * @param view 视图的滚动
             * @param firstVisibleItem 第一个索引
             * @param visibleItemCount  可见的Item数量
             * @param totalItemCount  适配的数量
             */
            @Override
              public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mListView != null && mListView.getChildCount() > 0) {
                    Boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    ((MainActivity) mActivity).setSwipeRefreshEnable(enable);
                    if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading) {
                        loadMore(Constant.BEFORE + date);
                    }
                }
            }
        });
        return view;
    }

    private void loadMore(String url) {

        isLoading = true;
        if (HttpUtils.isNetworkConnected(mActivity)){
            HttpUtils.get(url, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

                }

                @Override
                public void onSuccess(int i, Header[] headers, String s) {


                    SQLiteDatabase db = ((MainActivity)mActivity).getCacheDbHelper().getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" + date + ",' " + s + "')");
                    db.close();
                    parseLatestJson(s);
                }
            });
        }else {
            SQLiteDatabase db = ((MainActivity)mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + date, null);
            if (cursor.moveToFirst()){
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseLatestJson(json);
            }else {
                cursor.close();
                db.close();
            }
        }


    }


    private void loadFirst() {
        isLoading = true;
                /**判断网络状态*/
        if (HttpUtils.isNetworkConnected(mActivity)){
                        /**获取的URL*/
            HttpUtils.get(Constant.LATESTNEWS, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

                }

                //成功
                @Override
                public void onSuccess(int i, Header[] headers, String s) {
                    /**写入SQLite中*/
                    SQLiteDatabase db = ((MainActivity)mActivity).getCacheDbHelper().getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values (" + Constant.LATEST_COLUMN + ",'" + s + "')");
                    db.close();
                    parseLatestJson(s);

                }
            });
        }else {
            /**无网络连接时调用*/
            SQLiteDatabase db = ((MainActivity)mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + Constant.LATEST_COLUMN, null);
            if (cursor.moveToFirst()){
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseLatestJson(json);
            }else {
                isLoading = false;
            }
            cursor.close();
            db.close();
        }

    }

    /**解析数据并添加到 Adapter 中*/
    private void parseLatestJson(String s) {
        Gson gson = new Gson();
        latest = gson.fromJson(s, Latest.class);
        date = latest.getDate();
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<StoriesEntity> entities = latest.getStories();
                StoriesEntity entity = new StoriesEntity();
                entity.setType(Constant.TOPIC);
                entity.setTitle("今日热闻");
                entity.setTitle(convertDate(date));
                entities.add(0, entity);
                mAdapter.addList(entities);
                isLoading = false;
            }
        });
    }

    private String convertDate(String date) {
        String result = date.substring(0, 4);
        result += "年";
        result += date.substring(4, 6);
        result += "月";
        result += date.substring(6, 8);
        result += "日";
        return result;
    }

}
