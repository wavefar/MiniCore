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
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bumptech.glide.Glide;
import com.lq.cxy.shop.Constant;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.activity.LocationActivity;
import com.lq.cxy.shop.databinding.ActivitySellerApplyBinding;
import com.lq.cxy.shop.fragment.MeFragment;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.model.entity.BrandEntity;
import com.lq.cxy.shop.model.entity.LicenseInfoEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.merchant.LicenseSupplyApi;
import com.lq.cxy.shop.utils.UiUtil;
import com.lq.cxy.shop.utils.Util;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;

import org.greenrobot.eventbus.EventBus;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.IDCardUtil;
import org.wavefar.lib.utils.KeyBoardUtil;
import org.wavefar.lib.utils.TimeUtil;
import org.wavefar.lib.utils.ToastUtils;

import java.io.File;
import java.util.Date;
import java.util.List;

public class LicenseSupply extends AppCompatActivity implements View.OnClickListener {


    private ActivitySellerApplyBinding binding;
    private LicenseInfoEntity requestEntity = new LicenseInfoEntity();
    private boolean isAdd = true;
    ISListConfig config;
    private int curCateId = 0;
    private List<BrandEntity> cateList;
    private List<String> nameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constant.REQUEST_CODE_PERMISSION);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_seller_apply);
        binding.sellerApplyImgRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.sellerApplyImgRoot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) binding.sellerApplyImgRoot.getLayoutParams();
                lp.height = binding.sellerApplyImgRoot.getWidth() / 2;
                lp.width = binding.sellerApplyImgRoot.getWidth();
                binding.sellerApplyImgRoot.setLayoutParams(lp);
            }
        });
        setDataWithEntity(null);
        config = UiUtil.getImageSelectorConfig(getApplicationContext(), 2, 1, 800, 400);
        Util.loadCategory();
        Util.loadAndCacheLisence(new BaseResultCallback<BaseResponseEntity<LicenseInfoEntity>>() {
            @Override
            public void onResponse(BaseResponseEntity<LicenseInfoEntity> response) {
                if (response.isSuccess()) {
                    isAdd = response.getContent() == null;
                    setDataWithEntity(response.getContent());
                } else {
                    ToastUtils.showShort(response.getMessage());
                    finish();
                }
            }
        });
        KeyBoardUtil.hideSoftInput(this);
    }

    private void setDataWithEntity(LicenseInfoEntity entity) {
        TextView title = binding.sellerApplyHead.findViewById(R.id.head_title);
        binding.sellerApplyHead.findViewById(R.id.head_back_root).setOnClickListener(this);
        binding.sellerApplyHead.findViewById(R.id.head_more).setVisibility(View.GONE);
        binding.sellerApplyImgRoot.setOnClickListener(this);
        binding.sellerBusDurationStartRoot.setOnClickListener(this);
        binding.sellerBusDurationEndRoot.setOnClickListener(this);
        binding.applySellerShopCardType.setOnClickListener(this);
        binding.noticeSellerShopCardType.setOnClickListener(this);
        binding.applySellerLicenseType.setOnClickListener(this);
        binding.applySellerShopFundDate.setOnClickListener(this);
        binding.applySellerShopAddr.setOnClickListener(this);
        binding.clickToSave.setOnClickListener(this);
        binding.clickToSubmitPublish.setOnClickListener(this);
        binding.applySellerShopScope.setOnClickListener(this);
        if (entity == null) {
            title.setText(R.string.apply_merchant);
            binding.applySellerShopFundDate.setText(
                    TimeUtil.dateTime2String(System.currentTimeMillis(), Constant.FORMAT_DATE));
            binding.applySellerBusDurationStart.setText(
                    TimeUtil.dateTime2String(System.currentTimeMillis(), Constant.FORMAT_DATE));
            binding.applySellerBusDurationEnd.setText(
                    TimeUtil.dateTime2String(System.currentTimeMillis(), Constant.FORMAT_DATE));
            binding.applySellerShopCardType.setText(LicenseInfoEntity.ID_TYPE_STR.get(requestEntity.getIdType()));
            binding.applySellerLicenseType.setText(LicenseInfoEntity.LISENCE_TYPE_STR.get(requestEntity.getLicenseType()));
            return;
        } else {
            requestEntity = entity;
            boolean enableView = entity.getStatus() == 0 || entity.getStatus() == 2;
            binding.applySellerShopName.setEnabled(enableView);
            binding.applySellerShopAddr.setEnabled(enableView);
            binding.applySellerShopScope.setEnabled(enableView);
            binding.applySellerShopFundDate.setEnabled(enableView);
            binding.applySellerShopStartMoney.setEnabled(enableView);
            binding.applySellerShopLegalName.setEnabled(enableView);
            binding.applySellerShopCardType.setEnabled(enableView);
            binding.applySellerCardNum.setEnabled(enableView);
            binding.applySellerLicenseNum.setEnabled(enableView);
            binding.applySellerLicenseAddress.setEnabled(enableView);
            binding.applySellerBusDurationStart.setEnabled(enableView);
            binding.applySellerBusDurationEnd.setEnabled(enableView);
            binding.applySellerLicenseType.setEnabled(enableView);
            binding.applySellerEmContact.setEnabled(enableView);
            binding.applySellerEmPhone.setEnabled(enableView);
            binding.sellerApplyImgRoot.setEnabled(enableView);
            binding.applySellerShopScope.setEnabled(enableView);
            binding.noticeSellerShopCardType.setEnabled(enableView);
            binding.sellerBusDurationStartRoot.setEnabled(enableView);
            binding.sellerBusDurationEndRoot.setEnabled(enableView);
            binding.clickToSubmitPublish.setVisibility(enableView ? View.VISIBLE : View.GONE);
            binding.clickToSave.setVisibility(enableView ? View.VISIBLE : View.GONE);
//            editApplyState.setEnabled(enableView);
//            editApplyState.setVisibility(enableView ? View.VISIBLE : View.GONE);

            title.setText(R.string.apply_merchant_update);
            binding.applySellerShopName.setText(entity.getCompanyName());
            binding.applySellerShopAddr.setText(entity.getCompanyAddress());
            binding.applySellerShopScope.setText(entity.getScope());
            binding.applySellerShopFundDate.setText(
                    TimeUtil.dateTime2String(Long.valueOf(entity.getEstablishTime()), Constant.FORMAT_DATE));
            binding.applySellerShopStartMoney.setText(String.valueOf(entity.getCapital()));
            binding.applySellerShopLegalName.setText(entity.getLegalName());
            binding.applySellerShopCardType.setText(LicenseInfoEntity.ID_TYPE_STR.get(entity.getIdType()));
            binding.applySellerCardNum.setText(entity.getLegalNum());
            binding.applySellerLicenseNum.setText(entity.getLicenseNum());
            binding.applySellerLicenseAddress.setText(entity.getLicenseAddress());
            binding.applySellerBusDurationStart.setText(TimeUtil.dateTime2String(Long.valueOf(entity.getBeginTime()), Constant.FORMAT_DATE));
            binding.applySellerBusDurationEnd.setText(TimeUtil.dateTime2String(Long.valueOf(entity.getEndTime()), Constant.FORMAT_DATE));
            binding.applySellerLicenseType.setText(LicenseInfoEntity.LISENCE_TYPE_STR.get(entity.getLicenseType()));
            binding.applySellerEmContact.setText(entity.getEmContact());
            binding.applySellerEmPhone.setText(entity.getEmContactPhone());
            Glide.with(getApplicationContext()).load(entity.getLicenseImg()).into(binding.applyBusImage);
        }

    }

    @Override
    public void onClick(View view) {
        KeyBoardUtil.hideSoftInput(this);
        switch (view.getId()) {
            case R.id.applySellerShopScope:
                cateList = Util.getCategoryList();
                nameList = Util.getCategoryNameList(cateList);
                UiUtil.showOptionDialog(this, nameList, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        binding.applySellerShopScope.setText(nameList.get(options1));
                    }
                });
                break;
            case R.id.head_back_root:
                finish();
                break;
            case R.id.clickToSubmitPublish:
                // here to add publish code.
                checkAndSave(true);
                break;
            case R.id.clickToSave:
                checkAndSave(false);
                break;
            case R.id.applySellerShopAddr:
                startActivityForResult(new Intent(this, LocationActivity.class), LocationActivity.GET_LOCATION_CODE);
                break;
            case R.id.sellerApplyImgRoot:
                ISNav.getInstance().toListActivity(this, config, Constant.REQUEST_CODE_CHOOSE);
                break;
            case R.id.sellerBusDurationStartRoot:
                UiUtil.showDateSelect(this, false, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String result = TimeUtil.dateTime2String(date, Constant.FORMAT_DATE);
                        requestEntity.setBeginTime(result);
                        binding.applySellerBusDurationStart.setText(result);
                    }
                });
                break;
            case R.id.sellerBusDurationEndRoot:
                UiUtil.showDateSelect(this, false, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String result = TimeUtil.dateTime2String(date, Constant.FORMAT_DATE);
                        requestEntity.setEndTime(result);
                        binding.applySellerBusDurationEnd.setText(result);
                    }
                });
                break;
            case R.id.applySellerShopFundDate:
                UiUtil.showDateSelect(this, false, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String result = TimeUtil.dateTime2String(date, Constant.FORMAT_DATE);
                        requestEntity.setEstablishTime(result);
                        binding.applySellerShopFundDate.setText(result);
                    }
                });
                break;
            case R.id.noticeSellerShopCardType:
            case R.id.applySellerShopCardType:
                UiUtil.showOptionDialog(this, LicenseInfoEntity.ID_TYPE_STR, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        requestEntity.setIdType(options1);
                        binding.applySellerShopCardType.setText(LicenseInfoEntity.ID_TYPE_STR.get(options1));
                    }
                });
                break;

            case R.id.applySellerLicenseType:
                UiUtil.showOptionDialog(this, LicenseInfoEntity.LISENCE_TYPE_STR, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        requestEntity.setLicenseType(options1);
                        binding.applySellerLicenseType.setText(LicenseInfoEntity.LISENCE_TYPE_STR.get(options1));
                    }
                });
                break;

        }
    }

    private void checkAndSave(final boolean isPublish) {
        String cName = binding.applySellerShopName.getText().toString().trim();
        if (TextUtils.isEmpty(cName)) {
            ToastUtils.showShort(R.string.please_input_company_name_str);
            return;
        }
        String cAddr = binding.applySellerShopAddr.getText().toString().trim();
        if (TextUtils.isEmpty(cAddr)) {
            ToastUtils.showShort(R.string.please_input_company_addr_str);
            return;
        }
        String scope = binding.applySellerShopScope.getText().toString().trim();
        if (TextUtils.isEmpty(scope)) {
            ToastUtils.showShort(R.string.please_input_company_scope_str);
            return;
        }
        String capital = binding.applySellerShopStartMoney.getText().toString().trim();
        if (TextUtils.isEmpty(capital)) {
            ToastUtils.showShort(R.string.please_input_company_capital_str);
            return;
        }
        String legalName = binding.applySellerShopLegalName.getText().toString().trim();
        if (TextUtils.isEmpty(legalName)) {
            ToastUtils.showShort(R.string.please_input_legal_name_str);
            return;
        }
        String legalNum = binding.applySellerCardNum.getText().toString().trim();
        if (TextUtils.isEmpty(legalNum)) {
            ToastUtils.showShort(R.string.please_input_legal_num_str);
            return;
        } else {
            if (!IDCardUtil.verify(legalNum)) {
                ToastUtils.showShort(R.string.please_input_valid_legal_num_str);
                return;
            }
        }
        String lNum = binding.applySellerLicenseNum.getText().toString().trim();
//        if (TextUtils.isEmpty(lNum)) {
//            ToastUtils.showShort(R.string.please_input_license_num_str);
//            return;
//        } else {
//            if (!Validator.isLicense15(lNum) && !Validator.isLicense18(lNum)) {
//                ToastUtils.showShort(R.string.please_input_valid_license_num_str);
//            }
//        }
        String lAddr = binding.applySellerLicenseAddress.getText().toString().trim();
        if (TextUtils.isEmpty(lAddr)) {
            ToastUtils.showShort(R.string.please_input_license_addr_str);
            return;
        }

        String startTimeStr = binding.applySellerBusDurationStart.getText().toString().trim();
        String endTimeStr = binding.applySellerBusDurationEnd.getText().toString().trim();
        Date start = TimeUtil.parseStringToDate(startTimeStr, Constant.FORMAT_DATE);
        Date end = TimeUtil.parseStringToDate(endTimeStr, Constant.FORMAT_DATE);
        if (start.after(end)) {
            ToastUtils.showShort(R.string.end_time_must_bigger_than_start);
            return;
        }

//        if (isAdd && requestEntity.getFile() == null) {
//            ToastUtils.showShort(R.string.please_input_em_name_str);
//            return;
//        }
        String eContact = binding.applySellerEmContact.getText().toString().trim();
        if (TextUtils.isEmpty(eContact)) {
            ToastUtils.showShort(R.string.please_input_em_name_str);
            return;
        }
        String ePhone = binding.applySellerEmPhone.getText().toString().trim();
        if (TextUtils.isEmpty(ePhone)) {
            ToastUtils.showShort(R.string.please_input_em_phone_str);
            return;
        }
        requestEntity.setCompanyName(cName);
        requestEntity.setCompanyAddress(cAddr);
        requestEntity.setScope(scope);
        requestEntity.setCapital(Integer.valueOf(capital));
        requestEntity.setLegalName(legalName);
        requestEntity.setLegalNum(legalNum);
        requestEntity.setCustomerId(UserAPI.getUserInfo().getUserId());
        requestEntity.setLicenseNum(lNum);
        requestEntity.setLicenseAddress(lAddr);
        requestEntity.setEmContact(eContact);
        requestEntity.setEmContactPhone(ePhone);
        requestEntity.setBeginTime(startTimeStr);
        requestEntity.setEndTime(endTimeStr);
        requestEntity.setEstablishTime(binding.applySellerShopFundDate.getText().toString().trim());
        new LicenseSupplyApi().submitLicenseInfo(requestEntity, isAdd, new BaseResultCallback<BaseResponseEntity<LicenseInfoEntity>>() {
            @Override
            public void onResponse(BaseResponseEntity<LicenseInfoEntity> response) {
                if (response.isSuccess()) {
                    if (isPublish) {
                        doPublishData(response.getContent());
                    } else {
                        EventBus.getDefault().post(new MeFragment.RefreshUserDetailEvent() {
                        });
                        ToastUtils.showShort("资料保存成功");
                        finish();
                    }
                } else {
                    ToastUtils.showShort(response.getMessage());
                }
            }
        });
    }

    private void doPublishData(LicenseInfoEntity data) {
        new LicenseSupplyApi().publishLicenseInfo(data.getId(), new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                if (response.isSuccess()) {
                    EventBus.getDefault().post(new MeFragment.RefreshUserDetailEvent() {
                    });
                    ToastUtils.showShort("申请提交成功");
                } else {
                    ToastUtils.showShort(response.getMessage());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 图片选择结果回调
        if (requestCode == Constant.REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            for (String path: pathList) {
                Log.d("Matisse", "mSelected: " + pathList);
                requestEntity.setFile(new File(path));
                binding.applyBusImage.setImageURI(Uri.fromFile(new File(path)));
            }
        }
        if (requestCode == LocationActivity.GET_LOCATION_CODE && resultCode == RESULT_OK) {
            Bundle locExtra = data.getExtras();
            String addr = locExtra.getString(LocationActivity.EXTRA_KEY_ADDRESS);
            binding.applySellerShopAddr.setText(addr);
        }
    }
}