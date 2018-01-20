package com.myp.meiyuan.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.myp.meiyuan.util.Utils;
import com.myp.meiyuan.wxapi.WXUtils;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

/**
 * 作者 by wuliang 时间 16/10/26.
 * <p>
 * 程序的application
 */

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        CustomActivityOnCrash.install(this);
//        CustomActivityOnCrash.setErrorActivityClass(CustomErrorActivity.class);
        /***初始化工具类*/
        Utils.init(this);
        /***注册分享*/
        ShareSDK.initSDK(this);
        /***注册极光推送*/
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        /***注册微信登陆，支付服务*/
        WXUtils.registerWX(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
