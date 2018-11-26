package com.lq.cxy.shop.model.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.os.Bundle;

import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.activity.GoodsDetailActivity;
import com.lq.cxy.shop.adapter.OnItemClickListener;
import com.lq.cxy.shop.fragment.GoodsDetailBaseFrag;
import com.lq.cxy.shop.model.GoodsAPI;
import com.lq.cxy.shop.model.entity.ProductListEntity;
import com.lq.cxy.shop.model.entity.ProductEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.http.base.BaseSimpleResultCallback;

import org.wavefar.lib.base.BaseViewModel;
import org.wavefar.lib.binding.command.BindingAction;
import org.wavefar.lib.binding.command.BindingCommand;
import org.wavefar.lib.utils.ToastUtils;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;


/**
 * 商品列表
 * @author summer
 */
public class GoodViewModel extends BaseViewModel implements OnItemClickListener<ProductEntity> {
    private int mPageNum = 1;
    private boolean mIsRefresh = true;
    private String keyWords;
    public GoodViewModel(Context context) {
        super(context);
    }
    private GoodsAPI mGoodsAPI;
    @Override
    public void onCreate() {
        super.onCreate();
        mGoodsAPI = new GoodsAPI();
        requestData();
    }


    /**
     * 封装一个界面发生改变的观察者
     */
    public UIChangeObservable uc = new UIChangeObservable();

    @Override
    public void onItemClick(ProductEntity productEntity) {
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(GoodsDetailBaseFrag.KEY_GOODS_DETAIL, productEntity);
        startActivity(GoodsDetailActivity.class, mBundle);
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
    public ObservableList<ProductEntity> observableList = new ObservableArrayList<>();
    /**
     * 给RecyclerView添加ItemBinding
     */
    public ItemBinding<ProductEntity> itemBinding = ItemBinding.<ProductEntity>of(BR.productEntity, R.layout.item_good)
            .bindExtra(BR.listener,this);

    /**
     * 给RecyclerView添加Adpter，请使用自定义的Adapter继承BindingRecyclerViewAdapter，重写onBindBinding方法
     */
    public final BindingRecyclerViewAdapter<ProductEntity> adapter = new BindingRecyclerViewAdapter<>();

    /**
     * 下拉刷新
     */
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //重新请求
            mIsRefresh = true;
            mPageNum = 1;
            requestData();
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
        mGoodsAPI.getGoods(mPageNum,keyWords, new BaseSimpleResultCallback<BaseResponseEntity<ProductListEntity>,ProductListEntity>() {

            @Override
            public void onSucceed(ProductListEntity entity) {
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

    /**
     * 设置搜索关键字
     * @param keyWords
     */
    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
        mPageNum = 1;
        mIsRefresh = true;
        requestData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        observableList.clear();
        observableList = null;
    }


}
