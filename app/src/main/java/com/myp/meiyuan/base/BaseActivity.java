package com.myp.meiyuan.base;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.myp.meiyuan.util.AppManager;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * 作者 by wuliang 时间 16/10/31.
 * <p>
 * 所有activity的基类，此处建立了一个activity的栈，用于管理activity
 */

public abstract class BaseActivity extends RxAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }

    /**
     * 常用的跳转方法
     */
    public void gotoActivity(Class<?> cls, boolean isFinish) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        if (isFinish) {
            finish();
        }
    }

    public void gotoActivity(Class<?> cls, Bundle bundle, boolean isFinish) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
        if (isFinish) {
            finish();
        }
    }


    protected abstract int getLayout();
}
