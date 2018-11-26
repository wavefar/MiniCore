package com.lq.cxy.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.lljjcoder.style.citythreelist.ProvinceActivity;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.model.AddressApi;
import com.lq.cxy.shop.model.entity.AddressEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.utils.UiUtil;

import org.greenrobot.eventbus.EventBus;
import org.wavefar.lib.base.BaseSimpleActivity;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.KeyBoardUtil;
import org.wavefar.lib.utils.ToastUtils;

/**
 * 收货地址编辑
 *
 * @author Administrator
 */
public class AddressEditActivity extends BaseSimpleActivity implements View.OnClickListener {
    public static final String KEY_ADDREES_TO_EDIT = "to_be_edit_address";
    AddressApi addrApi;
    private AddressEntity defaultAddress;
    private boolean isUpdateAddress;
    private AppCompatEditText receiveUser, receivePhone, receiveArea;
    private AppCompatEditText summaryArea;
    CityPickerView cityPickerView = new CityPickerView();

    @Override
    protected void getBundle(Bundle bundle) {
        defaultAddress = bundle.getParcelable(KEY_ADDREES_TO_EDIT);
        isUpdateAddress = defaultAddress != null;
    }

    @Override
    @LayoutRes
    protected int getContentView() {
        return R.layout.activity_address_edit;
    }


    @Override
    public void initData() {
        cityPickerView.init(this);
        cityPickerView.setConfig(UiUtil.CITY_CONFIG);
        addrApi = new AddressApi();
        receiveUser = findViewById(R.id.receive_addr_username);
        receivePhone = findViewById(R.id.receive_addr_phone_num);
        receiveArea = findViewById(R.id.receive_addr_area_detail);
        summaryArea = findViewById(R.id.receive_addr_area_sum);
        TextView title = findViewById(R.id.head_title);
        if (defaultAddress != null) {
            receiveUser.setText(defaultAddress.getUserName());
            receivePhone.setText(defaultAddress.getPhoneNum());
            receiveArea.setText(defaultAddress.getAddress());
            title.setText(R.string.receive_change_addr);
        } else {
            title.setText(R.string.receive_add_addr);
        }
        findViewById(R.id.save_address_in_edit).setOnClickListener(this);
        findViewById(R.id.head_back_root).setOnClickListener(this);
        findViewById(R.id.receiver_addr_click_view).setOnClickListener(this);
        findViewById(R.id.head_more).setVisibility(View.GONE);
    }

    @Override
    public void initViewObservable() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_address_in_edit:
                if (defaultAddress == null) {
                    defaultAddress = new AddressEntity();
                }
                String user = receiveUser.getText().toString().trim();
                if (TextUtils.isEmpty(user)) {
                    ToastUtils.showShort("请输入收件人姓名");
                    return;
                }
                String phone = receivePhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showShort("请输入联系电话");
                    return;
                }
                if (province == null || city == null || area == null) {
                    ToastUtils.showShort("请选择地区");
                    return;
                }
                String detail = receiveArea.getText().toString();
                if (province == null || city == null || area == null) {
                    ToastUtils.showShort("请输入详细地址");
                    return;
                }
                defaultAddress.setAddress(AddressEntity.encodeString(province.getName(),
                        city.getName(), area.getName(), detail));
                defaultAddress.setUserName(user);
                defaultAddress.setPhoneNum(phone);
                if (isUpdateAddress) {
                    updateAddress();
                } else {
                    saveAddress();
                }
                break;
            case R.id.head_back_root:
                finish();
                break;
            case R.id.receiver_addr_click_view:
                KeyBoardUtil.hideSoftInput(this);
                cityPickerView.setOnCityItemClickListener(new OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean p, CityBean c, DistrictBean d) {
                        StringBuilder sb = new StringBuilder();
                        province = p;
                        city = c;
                        area = d;
                        if (province != null) {
                            sb.append(province.getName() + " ");
                        }

                        if (city != null) {
                            sb.append(city.getName() + " ");
                        }

                        if (area != null) {
                            sb.append(area.getName());
                        }
                        summaryArea.setText(sb.toString());
                    }
                });
                cityPickerView.showCityPicker();
                break;
            default:
                break;
        }
    }

    private void saveAddress() {
        addrApi.addAddress(defaultAddress, new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                if (response.isSuccess()) {
                    EventBus.getDefault().post(new AddressManageActivity.AddressChangeEvent() {
                    });
                    finish();
                } else {
                    ToastUtils.showShort(response.getMessage());
                }
            }
        });
    }

    private void updateAddress() {
        addrApi.updateAddress(defaultAddress, new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                if (response.isSuccess()) {
                    EventBus.getDefault().post(new AddressManageActivity.AddressChangeEvent() {
                    });
                    finish();
                } else {
                    ToastUtils.showShort(response.getMessage());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ProvinceActivity.RESULT_DATA) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }

            }
        }
    }

    private ProvinceBean province;
    private CityBean city;
    private DistrictBean area;
}
