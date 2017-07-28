package com.baway.wangkai;

import android.app.Application;

import com.baway.wangkai.utils.CrashHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by DELL on 2017/7/28.
 * @author 王锴
 * @Date 2017年7月28日14:48:20
 * 功能：全局变量 初始化ImageLoader
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderConfiguration ilcf = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(ilcf);
        CrashHandler crashHandler = CrashHandler.getInstance();

    }
}
