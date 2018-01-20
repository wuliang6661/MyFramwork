package com.myp.meiyuan.wxapi;

import android.content.Context;

import com.myp.meiyuan.config.LocalConfiguration;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by wuliang on 2017/3/6.
 * <p>
 * 调用微信登陆，与微信支付的工具类
 * <p>
 * 在进行微信OAuth2.0授权登录接入之前，在微信开放平台注册开发者帐号，并拥有一个已审核通过的移动应用，
 * 并获得相应的AppID和AppSecret，申请微信登录且通过审核后，可开始接入流程。
 * <p>
 * 链接为：https://open.weixin.qq.com/cgi-bin/index?t=home/index&lang=zh_CN
 */

public class WXUtils {

    // 自己微信应用的 appId
    public static String WX_APP_ID = LocalConfiguration.WEIXIN_APP_ID;
    // 自己微信应用的 appSecret
    public static String WX_SECRET = LocalConfiguration.APP_SECRET;
    public static IWXAPI wxApi;


    /**
     * 在程序入口处调用此方法注册微信授权
     *
     * @param context
     */
    public static void registerWX(Context context) {
        wxApi = WXAPIFactory.createWXAPI(context, null);
        wxApi.registerApp(WX_APP_ID);
    }


    /**
     * 调用此方法微信登陆
     *
     * @param context
     */
    public static void loginWX(Context context) {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "supermember";
        msgApi.registerApp(WX_APP_ID);
        msgApi.sendReq(req);
    }


    /**
     * 调用此方法微信支付
     */
    public static void payWX() {
//        new AsyncTask(){
//            @Override
//            protected Object doInBackground(Object[] objects) {
        PayReq request = new PayReq();
        request.appId = WX_APP_ID;
        request.partnerId = "1900000109";   //微信支付分配的商户号
        request.prepayId = "1101000000140415649af9fc314aa427";  //微信返回的支付交易ID
        request.packageValue = "Sign=WXPay";    //扩展字符串 ，暂填固定值"Sign=WXPay"
        request.nonceStr = "1101000000140429eb40476f8896f4c9"; //随机字符串
        request.timeStamp = "1398746574";    //时间戳
        request.sign = WX_SECRET;
        wxApi.sendReq(request);
//        return null;
//            }
//        }.execute();
    }

//    //获取产品订单信息
//    private String genProductArgs() {
//        StringBuffer xml = new StringBuffer();
//        try {
//            String nonceStr = genNonceStr();
//            xml.append("</xml>");
//            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
//            packageParams.add(new BasicNameValuePair("appid", LocalConfiguration.WEIXIN_APP_ID)); //APPID
//            packageParams.add(new BasicNameValuePair("body", "单价：" + 13 + " x " + 11 + "份"));  //简单描述
//            packageParams.add(new BasicNameValuePair("mch_id", ""));  //商户ID
//            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));   //随机字符串
//            packageParams.add(new BasicNameValuePair("notify_url","http://www.weixin.qq.com/wxpay/pay.php")); //通知地址
//            packageParams.add(new BasicNameValuePair("out_trade_no",""));  //商户订单号
//            packageParams.add(new BasicNameValuePair("spbill_create_ip","")); //终端IP
//            //double price = Double.parseDouble(payment_num.getText().toString()) * (Integer.parseInt(singlePrice) * 100);
//            double price = Double.parseDouble("13") * 100 * n;
//            int priceInt = (int) price;
//            packageParams.add(new BasicNameValuePair("total_fee", priceInt+""));    //微信接收int型价格
//            packageParams.add(new BasicNameValuePair("trade_type", "APP"));  //支付类型
//            String sign = genAppSign(packageParams);
//            packageParams.add(new BasicNameValuePair("sign", sign));  //签名
//            String xmlstring = parseNodeToXML(packageParams);   //转化成xml
//            return xmlstring;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    //获取订单号
//    private String getTrade(){
//        long nowTime = System.currentTimeMillis();
//        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
//        return format.format(new Date(nowTime));
//    }
//
//    //获取支付签名Sign
//    StringBuilder sb = new StringBuilder();
//    private String genAppSign(List<NameValuePair> params) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < params.size(); i++) {
//            sb.append(params.get(i).getName());
//            sb.append('=');
//            sb.append(params.get(i).getValue());
//            sb.append('&');
//        }
//        sb.append("key=");
//        sb.append(Constants.API_KEY);
//        this.sb.append("sign str\n" + sb.toString() + "\n\n");
//        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
//        return appSign;
//    }
//
//    //获取随机字符串
//    private String genNonceStr() {
//        Random random = new Random();
//        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
//    }
//
//    /**
//     * 解析为xml格式
//     * @param treeNodes
//     * @return
//     */
//    public String parseNodeToXML(List<NameValuePair> treeNodes) {
//        StringBuffer xmlnodes = new StringBuffer();
//        if (treeNodes != null && treeNodes.size() > 0) {
//            xmlnodes.append("<xml>");
//            for (int i = 0; i < treeNodes.size(); i++) {
//                NameValuePair node = treeNodes.get(i);
//                xmlnodes.append("<"+node.getName()+">").append(node.getValue()).append("</"+node.getName()+">");
//            }
//            xmlnodes.append("</xml>");
//        }
//        //return xmlnodes.toString();
//        String xml = xmlnodes.toString();
//        try {
//            xml = new String(xml.toString().getBytes(), "ISO8859-1");  //商品详情为中文，将其转化为统一编码，不然获取perpred_id失败
//            return xml;
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//
//    //获取手机IP
//    public String getLocalHostIp() {
//        String ipaddress = "";
//        try {
//            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
//            // 遍历所用的网络接口
//            while (en.hasMoreElements()) {
//                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
//                Enumeration<InetAddress> inet = nif.getInetAddresses();
//                // 遍历每一个接口绑定的所有ip
//                while (inet.hasMoreElements()) {
//                    InetAddress ip = inet.nextElement();
//                    if (!ip.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ip.getHostAddress())) {
//                        return ip.getHostAddress();
//                    }
//                }
//            }
//        }
//        catch (SocketException e) {
//            Log.e("feige", "获取本地ip地址失败");
//            e.printStackTrace();
//        }
//        return ipaddress;
//    }

}
