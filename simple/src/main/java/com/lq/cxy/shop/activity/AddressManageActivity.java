package com.lq.cxy.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.databinding.ActivityAddressManageBinding;
import com.lq.cxy.shop.model.viewmodel.AddressListViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.wavefar.lib.base.BaseActivity;

/**
 * 收货地址管理
 * @author Administrator
 */
public class AddressManageActivity extends BaseActivity<ActivityAddressManageBinding,
        AddressListViewModel> implements View.OnClickListener {
    public static final String KEY_IS_SELECT_MODE = "key_is_select";
    private boolean isSeletedMode;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_address_manage;
    }

    @Override
    public int initVariableId() {
        return BR.addressManViewModel;
    }

    @Override
    public AddressListViewModel initViewModel() {
        return new AddressListViewModel(this);
    }

    @Override
    protected void getBundle(Bundle bundle) {
        if (bundle != null && bundle.containsKey(KEY_IS_SELECT_MODE)) {
            isSeletedMode = bundle.getBoolean(KEY_IS_SELECT_MODE);
            viewModel.setSelectedMode(isSeletedMode);
        }
    }

    @Override
    public void initData() {
        binding.createNewAddrRoot.setOnClickListener(this);
        binding.addressHeadRoot.findViewById(R.id.head_back_root).setOnClickListener(this);
        binding.addressHeadRoot.findViewById(R.id.head_more).setVisibility(View.GONE);
        TextView title = binding.addressHeadRoot.findViewById(R.id.head_title);
        title.setText(R.string.receive_address_manage_title);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_new_addr_root:
                startActivity(new Intent(this, AddressEditActivity.class));
                break;
            case R.id.head_back_root:
                finish();
                break;
                default:
                    break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddressChanges(AddressChangeEvent event) {
        viewModel.loadAddressList();
    }

    public interface AddressChangeEvent {
    }

}
