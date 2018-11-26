package com.lq.cxy.shop.model.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.util.Log;

import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.Constant;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.model.entity.MerchantAssesItemEntity;
import com.lq.cxy.shop.model.entity.MerchantAssessEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.merchant.AssessApi;

import org.wavefar.lib.base.BaseViewModel;
import org.wavefar.lib.binding.command.BindingAction;
import org.wavefar.lib.binding.command.BindingCommand;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.KeyBoardUtil;
import org.wavefar.lib.utils.ToastUtils;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class MerchantAssessReplyViewModel extends BaseViewModel implements MerchantAssessReplyItemViewModel.OnSaveListener {

    public ObservableList<MerchantAssessReplyItemViewModel> listObservable = new ObservableArrayList<>();
    public ObservableBoolean isFreshFinished = new ObservableBoolean(true);
    public ObservableBoolean isLoadMoreFinished = new ObservableBoolean(true);
    public final BindingRecyclerViewAdapter<MerchantAssessReplyItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ItemBinding<MerchantAssessReplyItemViewModel> itemBinding = ItemBinding.of(BR.vm, R.layout.activity_merchant_assess_reply_item);

    private AssessApi assessApi;
    private MerchantAssessEntity entity;
    private int curPage = 1, totalPage;

    public MerchantAssessReplyViewModel(Context context) {
        super(context);
        assessApi = new AssessApi();
        loadData();
    }

    private void loadData() {
        showDialog("加载中");
        assessApi.loadWaitReplyList(curPage, new BaseResultCallback<BaseResponseEntity<MerchantAssessEntity>>() {
            @Override
            public void onResponse(BaseResponseEntity<MerchantAssessEntity> response) {
                resetListLoadState();
                dismissDialog();
                if (response.isSuccess()) {
                    entity = response.getContent();
                    totalPage = entity.getSize() / Constant.PAGE_SIZE + (entity.getSize() % Constant.PAGE_SIZE == 0 ? 0 : 1);
                    fillData();
                } else {
                }
            }

            @Override
            public void onError(Throwable e, String body) {
                super.onError(e, body);
                resetListLoadState();
                dismissDialog();
            }
        });
    }

    private void fillData() {
        if (curPage == 1) {
            listObservable.clear();
        }
        if (entity != null) {
            MerchantAssessReplyItemViewModel replyVM;
            for (MerchantAssesItemEntity itemEntity: entity.getList()) {
                replyVM = new MerchantAssessReplyItemViewModel(context, itemEntity);
                replyVM.setListener(this);
                listObservable.add(replyVM);
            }
        }
    }

    private void resetListLoadState() {
        if (isFreshFinished.get()) {
            isFreshFinished.set(true);
        }
        if (isLoadMoreFinished.get()) {
            isLoadMoreFinished.set(true);
        }
    }

    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isFreshFinished.set(false);
            curPage = 1;
            loadData();
        }
    });
    //上拉加载
    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isLoadMoreFinished.set(false);
            if (curPage >= totalPage) {
                resetListLoadState();
                return;
            }
            curPage++;
            loadData();
        }
    });


    @Override
    public void saveReply(String msg, MerchantAssesItemEntity data) {
        assessApi.saveMerchantReply(data, msg, new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                KeyBoardUtil.hideSoftInput((Activity) context);
                if (response.isSuccess()) {
                    curPage = 1;
                    loadData();
                }else {
                    ToastUtils.showShort("回复失败，请稍后重试");
                }

            }
        });
    }
}
