package com.myp.meiyuan.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.myp.meiyuan.api.ApiManager;
import com.myp.meiyuan.config.LocalConfiguration;
import com.myp.meiyuan.util.LogUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;


/**
 * Created by wuliang on 2017/3/3.o
 * <p>
 * 用于微信提供的分享和登录
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String APP_SECRET = LocalConfiguration.APP_SECRET;
    private IWXAPI mWeixinAPI;
    public static final String WEIXIN_APP_ID = LocalConfiguration.WEIXIN_APP_ID;
    private static String uuid;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeixinAPI = WXAPIFactory.createWXAPI(this, WEIXIN_APP_ID, true);
        mWeixinAPI.handleIntent(this.getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWeixinAPI.handleIntent(intent, this);//必须调用此句话
    }

    //微信发送的请求将回调到onReq方法
    @Override
    public void onReq(BaseReq req) {
        LogUtils.log("onReq");
    }


    //发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {
        LogUtils.log("onResp");
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                LogUtils.log("ERR_OK");
                //发送成功
                SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                if (sendResp != null) {
                    String code = sendResp.code;
                    getAccess_token(code);
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                LogUtils.log("ERR_USER_CANCEL");
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                LogUtils.log("ERR_AUTH_DENIED");
                //发送被拒绝
                break;
            default:
                //发送返回
                break;
        }
    }

    /**
     * 获取openid accessToken值用于后期操作
     *
     * @param code 请求码
     */
    private void getAccess_token(final String code) {
        String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + WEIXIN_APP_ID
                + "&secret="
                + APP_SECRET
                + "&code="
                + code
                + "&grant_type=authorization_code";
        LogUtils.log("getAccess_token：" + path);
        //网络请求，根据自己的请求方式
//        VolleyRequest.get(this, path, "getAccess_token", false, null, new VolleyRequest.Callback() {
//            @Override
//            public void onSuccess(String result) {
//                LogUtils.log("getAccess_token_result:" + result);
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(result);
//                    String openid = jsonObject.getString("openid").toString().trim();
//                    String access_token = jsonObject.getString("access_token").toString().trim();
//                    getUserMesg(access_token, openid);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//
//            }
//        });
        ApiManager.getInstance().getWxMesage(WEIXIN_APP_ID, APP_SECRET, code, "authorization_code")
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String result) {
                        LogUtils.log("getAccess_token_result:" + result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            String openid = jsonObject.getString("openid").toString().trim();
                            String access_token = jsonObject.getString("access_token").toString().trim();
                            getUserMesg(access_token, openid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 获取微信的个人信息
     *
     * @param access_token
     * @param openid
     */
    private void getUserMesg(final String access_token, final String openid) {
        String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + access_token
                + "&openid="
                + openid;
        LogUtils.log("getUserMesg：" + path);
        //网络请求，根据自己的请求方式
//        VolleyRequest.get(this, path, "getAccess_token", false, null, new VolleyRequest.Callback() {
//            @Override
//            public void onSuccess(String result) {
//                LogUtils.log("getUserMesg_result:" + result);
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(result);
//                    String nickname = jsonObject.getString("nickname");
//                    int sex = Integer.parseInt(jsonObject.get("sex").toString());
//                    String headimgurl = jsonObject.getString("headimgurl");
//
//                    LogUtils.log("用户基本信息:");
//                    LogUtils.log("nickname:" + nickname);
//                    LogUtils.log("sex:" + sex);
//                    LogUtils.log("headimgurl:" + headimgurl);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                finish();
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//
//            }
//        });
        ApiManager.getInstance().getUserMessage(access_token, openid)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String result) {
                        LogUtils.log("getUserMesg_result:" + result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            String nickname = jsonObject.getString("nickname");
                            int sex = Integer.parseInt(jsonObject.get("sex").toString());
                            String headimgurl = jsonObject.getString("headimgurl");

                            LogUtils.log("用户基本信息:");
                            LogUtils.log("nickname:" + nickname);
                            LogUtils.log("sex:" + sex);
                            LogUtils.log("headimgurl:" + headimgurl);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                });
    }
}
