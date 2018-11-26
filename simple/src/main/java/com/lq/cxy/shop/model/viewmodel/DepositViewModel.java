package com.lq.cxy.shop.model.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.fragment.DepositFragment;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.model.entity.DepositEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.http.base.BaseSimpleResultCallback;
import com.lq.cxy.shop.utils.PayUtil;

import org.wavefar.lib.base.BaseViewModel;
import org.wavefar.lib.binding.command.BindingAction;
import org.wavefar.lib.binding.command.BindingCommand;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.IntentUtils;
import org.wavefar.lib.utils.JsonUtils;
import org.wavefar.lib.utils.ToastUtils;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 押金订单列表
 * @author summer
 * @date 2018/9/9 下午5:48
 */
public class DepositViewModel extends BaseViewModel{
    private boolean mIsRefresh = true;
    private UserAPI mUserAPI;
    private PayUtil mPayUtil;
    public DepositViewModel(Context context) {
        super(context);
        mUserAPI = new UserAPI();
        mPayUtil = new PayUtil((Activity) context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        requestData();
    }

    /**
     * 封装一个界面发生改变的观察者
     */
    public UIChangeObservable uc = new UIChangeObservable();


    public class UIChangeObservable {
        /**
         * 下拉刷新完成的观察者
         */
        public ObservableBoolean isFinishRefreshing = new ObservableBoolean(false);
        /**
         * 上拉加载完成的观察者
         */
        public ObservableBoolean isFinishLoadmore = new ObservableBoolean(false);
    }

    /**
     * 给RecyclerView添加ObservableList
     */
    public ObservableList<DepositEntity> observableList = new ObservableArrayList<>();
    /**
     * 给RecyclerView添加ItemBinding
     */
    public ItemBinding<DepositEntity> itemBinding = ItemBinding.<DepositEntity>of(BR.orderEntity, R.layout.item_deposit)
            .bindExtra(BR.listener,this);
    /**
     * 下拉刷新
     */
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //重新请求
            refreshData();
        }
    });
    /**
     *  加载更多
     */
    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            mIsRefresh = false;
            requestData();
        }
    });

    /**
     * 请求数据
     */
    private void requestData() {
        mUserAPI.getMyDeposit(new BaseResultCallback<BaseResponseEntity<List<DepositEntity>>>() {

            @Override
            public void onResponse(BaseResponseEntity<List<DepositEntity>> response) {
                if (mIsRefresh) {
                    uc.isFinishRefreshing.set(!uc.isFinishRefreshing.get());
                }
                if (response.isSuccess()) {
                    observableList.clear();
                    List<DepositEntity> depositEntities = response.getContent();
                    observableList.addAll(depositEntities);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        observableList.clear();
        observableList = null;
    }


    /**
     * 取消订单
     */
    public void cancelOrder(final DepositEntity orderEntity) {
        mUserAPI.cancelDeposit(orderEntity.getId(), new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                ToastUtils.showShort(response.getMessage());
                if (response.isSuccess()) {
                    refreshData();
                }
            }
        });
    }

    /**
     * 退押金
     * @param depositEntity
     */
    public void refundDeposit(DepositEntity depositEntity) {
        mUserAPI.refundDeposit(depositEntity.getId(), new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                ToastUtils.showShort(response.getMessage());
                if (response.isSuccess()) {
                    refreshData();
                }
            }
        });
    }

    /**
     * 押金订单支付
     */
    public void payOrder(final DepositEntity orderEntity) {

        mUserAPI.depositPay(orderEntity.getId(), new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                if (response.isSuccess()) {
                    mPayUtil.pay(String.valueOf(response.getContent()));
                    mPayUtil.setPayCallback(new PayUtil.PayCallback() {
                        @Override
                        public void onSucceed(PayUtil.PayResult payResult) {
                            refreshData();
                        }

                        @Override
                        public void onFailure(PayUtil.PayResult payResult) {

                        }
                    });
                } else {
                    ToastUtils.showShort(response.getMessage());
                }
            }
        });
    }

    /**
     * 押金下单
     * @param orderEntity
     */
    public void submitDeposit(DepositEntity orderEntity) {

        mUserAPI.saveDeposit(orderEntity, new BaseSimpleResultCallback<BaseResponseEntity<DepositEntity>,DepositEntity>() {
            @Override
            public void onSucceed(DepositEntity entity) {
                if (entity != null) {
                    payOrder(entity);
                }
            }

            @Override
            public void onError(Throwable e, String body) {
                if ( body != null) {
                    BaseResponseEntity baseStatus = JsonUtils.fromJson(body,BaseResponseEntity.class);
                    if (baseStatus != null  && -1024 == baseStatus.getCode()) {
                        ToastUtils.showShort(baseStatus.getMessage());
                        IntentUtils.startContainerActivity(DepositFragment.class.getName());
                    }
                }
            }
        });
    }

    private void refreshData() {
        //重新请求
        mIsRefresh = true;
        requestData();
    }
}
