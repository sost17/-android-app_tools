package com.example.administrator.myapplication.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.model.StoriesEntity;
import com.example.administrator.myapplication.uilt.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 16-3-20.
 */
public class MainAdapter extends BaseAdapter {

    private List<StoriesEntity> entities;
    private ImageLoader mImageloader;
    private Context context;
    private DisplayImageOptions options;
    private boolean isLight;

    public MainAdapter(Context content) {
        this.context  = content;
        this.entities = new ArrayList<>();

        mImageloader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .build();

        isLight = ((MainActivity)content).isLight();

    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public Object getItem(int position) {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.main_news_item, parent, false);
            viewHolder.tv_topic = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.iv_title = (ImageView) convertView.findViewById(R.id.tv_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        StoriesEntity entity = entities.get(i);
        if (entity.getType() == Constant.TOPIC) {
            ((FrameLayout) viewHolder.tv_topic.getParent()).setBackgroundColor(Color.TRANSPARENT);
            viewHolder.tv_title.setVisibility(View.GONE);
            viewHolder.iv_title.setVisibility(View.GONE);
            viewHolder.tv_topic.setVisibility(View.VISIBLE);
            viewHolder.tv_topic.setText(entity.getTitle());
        } else {
            //样式设置
            ((FrameLayout) viewHolder.tv_topic.getParent()).setBackgroundResource(isLight ? R.drawable.item_background_selector_light : R.drawable.item_background_selector_dark);
            viewHolder.tv_topic.setVisibility(View.GONE);
            viewHolder.tv_title.setVisibility(View.VISIBLE);
            viewHolder.iv_title.setVisibility(View.VISIBLE);
            viewHolder.tv_title.setText(entity.getTitle());
            mImageloader.displayImage(entity.getImages().get(0), viewHolder.iv_title, options);
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView tv_topic;
        TextView tv_title;
        ImageView iv_title;
    }


    public void addList(List<StoriesEntity> items) {
        this.entities.addAll(items);
        notifyDataSetChanged();
    }
}
