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
import com.lq.cxy.shop.model.FavoriteGoodsAPI;
import com.lq.cxy.shop.model.entity.FavGoodsListEntity;
import com.lq.cxy.shop.model.entity.ProductEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.http.base.BaseSimpleResultCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.wavefar.lib.base.BaseViewModel;
import org.wavefar.lib.binding.command.BindingAction;
import org.wavefar.lib.binding.command.BindingCommand;
import org.wavefar.lib.utils.ToastUtils;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 收藏model
 * @author summer
 */
public class FavGoodsListViewModel extends BaseViewModel implements OnItemClickListener<ProductEntity> {
    private FavoriteGoodsAPI favAPI;
    private int pageNum = 1;
    private FavGoodsListEntity listEntity;

    public FavGoodsListViewModel(Context context) {
        super(context);
        favAPI = new FavoriteGoodsAPI();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadFavList(pageNum);
    }

    private void loadFavList(int page) {
        favAPI.loadFavList(page, new BaseSimpleResultCallback<BaseResponseEntity<FavGoodsListEntity>,FavGoodsListEntity>() {

            @Override
            public void onSucceed(FavGoodsListEntity entity) {
                resetListLoadState();
                listEntity  = entity;
                isFreshFinished.set(!isFreshFinished.get());
                if (pageNum == 1) {
                    observableList.clear();
                }

                //请求成功
                if (null != entity.getList() && entity.getList().size() > 0) {
                    observableList.addAll(entity.getList());
                }
            }
        });
    }

    private void resetListLoadState() {
        if (isFreshFinished.get()) {
            isFreshFinished.set(true);
        }
        if (isLoadMoreFinished.get()) {
            isLoadMoreFinished.set(true);
        }
    }

    public ObservableList<ProductEntity> observableList = new ObservableArrayList<>();
    public ObservableBoolean isFreshFinished = new ObservableBoolean(true);
    public ObservableBoolean isLoadMoreFinished = new ObservableBoolean(true);
    public final BindingRecyclerViewAdapter<ProductEntity> adapter = new BindingRecyclerViewAdapter<>();
    public ItemBinding<ProductEntity> itemBinding = ItemBinding.<ProductEntity>of(BR.productEntity, R.layout.item_good)
            .bindExtra(BR.listener,this);

    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isFreshFinished.set(false);
            ToastUtils.showShort("下拉刷新");
            //重新请求
            pageNum = 1;
            loadFavList(pageNum);
        }
    });
    //上拉加载
    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isLoadMoreFinished.set(false);
            if (listEntity != null && listEntity.getPages() > pageNum) {
                pageNum++;
                loadFavList(pageNum);
            }
        }
    });

    @Subscribe
    public void handleFavCheckChanged(FavCheckedChangedEvent event) {
        pageNum = 1;
        loadFavList(pageNum);
    }

    @Override
    public void onItemClick(ProductEntity entity) {
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(GoodsDetailBaseFrag.KEY_GOODS_DETAIL, entity);
        startActivity(GoodsDetailActivity.class, mBundle);
    }

    public interface FavCheckedChangedEvent {
    }
}
