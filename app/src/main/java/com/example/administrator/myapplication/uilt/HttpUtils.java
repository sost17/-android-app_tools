package com.example.administrator.myapplication.uilt;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * Created by Administrator on 16-3-20.
 */
public class HttpUtils {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url,ResponseHandlerInterface responseHandlerInterface){
        client.get(Constant.BASEURL+url,responseHandlerInterface);
    }

    public static boolean isNetworkConnected(Context context){
      if (context != null){
          /**
           * 判断是否有网络
           */
          ConnectivityManager connectivityManager = (ConnectivityManager)
                  context.getSystemService(Context.CONNECTIVITY_SERVICE);
          NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

          if (networkInfo !=null){
              return networkInfo.isAvailable();
          }

      }
        return false;

    }

}
