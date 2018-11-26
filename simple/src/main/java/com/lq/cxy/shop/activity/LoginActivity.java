package com.lq.cxy.shop.activity;

import android.os.Bundle;

import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.databinding.ActivityLoginBinding;
import com.lq.cxy.shop.model.viewmodel.LoginViewModel;

import org.wavefar.lib.base.BaseActivity;


/**
 * 一个MVVM模式的登陆界面
 * @author summer
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {
    public final  static  String PRE_CLASSNAME_KEY = "PRE_CLASSNAME_KEY";
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public LoginViewModel initViewModel() {
        //View持有ViewModel的引用 (这里暂时没有用Dagger2解耦)
        return new LoginViewModel(this);
    }
}
