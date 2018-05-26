package com.example.administrator.myapplication.model;

import java.util.List;

/**
 * Created by wwjun.wang on 2015/8/12.
 */
public class Latest {

    private List<StoriesEntity> stories;
    private String date;


    public void setStories(List<StoriesEntity> stories) {
        this.stories = stories;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public List<StoriesEntity> getStories() {
        return stories;
    }

    public String getDate() {
        return date;
    }


    @Override
    public String toString() {
        return "Latest{" +
                ", stories=" + stories +
                ", date='" + date + '\'' +
                '}';
    }
}
