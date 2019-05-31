package com.lcjiang.im.cjeaseuidemo;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;



/**
 * Created by cj on 2016/12/5.
 */
public class MyApplication extends Application {

    private static final String TAG = "MyApp";
    private static Context mContext;
    private static MyApplication myApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        myApp = this;
        SDKInitializer.initialize(this);
        CjImHelper.getInstance().init(mContext);


    }

    /**获取Context.
     * @return
     */
    public static Context getContext(){
        return mContext;
    }


    public static MyApplication getInstance() {
        return myApp;
    }

}
