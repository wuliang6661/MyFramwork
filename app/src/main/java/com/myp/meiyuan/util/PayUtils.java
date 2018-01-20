package com.myp.meiyuan.util;

import android.app.Activity;

import com.alipay.sdk.app.PayTask;

/**
 * Created by wuliang on 2017/3/7.
 * <p>
 * 此处放置调用支付方法的工具类
 */

public class PayUtils {


    /**
     * 调用支付宝支付的方法
     *
     * 接入App支付能力，需要在开放平台创建一个应用，通过该应用来接入各种能力。
     *指引为：https://open.alipay.com/platform/home.htm?from=zhuzhan20160818
     *
     * @param payInfo 订单数据
     */
    public static void aliPay(final Activity context, final String payInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(context);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
//                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
            }
        };
// 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
