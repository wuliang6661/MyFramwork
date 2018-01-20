package com.myp.meiyuan.ui.login;


import com.myp.meiyuan.mvp.BasePresenterImpl;

/**
 * MVPPlugin
 * �wuliang
 * <p>
 * 数据请求处理类 (业务处理)
 */

public class LoginPresenter extends BasePresenterImpl<LoginContract.View> implements LoginContract.Presenter {


    @Override
    public void getData(String message) {
        mView.getResult(message);
    }
}
