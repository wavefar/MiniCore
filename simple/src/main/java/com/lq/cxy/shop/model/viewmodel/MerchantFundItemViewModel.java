package com.lq.cxy.shop.model.viewmodel;

import android.content.Context;

import com.lq.cxy.shop.model.entity.OrderIncomeEntity;
import com.lq.cxy.shop.model.entity.OrderOutcomeEntity;

import org.wavefar.lib.base.BaseViewModel;

public class MerchantFundItemViewModel extends BaseViewModel {
    public String nameStr;
    public double money;
    public final boolean isIncome;
    public MerchantFundItemViewModel(Context context, OrderIncomeEntity entity) {
        super(context);
        nameStr = OrderIncomeEntity.getMoneyTypeString(entity.getMoneyType());
        money = entity.getMoney();
        isIncome = true;
    }

    public MerchantFundItemViewModel(Context context, OrderOutcomeEntity entity) {
        super(context);
        nameStr = entity.getRealName();
        money = entity.getMoney();
        isIncome = false;
    }
}
