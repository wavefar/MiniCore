package com.lq.cxy.shop.model.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.v4.app.Fragment;

import com.baidu.location.BDLocation;
import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.model.PublicApi;
import com.lq.cxy.shop.model.entity.StoreEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.http.base.BaseSimpleResultCallback;

import org.wavefar.lib.binding.command.BindingAction;
import org.wavefar.lib.binding.command.BindingCommand;

import java.util.ArrayList;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;


/**
 * 周边商店列表
 * @author summer
 */
public class StoreViewModel extends BaseLbsViewModel{
    private double longitude,latitude;
    private Fragment fragment;
    public StoreViewModel(Fragment context) {
        super(context.getContext());
        fragment = context;
    }

    public  StoreViewModel(Context context) {
        super(context);
    }

    private PublicApi mPublicAPI;

    @Override
    public void onCreate() {
        super.onCreate();
        mPublicAPI = new PublicApi();
    }


    /**
     * 封装一个界面发生改变的观察者
     */
    public UIChangeObservable uc = new UIChangeObservable();

    @Override
    public void onLocation(BDLocation bdLocation) {
            //拿到定位信息后关闭定位，只定位一次；
           locationUtil.destroy();

           longitude = bdLocation.getLongitude();
           latitude = bdLocation.getLatitude();
           showDialog("加载中...");
           requestData();

    }

    public class UIChangeObservable {
        /**
         * 下拉刷新完成的观察者
         */
        public ObservableBoolean isFinishRefreshing = new ObservableBoolean(false);

    }

    /**
     * 给RecyclerView添加ObservableList
     */
    public ObservableList<StoreItemViewModel> observableList = new ObservableArrayList<>();
    /**
     * 给RecyclerView添加ItemBinding
     */
    public ItemBinding<StoreItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_store);

    /**
     * 给RecyclerView添加Adpter，请使用自定义的Adapter继承BindingRecyclerViewAdapter，重写onBindBinding方法
     */
    public final BindingRecyclerViewAdapter<StoreItemViewModel> adapter = new BindingRecyclerViewAdapter<>();

    /**
     * 下拉刷新
     */
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //重新请求
            requestData();
        }
    });


    /**
     * 请求数据
     */
    private void requestData() {
        mPublicAPI.getNearStore(longitude,latitude,new BaseSimpleResultCallback<BaseResponseEntity<ArrayList<StoreEntity>>,ArrayList<StoreEntity>>() {


            @Override
            public void onSucceed(ArrayList<StoreEntity> entity) {
                //关闭对话框
                dismissDialog();
                //刷新数据
                observableList.clear();
                uc.isFinishRefreshing.set(!uc.isFinishRefreshing.get());
                //请求成功
                //将实体赋给全局变量，双向绑定动态添加Item
                for (StoreEntity listBean : entity) {
                    observableList.add(new StoreItemViewModel(context, listBean));
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
}
