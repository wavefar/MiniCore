package com.lq.cxy.shop.model.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableDouble;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.activity.merchant.CashToCardActivity;
import com.lq.cxy.shop.activity.merchant.MerPayAccount;
import com.lq.cxy.shop.model.entity.MerchantBindingAccountEntity;
import com.lq.cxy.shop.model.entity.MerchantCashListEntity;
import com.lq.cxy.shop.model.entity.MerchantMoneyEntity;
import com.lq.cxy.shop.model.entity.OrderIncomeEntity;
import com.lq.cxy.shop.model.entity.OrderOutcomeEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.merchant.MerFundApi;
import com.lq.cxy.shop.model.merchant.ShopApi;

import org.wavefar.lib.base.BaseViewModel;
import org.wavefar.lib.binding.command.BindingAction;
import org.wavefar.lib.binding.command.BindingCommand;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.AlertUtils;
import org.wavefar.lib.utils.ToastUtils;

import java.util.Collections;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class MerchantFundFragViewModel extends BaseViewModel implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_ADD_ACCOUNT = 25;
    private static final int REQUEST_CASH_TO_CARD = 39;
    private int currentPage = 1, totalPage = 1;
    public ObservableBoolean isFreshFinished = new ObservableBoolean(true);
    public ObservableBoolean isLoadMoreFinished = new ObservableBoolean(true);
    public final BindingRecyclerViewAdapter<MerchantFundItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ItemBinding<MerchantFundItemViewModel> itemBinding = ItemBinding.of(BR.vm, R.layout.activity_fund_manage_item);
    public ObservableList<MerchantFundItemViewModel> listObservable = new ObservableArrayList<>();
    public ObservableDouble total = new ObservableDouble();
    public ObservableDouble freze = new ObservableDouble();
    public ObservableDouble available = new ObservableDouble();
    //    public double total, freze, available;
    private ShopApi shopApi;
    private boolean isShowIncome;

    public MerchantFundFragViewModel(Fragment fragment) {
        super(fragment);
        shopApi = new ShopApi();
        isShowIncome = true;
        refresh();
    }

    private void refresh() {
        showDialog();
        shopApi.getMyRemainMoney(new BaseResultCallback<BaseResponseEntity<MerchantMoneyEntity>>() {
            @Override
            public void onResponse(BaseResponseEntity<MerchantMoneyEntity> response) {
                dismissDialog();
                total.set(0);
                freze.set(0);
                available.set(0);
                if (response.isSuccess()) {
                    MerchantMoneyEntity moneyEntity = response.getContent();
                    if (moneyEntity != null) {
                        total.set(moneyEntity.getMoney() + moneyEntity.getFrozenMoney());
                        freze.set(moneyEntity.getFrozenMoney());
                        available.set(moneyEntity.getMoney());
                    }
                } else {
                    ToastUtils.showShort(response.getMessage());
                }
            }
        });
        if (isShowIncome) {
            shopApi.getMyIncomeDetailList(currentPage, new BaseResultCallback<BaseResponseEntity<MerchantCashListEntity<OrderIncomeEntity>>>() {
                @Override
                public void onResponse(BaseResponseEntity<MerchantCashListEntity<OrderIncomeEntity>> response) {
                    dismissDialog();
                    resetListLoadState();
                    if (response.isSuccess()) {
                        totalPage = response.getContent().getPages();
                        isFreshFinished.set(!isFreshFinished.get());
                        if (currentPage == 1) {
                            listObservable.clear();
                        }
                        MerchantFundItemViewModel itemViewModel;
                        for (OrderIncomeEntity item: response.getContent().getList()) {
                            itemViewModel = new MerchantFundItemViewModel(context, item);
                            listObservable.add(itemViewModel);
                        }
                    } else {
                        ToastUtils.showShort(response.getMessage());
                    }
                }

                @Override
                public void onError(Throwable e, String body) {
                    super.onError(e, body);
                    dismissDialog();
                }
            });
        } else {
            shopApi.getMyOutcomeDetailList(currentPage, new BaseResultCallback<BaseResponseEntity<MerchantCashListEntity<OrderOutcomeEntity>>>() {
                @Override
                public void onResponse(BaseResponseEntity<MerchantCashListEntity<OrderOutcomeEntity>> response) {
                    dismissDialog();
                    resetListLoadState();
                    if (response.isSuccess()) {
                        totalPage = response.getContent().getPages();
                        isFreshFinished.set(!isFreshFinished.get());
                        if (currentPage == 1) {
                            listObservable.clear();
                        }
                        MerchantFundItemViewModel itemViewModel;
                        for (OrderOutcomeEntity item: response.getContent().getList()) {
                            itemViewModel = new MerchantFundItemViewModel(context, item);
                            listObservable.add(itemViewModel);
                        }
                    } else {
                        ToastUtils.showShort(response.getMessage());
                    }
                }

                @Override
                public void onError(Throwable e, String body) {
                    super.onError(e, body);
                    dismissDialog();
                }
            });
        }


    }

    private void resetListLoadState() {
        if (!isFreshFinished.get()) {
            isFreshFinished.set(true);
        }
        if (!isLoadMoreFinished.get()) {
            isLoadMoreFinished.set(true);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        isShowIncome = menuItem.getItemId() == R.id.detial_income ? true : false;
        refresh();
        return true;
    }

    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isFreshFinished.set(false);
            //重新请求
            currentPage = 1;
            refresh();
        }
    });
    //上拉加载
    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isLoadMoreFinished.set(false);
            if (currentPage >= totalPage) {
                resetListLoadState();
                return;
            }
            currentPage++;
            refresh();
        }
    });

    private boolean isInhanding;

    public void onClickToGetCash(View v) {
        if(Math.abs(available.get()-0.1) < 0.05){
            ToastUtils.showShort("最小提现金额为 0.1 元");
            return;
        }
        if (isInhanding) {
            return;
        }
        isInhanding = true;
        AlertUtils.showProgressDialog(context, "", "加载中...");
        new MerFundApi().loadBindingPayAccount(new BaseResultCallback<BaseResponseEntity<List<MerchantBindingAccountEntity>>>() {
            @Override
            public void onResponse(BaseResponseEntity<List<MerchantBindingAccountEntity>> response) {
                AlertUtils.closeProgressDialog();
                if (response.isSuccess()) {
                    if (response.getContent() == null || response.getContent().size() == 0) {
                        MerPayAccount.bindAccountForResult(fragment, 0, REQUEST_ADD_ACCOUNT);
                    } else {
                        handleSelectAccount(response.getContent(), available.get());
                    }
                } else {
                    ToastUtils.showShort(response.getMessage());
                }
                isInhanding = false;
            }

            @Override
            public void onError(Throwable e, String body) {
                super.onError(e, body);
                isInhanding = false;
            }
        });
    }

    private void handleSelectAccount(List<MerchantBindingAccountEntity> accountList, double cash) {
        if (accountList.size() >= 1) {
            MerchantBindingAccountEntity acc = null;
            for (MerchantBindingAccountEntity t: accountList) {
                if (t.getAccountTypeInt() == 0) {
                    acc = t;
                    break;
                }
            }
            CashToCardActivity.startGetCashForResult(fragment, acc, cash, REQUEST_CASH_TO_CARD);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CASH_TO_CARD:
                if (resultCode == Activity.RESULT_OK) {
                    refresh();
                }
                break;
            case REQUEST_ADD_ACCOUNT:
                if (resultCode == Activity.RESULT_OK) {
                    MerchantBindingAccountEntity accountEntity = data.getParcelableExtra(MerPayAccount.RESULT_EXTRA_ACCOUNT);
                    handleSelectAccount(Collections.singletonList(accountEntity), available.get());
                }
                break;
        }
    }
}
