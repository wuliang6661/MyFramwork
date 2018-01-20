package com.myp.meiyuan.ui.login;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.myp.meiyuan.R;
import com.myp.meiyuan.mvp.MVPBaseActivity;

/**
 * MVPPlugin
 * �wuliang
 * <p>
 * 登陆页面
 */

public class LoginActivity extends MVPBaseActivity<LoginContract.View, LoginPresenter> implements LoginContract.View {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.getData("aa");
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onRequestError(String msg) {

    }

    @Override
    public void onRequestEnd() {

    }

    @Override
    public void getResult(String data) {

    }
}
