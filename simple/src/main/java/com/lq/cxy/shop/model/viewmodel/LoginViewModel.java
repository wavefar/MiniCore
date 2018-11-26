package com.lq.cxy.shop.model.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;

import com.lq.cxy.shop.Constant;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.activity.MainActivity;
import com.lq.cxy.shop.activity.RegisterActivity;
import com.lq.cxy.shop.activity.SetPasswordActivity;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.model.entity.UserEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.http.base.BaseSimpleResultCallback;

import org.wavefar.lib.base.BaseViewModel;
import org.wavefar.lib.binding.command.BindingAction;
import org.wavefar.lib.binding.command.BindingCommand;
import org.wavefar.lib.utils.IntentUtils;
import org.wavefar.lib.utils.ToastUtils;

import static com.lq.cxy.shop.activity.LoginActivity.PRE_CLASSNAME_KEY;


/**
 * 登录业务逻辑
 * @author Administrator
 */
public class LoginViewModel extends BaseViewModel {
    /**
     * 用户名的绑定
     */
    public ObservableField<String> userName = new ObservableField<>("");
    /**
     * 密码的绑定
     */
    public ObservableField<String> password = new ObservableField<>("");

    /**
     * 之前页面类名
     */
    private String preClassName;

    public LoginViewModel(Context context) {
        super(context);
        Intent intent = ((Activity)context).getIntent();
        if (intent != null) {
            preClassName  = intent.getStringExtra(PRE_CLASSNAME_KEY);
        }
    }

    private boolean isHide;
    /**
     *  密码显示开关  (你可以尝试着狂按这个按钮,会发现它有防多次点击的功能)
     */
    public BindingCommand passwordShowSwitchOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            changePasswordStyle(isHide);
            isHide = !isHide;
        }
    });

    private void changePasswordStyle(boolean isHide) {
        ImageView imageView = ((Activity)context).findViewById(R.id.iv_swich_passwrod);
        EditText passwordEt = ((Activity)context).findViewById(R.id.et_password);

        imageView.setImageResource(isHide ? R.mipmap.show_psw : R.mipmap.show_psw_press);
        passwordEt.setTransformationMethod(isHide ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
    }

    /**
     * 登录
     */
    public BindingCommand onLogin = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            login();
        }
    });

    /**
     * 注册
     */
    public BindingCommand onRegister = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(RegisterActivity.class);
        }
    });

    /**
     * 忘记密码
     */
    public BindingCommand onForget = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle = new Bundle();
            bundle.putInt(SetPasswordActivity.TYPE_KEY,1);
            startActivity(RegisterActivity.class,bundle);
        }
    });

    /**
     * 查看注册协议
     */
    public BindingCommand onAgreement = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntentUtils.startWeb(Constant.AGREEMENT,"用户使用协议");
        }
    });


    /**
     * 登陆操作
     **/
    private void login() {
        if (TextUtils.isEmpty(userName.get())) {
            ToastUtils.showShort("请输入账号！");
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtils.showShort("请输入密码！");
            return;
        }
        showDialog("登录中...");
        new UserAPI().login(userName.get(),password.get(),new BaseSimpleResultCallback<BaseResponseEntity<UserEntity>,UserEntity>(){

            @Override
            public void onSucceed(UserEntity userEntity) {
                dismissDialog();
                if (userEntity != null) {
                    ToastUtils.showShort("登录成功");
                    UserAPI.saveUserInfo(userEntity);
                    redirect();
                }
            }
        });
    }

    private void redirect() {
        if (preClassName != null) {
            try {
                startActivity(Class.forName(preClassName),null,true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                startActivity(MainActivity.class,null,true);
            }
        } else {
            ((Activity)context).finish();
        }
    }
}
