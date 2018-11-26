package com.lq.cxy.shop.activity.merchant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lq.cxy.shop.Constant;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.activity.LocationActivity;
import com.lq.cxy.shop.databinding.ActivityAddShopBinding;
import com.lq.cxy.shop.fragment.MeFragment;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.model.entity.LicenseInfoEntity;
import com.lq.cxy.shop.model.entity.ShopAddEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.merchant.ShopApi;
import com.lq.cxy.shop.utils.UiUtil;
import com.lq.cxy.shop.utils.Util;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;

import org.greenrobot.eventbus.EventBus;
import org.wavefar.lib.base.BaseSimpleActivity;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.AlertUtils;
import org.wavefar.lib.utils.KeyBoardUtil;
import org.wavefar.lib.utils.ToastUtils;

import java.io.File;
import java.util.List;


public class AddShopActivity extends BaseSimpleActivity implements View.OnClickListener {
    ActivityAddShopBinding binding;
    ShopAddEntity requestEntity = new ShopAddEntity();
    private boolean isAdd = true;
    ISListConfig config;
    String longitude, lantitude;
    private File shopImg;
    private LicenseInfoEntity lisence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constant.REQUEST_CODE_PERMISSION);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_shop);
        config = UiUtil.getImageSelectorConfig(getApplicationContext(), 2, 1, 800, 400);
        AlertUtils.showProgressDialog(this, "", "加载中");
        if (UserAPI.getLisenceInfo() == null) {
            loadLisence();
        }
        new ShopApi().queryShopInfo(new BaseResultCallback<BaseResponseEntity<ShopAddEntity>>() {
            @Override
            public void onResponse(BaseResponseEntity<ShopAddEntity> response) {
                AlertUtils.closeProgressDialog();
                if (response.isSuccess()) {
                    isAdd = response.getContent() == null;
                    setDataWithEntity(response.getContent());
                } else {
                    ToastUtils.showShort(response.getMessage());
                    finish();
                }
            }
        });
        binding.adShopImgRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.adShopImgRoot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) binding.adShopImgRoot.getLayoutParams();
                lp.height = binding.adShopImgRoot.getWidth() / 2;
                lp.width = binding.adShopImgRoot.getWidth();
                binding.adShopImgRoot.setLayoutParams(lp);
                KeyBoardUtil.hideSoftInput(AddShopActivity.this);
            }
        });
        setDataWithEntity(null);
        KeyBoardUtil.hideSoftInput(this);
    }

    private void loadLisence() {
        Util.loadAndCacheLisence(new BaseResultCallback<BaseResponseEntity<LicenseInfoEntity>>() {
            @Override
            public void onResponse(BaseResponseEntity<LicenseInfoEntity> response) {
                if (response.isSuccess()) {
                    lisence = response.getContent();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.adShopAddrText:
                startActivityForResult(new Intent(this, LocationActivity.class), LocationActivity.GET_LOCATION_CODE);
                break;
            case R.id.adShopImgRoot:
                ISNav.getInstance().toListActivity(this, config, Constant.REQUEST_CODE_CHOOSE);
                break;
            case R.id.head_back_root:
                finish();
                break;
            case R.id.clickToSave:
                checkAndSave(false);
                break;
            case R.id.clickToSubmitPublish:
                checkAndSave(true);
                break;
        }
    }

    private void checkAndSave(final boolean publish) {
        String name = binding.adShopNameText.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShort("请输入店铺名称");
            return;
        }
        String addr = binding.adShopAddrText.getText().toString().trim();
        if (TextUtils.isEmpty(addr)) {
            ToastUtils.showShort("请输入店铺地址");
            return;
        }
        String phone = binding.adShopPhoneText.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort("请输入联系电话");
            return;
        }
        if (isAdd && shopImg == null) {
            ToastUtils.showShort("请选择店铺图片");
            return;
        }
        requestEntity.setStoreAddress(addr);
        requestEntity.setStoreName(name);
        requestEntity.setLatitude(lantitude);
        requestEntity.setLongitude(longitude);
        requestEntity.setStoreTel(phone);
        requestEntity.setFile(shopImg);
        if (lisence == null) {
            lisence = UserAPI.getLisenceInfo();
            if (lisence == null) {
                loadLisence();
                ToastUtils.showShort("参数错误，请稍后再试!");
                return;
            }
        }
        requestEntity.setCategoryId(Integer.valueOf(Util.getIdBaseName(Util.getCategoryList(), lisence.getScope())));
//        requestEntity.setStoreCode(binding.adShopCodeText.getText().toString().trim());
        new ShopApi().submitShopInfo(requestEntity, isAdd, new BaseResultCallback<BaseResponseEntity<ShopAddEntity>>() {
            @Override
            public void onResponse(BaseResponseEntity<ShopAddEntity> response) {
                if (response.isSuccess()) {
                    if (publish) {
                        publishStore(response.getContent());
                    } else {
                        EventBus.getDefault().post(new MeFragment.RefreshUserDetailEvent() {
                        });
                        ToastUtils.showShort("店铺信息保存成功！");
                        finish();
                    }
                } else {
                    ToastUtils.showShort(response.getMessage());
                }
            }
        });
    }

    private void publishStore(ShopAddEntity store) {
        new ShopApi().publishShop(store, new BaseResultCallback<BaseResponseEntity<String>>() {
            @Override
            public void onResponse(BaseResponseEntity<String> response) {
                if (response.isSuccess()) {
                    ToastUtils.showShort("店铺信息提交成功！");
                    EventBus.getDefault().post(new MeFragment.RefreshUserDetailEvent() {
                    });
                    finish();
                } else {
                    ToastUtils.showShort(response.getMessage());
                }
            }
        });
    }

    private void setDataWithEntity(ShopAddEntity entity) {
        TextView title = binding.shopApplyHead.findViewById(R.id.head_title);
        binding.shopApplyHead.findViewById(R.id.head_back_root).setOnClickListener(this);
        ImageView editApplyState = binding.shopApplyHead.findViewById(R.id.head_more);
        editApplyState.setVisibility(View.GONE);
        binding.adShopImgRoot.setOnClickListener(this);
        binding.adShopAddrText.setOnClickListener(this);
        binding.clickToSave.setOnClickListener(this);
        binding.clickToSubmitPublish.setOnClickListener(this);
        if (entity == null) {
            title.setText(R.string.add_shop_title);
        } else {
            boolean enableView = entity.getStatus() == 0 || entity.getStatus() == 2;
            binding.clickToSave.setVisibility(enableView ? View.VISIBLE : View.GONE);
            binding.clickToSubmitPublish.setVisibility(enableView ? View.VISIBLE : View.GONE);
            binding.adShopAddrText.setEnabled(enableView);
            binding.adShopImgRoot.setEnabled(enableView);
            binding.adShopAddrText.setEnabled(enableView);
            binding.adShopNameText.setEnabled(enableView);
            binding.adShopPhoneText.setEnabled(enableView);

            requestEntity = entity;
            title.setText(R.string.update_shop_title);
            binding.adShopAddrText.setText(entity.getStoreAddress());
            binding.adShopNameText.setText(entity.getStoreName());
//            binding.adShopCodeText.setText(entity.getStoreCode());
            binding.adShopPhoneText.setText(entity.getStoreTel());
            Glide.with(getApplicationContext()).load(entity.getStoreImg()).into(binding.adShopImgImage);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 图片选择结果回调
        if (requestCode == Constant.REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            for (String path: pathList) {
                shopImg = new File(path);
                requestEntity.setFile(shopImg);
                binding.adShopImgImage.setImageURI(Uri.fromFile(shopImg));
            }
        }
        if (requestCode == LocationActivity.GET_LOCATION_CODE && resultCode == RESULT_OK) {
            Bundle locExtra = data.getExtras();
            String addr = locExtra.getString(LocationActivity.EXTRA_KEY_ADDRESS);
            longitude = locExtra.getString(LocationActivity.EXTRA_KEY_LONGITUDE);
            lantitude = locExtra.getString(LocationActivity.EXTRA_KEY_LATITUCE);
            binding.adShopAddrText.setText(addr);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        KeyBoardUtil.hideSoftInput(this);
        switch (requestCode) {
            case Constant.REQUEST_CODE_PERMISSION:

                for (String p: permissions) {
                    if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                        ToastUtils.showShort("请允许操作权限");
                        finish();
                    }
                }
                break;
        }
    }
}
