package com.myp.meiyuan.ui.main;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.myp.meiyuan.R;
import com.myp.meiyuan.mvp.MVPBaseActivity;

/**
 * MVPPlugin
 */

public class MainActivity extends MVPBaseActivity<MainContract.View, MainPresenter> implements MainContract.View {

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRequestError(String msg) {

    }

    @Override
    public void onRequestEnd() {

    }
}
