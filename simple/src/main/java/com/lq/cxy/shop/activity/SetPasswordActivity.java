package com.lq.cxy.shop.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lq.cxy.shop.R;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.model.entity.UserEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.http.base.BaseSimpleResultCallback;

import org.wavefar.lib.base.BaseSimpleActivity;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.AlertUtils;
import org.wavefar.lib.utils.IntentUtils;
import org.wavefar.lib.utils.StringUtil;
import org.wavefar.lib.utils.ToastUtils;


/**
 * 设置密码(包括注册、忘记密码、修改密码)
 * @author Administrator
 */
public class SetPasswordActivity extends BaseSimpleActivity implements
        OnClickListener {

    public static final String PHONE_KEY = "Phone_Key";
    public static final String TYPE_KEY = "Type_Key";
    public static final String SMSCODE_KEY = "smsCode_key";
    private EditText mPasswordEt, mSurePasswordEt;
    private Button mSaveBtn;
    private UserAPI mUserAPI;
    private String mTel;
    private int mType;
    private String mSmsCode;

    @Override
    protected int getContentView() {
        return R.layout.activity_set_password;
    }

    @Override
    protected void getBundle(Bundle bundle) {
        mTel = bundle.getString(PHONE_KEY);
        mType = bundle.getInt(TYPE_KEY);
        mSmsCode = bundle.getString(SMSCODE_KEY);
    }

    @Override
    public void initParam() {
        super.initParam();
        mUserAPI = new UserAPI();
    }


    @Override
    public void initData() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        TextView topTitleTv = findViewById(R.id.toolbar_title);
        topTitleTv.setText("设置密码");
        mPasswordEt = findViewById(R.id.password_et);
        mSurePasswordEt = findViewById(R.id.sure_password_et);
        mSaveBtn = findViewById(R.id.saveBtn);
        setButtonState();
        mSaveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 保存
            case R.id.saveBtn:
                submit();
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 注册或忘记密码修改
     */
    private void submit() {
        String mPas = mPasswordEt.getText().toString().trim();
        String mSurePas = mSurePasswordEt.getText().toString().trim();
        if (TextUtils.isEmpty(mPas)) {
            ToastUtils.showLong( "请输入密码");
            return;
        }
        if (TextUtils.isEmpty(mSurePas)) {
            ToastUtils.showLong( "请输入确认密码");
            return;
        }
        if (!mPas.equals(mSurePas)) {
            ToastUtils.showLong("两次密码不一致");
            return;
        }
        AlertUtils.showProgressDialog(this,"","请求中...");
        //注册
        if (mType == 0) {
            final UserEntity userEntity = new UserEntity();
            userEntity.setLoginName(mTel);
            userEntity.setPassword(mPas);
            userEntity.setPhoneNum(mTel);
            mUserAPI.register(userEntity,new BaseSimpleResultCallback<BaseResponseEntity<UserEntity>,UserEntity>() {
                @Override
                public void onSucceed(UserEntity entity) {
                    AlertUtils.closeProgressDialog();
                    if (entity != null) {
                        ToastUtils.showShort("注册成功");
                        UserAPI.saveUserInfo(entity);
                        IntentUtils.redirect(MainActivity.class);
                        finish();
                    }
                }
            });
            //忘记密码
        } else if(mType == 1) {
            mUserAPI.forgetPassword(mTel,mPas,mSmsCode,new BaseResultCallback<BaseResponseEntity<?>>(){

                @Override
                public void onResponse(BaseResponseEntity<?> response) {
                    AlertUtils.closeProgressDialog();
                    ToastUtils.showShort(response.getMessage());
                    if (response.isSuccess()) {
                        UserAPI.unLoginPreHome();
                        finish();
                    }
                }
            });

            //修改密码
        }else if (mType == 2) {
            mUserAPI.modifyPassword(mPas,new BaseResultCallback<BaseResponseEntity<?>>(){

                @Override
                public void onResponse(BaseResponseEntity<?> response) {
                    AlertUtils.closeProgressDialog();
                    ToastUtils.showShort(response.getMessage());
                    if (response.isSuccess()) {
                        //注销后重新登录
                        mUserAPI.logout(SetPasswordActivity.this);
                    }
                }
            });
        }
    }

    private void setButtonState() {
        mSaveBtn.setBackgroundResource(R.drawable.shape_gray_btn);
        mSaveBtn.setClickable(false);
        mPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtil.isEmpty(mPasswordEt.getText().toString())
                        || StringUtil.isEmpty(mSurePasswordEt.getText()
                        .toString())) {
                    mSaveBtn.setClickable(false);
                    mSaveBtn.setBackgroundResource(R.drawable.shape_gray_btn);
                } else {
                    mSaveBtn.setClickable(true);
                    mSaveBtn.setBackgroundResource(R.drawable.shape_btn);
                }
            }
        });

        mSurePasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtil.isEmpty(mPasswordEt.getText().toString())
                        || StringUtil.isEmpty(mSurePasswordEt.getText()
                        .toString())) {
                    mSaveBtn.setClickable(false);
                    mSaveBtn.setBackgroundResource(R.drawable.shape_gray_btn);
                } else {
                    mSaveBtn.setClickable(true);
                    mSaveBtn.setBackgroundResource(R.drawable.shape_btn);
                }
            }
        });
    }
}
