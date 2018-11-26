package com.lq.cxy.shop.model.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.os.Bundle;

import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.Constant;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.adapter.OnItemClickListener;
import com.lq.cxy.shop.fragment.OrderDetailFragment;
import com.lq.cxy.shop.fragment.OrderFragment;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.model.entity.OrderEntity;
import com.lq.cxy.shop.model.entity.OrderListEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.http.base.BaseSimpleResultCallback;
import com.lq.cxy.shop.utils.PayUtil;
import com.lq.cxy.shop.widget.PostEvaluateDialog;

import org.wavefar.lib.base.BaseViewModel;
import org.wavefar.lib.binding.command.BindingAction;
import org.wavefar.lib.binding.command.BindingCommand;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.IntentUtils;
import org.wavefar.lib.utils.ToastUtils;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 订单列表
 * @author summer
 * @date 2018/9/9 下午5:48
 */
public class OrderViewModel extends BaseViewModel implements OnItemClickListener<OrderEntity>{
    private int mPageNum = 1;
    private boolean mIsRefresh = true;
    private int orderType;
    public OrderViewModel(Context context,int orderType) {
        super(context);
        this.orderType = orderType;
    }
    private UserAPI mUserAPI;
    private PayUtil mPayUtil;
    @Override
    public void onCreate() {
        super.onCreate();
        mUserAPI = new UserAPI();
        mPayUtil = new PayUtil((Activity) context);
        requestData();
    }


    /**
     * 封装一个界面发生改变的观察者
     */
    public UIChangeObservable uc = new UIChangeObservable();

    @Override
    public void onItemClick(OrderEntity entity) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(OrderDetailFragment.ORDER_KEY, entity);
        startContainerActivity(OrderDetailFragment.class.getName(),bundle);
    }


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
    public ObservableList<OrderEntity> observableList = new ObservableArrayList<>();
    /**
     * 给RecyclerView添加ItemBinding
     */
    public ItemBinding<OrderEntity> itemBinding = ItemBinding.<OrderEntity>of(BR.orderEntity, R.layout.item_order)
            .bindExtra(BR.listener,this);

    /**
     * 给RecyclerView添加Adpter，请使用自定义的Adapter继承BindingRecyclerViewAdapter，重写onBindBinding方法
     */
    public final BindingRecyclerViewAdapter<OrderEntity> adapter = new BindingRecyclerViewAdapter<>();

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
        mUserAPI.getOrders(mPageNum,orderType,new BaseSimpleResultCallback<BaseResponseEntity<OrderListEntity>,OrderListEntity>() {

            @Override
            public void onSucceed(OrderListEntity entity) {
                //关闭对话框
                dismissDialog();
                //刷新数据
                if (mIsRefresh) {
                    observableList.clear();
                    uc.isFinishRefreshing.set(!uc.isFinishRefreshing.get());
                } else {
                    uc.isFinishLoadmore.set(!uc.isFinishLoadmore.get());
                    if (entity.getList().size() == 0) {
                        ToastUtils.showShort("没有更多数据啦");
                    }
                }

                //请求成功
                if (null != entity.getList() && entity.getList().size() > 0) {
                    observableList.addAll(entity.getList());
                }
                mPageNum++;
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
     * 确定收货
     */
    public void confirmOrder(final OrderEntity orderEntity) {
        mUserAPI.finishOrder(orderEntity.getId(), new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                ToastUtils.showShort(response.getMessage());
                if (response.isSuccess()) {
                    orderEntity.setStatus(4);
                    refreshData();
                }
            }
        });
    }

    /**
     * 评分
     */
    public void postEvaluate(final OrderEntity orderEntity) {
            new PostEvaluateDialog(context,orderEntity).show();
    }

    /**
     * 取消订单
     */
    public void cancelOrder(final OrderEntity orderEntity) {
        mUserAPI.cancelOrder(orderEntity.getId(), new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                ToastUtils.showShort(response.getMessage());
                if (response.isSuccess()) {
                    orderEntity.setStatus(4);
                    refreshData();
                }
            }
        });
    }

    /**
     * 查询运单信息
     * @param orderEntity
     */
    public void myExpress(OrderEntity orderEntity ){
        IntentUtils.startWeb(String.format(Constant.MY_EXPRESS,orderEntity.getWaybillId()));
    }

    /**
     * 支付订单
     */
    public void payOrder(final OrderEntity orderEntity) {

        mUserAPI.pay(orderEntity.getId(), new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                if (response.isSuccess()) {
                    mPayUtil.pay(String.valueOf(response.getContent()));
                    mPayUtil.setPayCallback(new PayUtil.PayCallback() {
                        @Override
                        public void onSucceed(PayUtil.PayResult payResult) {
                            orderEntity.setStatus(1);
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


    private void refreshData() {
        //重新请求
        mIsRefresh = true;
        mPageNum = 1;
        requestData();
    }
}
