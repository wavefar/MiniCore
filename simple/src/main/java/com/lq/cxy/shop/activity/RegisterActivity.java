package com.lq.cxy.shop.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lq.cxy.shop.R;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;

import org.wavefar.lib.base.BaseSimpleActivity;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.AlertUtils;
import org.wavefar.lib.utils.IntentUtils;
import org.wavefar.lib.utils.StringUtil;
import org.wavefar.lib.utils.ToastUtils;


/**
 * 注册页面、忘记密码、修改密码公用
 *
 * @author summer
 */
public class RegisterActivity extends BaseSimpleActivity implements View.OnClickListener {

    private Button registerBtn, sendVerification;
    private EditText mobileNumber, inputVerification;
    private UserAPI mUserAPI;
    private AuthCountDownTimer timeCount;
    /**
     * 当前请求类型默认为0注册，1为忘记密码 ，2为修改密码；
     */
    private int mActionType;

    @Override
    public void initParam() {
        mUserAPI = new UserAPI();
        timeCount = new AuthCountDownTimer(60 * 1000, 1000);
    }


    @Override
    public void initData() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        TextView titleTv = findViewById(R.id.toolbar_title);
        if (mActionType == 0) {
            titleTv.setText("用户注册");
        } else if(mActionType == 1) {
            titleTv.setText("忘记密码");
        } else if (mActionType == 2) {
            titleTv.setText("修改密码");
        }

        registerBtn = findViewById(R.id.saveBtn);
        sendVerification = findViewById(R.id.getcode_btn);
        mobileNumber = findViewById(R.id.phone_et);
        inputVerification = findViewById(R.id.verifycode_et);
        setButtonState();

        registerBtn.setOnClickListener(this);
        sendVerification.setOnClickListener(this);
    }


    @Override
    protected void getBundle(Bundle bundle) {
        mActionType = bundle.getInt(SetPasswordActivity.TYPE_KEY);
    }

    /**
     * 下一步
     */
    private void next() {
        final String smsCode = inputVerification.getText().toString().trim();
        final String tel = mobileNumber.getText().toString().trim();
        if (TextUtils.isEmpty(tel) || !StringUtil.isMobileNo(tel)) {
            ToastUtils.showLong("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(smsCode)) {
            ToastUtils.showLong("请输入验证码");
            return;
        }
        AlertUtils.showProgressDialog(this, "", "请求中...");
        mUserAPI.smsVerifityCode(tel, smsCode, new BaseResultCallback<BaseResponseEntity<?>>() {
            @Override
            public void onResponse(BaseResponseEntity<?> response) {
                AlertUtils.closeProgressDialog();
                if (response.isSuccess()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(SetPasswordActivity.PHONE_KEY, tel);
                    bundle.putInt(SetPasswordActivity.TYPE_KEY, mActionType);
                    bundle.putString(SetPasswordActivity.SMSCODE_KEY,smsCode);
                    IntentUtils.redirectAndPrameter(SetPasswordActivity.class, bundle);
                    finish();
                } else {
                    ToastUtils.showLong(response.getMessage());
                }
            }
        });
    }

    /**
     * 验证码
     */
    private void verifyCode() {
        String telPhone = mobileNumber.getText().toString().trim();
        if (TextUtils.isEmpty(telPhone) || !StringUtil.isMobileNo(telPhone)) {
            ToastUtils.showLong("请输入正确的手机号");
            return;
        }
        mUserAPI.getPhoneSMS(telPhone, new BaseResultCallback<BaseResponseEntity<?>>() {
            @Override
            public void onResponse(BaseResponseEntity<?> response) {
                if (response.isSuccess()) {
                    timeCount.start();
                    ToastUtils.showLong("验证码已发送到你的手机");
                } else {
                    sendVerification.setEnabled(true);
                    sendVerification.setTextColor(ContextCompat.getColor(RegisterActivity.this,R.color.colorAccent));
                    sendVerification.setText("获取验证码");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.saveBtn:
                if (!isFinishing()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(getWindow().getDecorView()
                                .getWindowToken(), 0);
                    }
                    next();
                }
                break;
            case R.id.getcode_btn:
                verifyCode();
                break;
            default:
                break;
        }
    }

    private void setButtonState() {
        registerBtn.setBackgroundResource(R.drawable.shape_gray_btn);
        registerBtn.setClickable(false);
        mobileNumber.addTextChangedListener(new TextWatcher() {
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
                if (StringUtil.isEmpty(mobileNumber.getText().toString())
                        || StringUtil.isEmpty(inputVerification.getText().toString())) {
                    registerBtn.setClickable(false);
                    registerBtn.setBackgroundResource(R.drawable.shape_gray_btn);
                } else {
                    registerBtn.setClickable(true);
                    registerBtn.setBackgroundResource(R.drawable.shape_btn);
                }
            }
        });

        inputVerification.addTextChangedListener(new TextWatcher() {
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
                if (StringUtil.isEmpty(mobileNumber.getText().toString())
                        || StringUtil.isEmpty(inputVerification.getText().toString())) {
                    registerBtn.setClickable(false);
                    registerBtn.setBackgroundResource(R.drawable.shape_gray_btn);
                } else {
                    registerBtn.setClickable(true);
                    registerBtn.setBackgroundResource(R.drawable.shape_btn);
                }
            }
        });
    }

    private class AuthCountDownTimer extends CountDownTimer {
        private AuthCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            sendVerification.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.color_999999));
            sendVerification.setText(String.format("(重新获取%3d秒)", millisUntilFinished / 1000));
            sendVerification.setEnabled(false);
        }

        @Override
        public void onFinish() {
            sendVerification.setEnabled(true);
            sendVerification.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.colorAccent));
            sendVerification.setText("获取验证码");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCount != null) {
            timeCount.cancel();
            timeCount = null;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_register;
    }

}
