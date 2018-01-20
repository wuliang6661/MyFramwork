package com.myp.meiyuan.ui.main;

import com.myp.meiyuan.mvp.BasePresenter;
import com.myp.meiyuan.mvp.BaseRequestView;

/**
 * MVPPlugin
 * �wuliang
 * <p>
 * 主页的业务接口
 */

public class MainContract {
    interface View extends BaseRequestView {

    }

    interface Presenter extends BasePresenter<View> {

    }
}
