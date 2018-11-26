package com.lq.cxy.shop.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bumptech.glide.Glide;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.model.entity.UserEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.http.base.BaseSimpleResultCallback;
import com.lq.cxy.shop.utils.UiUtil;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;

import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.AlertUtils;
import org.wavefar.lib.utils.KeyBoardUtil;
import org.wavefar.lib.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户详情
 *
 * @author summer
 */
public class UserDetailFragment extends Fragment implements View.OnClickListener {
    private static final int REQUEST_CODE_CHOOSE = 12;
    private ImageView avatarIv;
    private TextView sexTv,nameTv;
    private static final ArrayList<String> SEX_LIST = new ArrayList<>();
    private UserAPI mUserApi;
    private UserEntity userEntity;
    private File avatar;

    static {
        SEX_LIST.add("男");
        SEX_LIST.add("女");
        SEX_LIST.add("保密");
    }

    private ISListConfig config;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_user_detail, container, false);
            initView(rootView);
        }
        return rootView;
    }

    private void initView(View view) {
        TextView titleTv = view.findViewById(R.id.toolbar_title);
        titleTv.setText("用户详情");
        view.findViewById(R.id.iv_back).setOnClickListener(this);


        avatarIv = view.findViewById(R.id.avatarIv);
        sexTv = view.findViewById(R.id.sexTv);
        nameTv = view.findViewById(R.id.nameTv);
        view.findViewById(R.id.avatarRl).setOnClickListener(this);
        view.findViewById(R.id.sexRl).setOnClickListener(this);
        view.findViewById(R.id.saveBtn).setOnClickListener(this);
        view.findViewById(R.id.nameRl).setOnClickListener(this);
        requestData();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserApi = new UserAPI();
        config = UiUtil.getImageSelectorConfig(getContext(), 1, 1, 100, 100);
    }

    /**
     * 网络请求获取用户信息
     */
    private void requestData() {
        userEntity = UserAPI.getUserInfo();
        if (userEntity == null) {
            return;
        }
        mUserApi.getUserDetail(userEntity.getUserId(), new BaseSimpleResultCallback<BaseResponseEntity<UserEntity>, UserEntity>() {
            @Override
            public void onSucceed(UserEntity entity) {
                userEntity =  entity;
                if (entity != null) {
                    setData(entity);
                }
            }
        });
    }

    /**
     * 填充数据
     */
    private void setData(UserEntity entity) {
        if (entity.getSex() == 0) {
            sexTv.setText("男");
        } else if (entity.getSex() == 1) {
            sexTv.setText("女");
        } else {
            sexTv.setText("保密");
        }
        nameTv.setText(TextUtils.isEmpty(entity.getName()) ? "未设置" : entity.getName());
        Glide.with(getActivity()).load(entity.getAvatar()).into(avatarIv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.sexRl:
                UiUtil.showOptionDialog(getActivity(), SEX_LIST, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        if (userEntity != null) {
                            userEntity.setSex(options1);
                        }
                        sexTv.setText(SEX_LIST.get(options1));
                    }
                });
                break;
             case  R.id.nameRl:
                 showEditDialog();
                break;
            case R.id.avatarRl:
                requestCamera();
                break;
            case R.id.saveBtn:
                updateUser();
                break;
            default:
                break;
        }
    }

    private void updateUser() {
        if (userEntity == null) {
            ToastUtils.showShort("获取数据异常，退出重新进入该页");
            return;
        }
        AlertUtils.showProgressDialog(getContext(),"","请求中...");
        mUserApi.update(userEntity, avatar, new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                AlertUtils.closeProgressDialog();
                ToastUtils.showShort(response.getMessage());
            }
        });
    }

    /**
     * 获取拍照权限
     */
    private void requestCamera() {
        ISNav.getInstance().toListActivity(this, config, REQUEST_CODE_CHOOSE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            for (String path : pathList) {
                avatar = new File(path);
                Glide.with(getActivity()).load(avatar).into(avatarIv);
            }
        }
    }


    private void showEditDialog() {
        final EditText editText = new EditText(getContext());
        editText.setSingleLine(true);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        editText.setText(nameTv.getText());
        editText.selectAll();
        Dialog alertDialog = new AlertDialog.Builder(getContext()).setTitle("请输入您的昵称")
                .setView(editText)
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = editText.getText().toString();
                        if (!TextUtils.isEmpty(str)) {
                            nameTv.setText(str);
                            if (userEntity != null) {
                                userEntity.setName(str);
                            }
                        }
                    }
                })
                .setNegativeButton("取消", null).create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                KeyBoardUtil.showSoftInput(editText);
            }
        });
        alertDialog.show();
    }

}
