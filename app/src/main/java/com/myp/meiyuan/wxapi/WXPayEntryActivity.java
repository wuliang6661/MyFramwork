package com.myp.meiyuan.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.myp.meiyuan.config.LocalConfiguration;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by wuliang on 2017/3/6.
 * <p>
 * 微信支付回调的activity类
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "com.myp.meiyuan.wxapi.WXPayEntryActivity";

    private IWXAPI api;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2);
        api = WXAPIFactory.createWXAPI(this, LocalConfiguration.WEIXIN_APP_ID);
        api.handleIntent(getIntent(), this);
        api.registerApp(LocalConfiguration.WEIXIN_APP_ID);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq req) {

    }


    @Override
    public void onResp(BaseResp resp) {
        int errCode = resp.errCode;
        if (errCode == 0) {
            // 0成功 展示成功页面
            // Intent intent = new Intent("name");
            // sendBroadcast(intent);
            // Log.e("test","支付成功的回调方法--onResp--");
            // Toast.makeText(this,"支付完成",Toast.LENGTH_SHORT).show();
//            new AlertDialog.Builder(this).setMessage("支付成功").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    finish();
//                    PaymentActivity.instance.finish();
//                    Intent intent = new Intent(WXPayEntryActivity.this, PuzzGameActivity.class);
//                    intent.putExtra("ISPLAY",true);
//                    startActivity(intent);
//                }
//            }).setTitle("提示").create().show();
//            Toast.makeText(this,"点击确定按钮开始参与拼图游戏活动",Toast.LENGTH_LONG).show();
        } else if (errCode == -1) {
            //-1 错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
//            new AlertDialog.Builder(this).setMessage("支付出错").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    finish();
//                }
//            }).setTitle("提示").create().show();
            finish();
        } else if (errCode == -2) {
            //-2 用户取消 无需处理。发生场景：用户不支付了，点击取消，返回APP。
            finish();
        }
    }
}