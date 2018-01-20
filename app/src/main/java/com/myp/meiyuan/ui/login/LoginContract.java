package com.myp.meiyuan.ui.login;

import com.myp.meiyuan.mvp.BasePresenter;
import com.myp.meiyuan.mvp.BaseRequestView;
import com.myp.meiyuan.mvp.BaseView;

/**
 * MVPPlugin
 * Wuliang
 * <p>
 * 登陆页面的业务控制
 */

public class LoginContract {

    interface View extends BaseRequestView {

        void getResult(String data);

    }

    interface Presenter extends BasePresenter<View> {

        void getData(String message);

    }
}
