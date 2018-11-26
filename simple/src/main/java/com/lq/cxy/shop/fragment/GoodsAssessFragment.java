package com.lq.cxy.shop.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.databinding.FragGoodsAssessBinding;
import com.lq.cxy.shop.model.GoodsAPI;
import com.lq.cxy.shop.model.entity.EvaluateEntity;
import com.lq.cxy.shop.model.entity.EvaluateListEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.http.base.BaseSimpleResultCallback;

import org.wavefar.lib.binding.command.BindingAction;
import org.wavefar.lib.binding.command.BindingCommand;
import org.wavefar.lib.utils.ToastUtils;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 商品评价页面
 * @author summer
 */
public class GoodsAssessFragment extends GoodsDetailBaseFrag{

    private int mPageNum = 1;
    private boolean mIsRefresh = true;
    private GoodsAPI mGoodsAPI;
    private FragGoodsAssessBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_goods_assess, container, false);
        binding.setVariable(BR.viewModel,this);
        binding.twinklingRefreshLayout.setHeaderView(new ProgressLayout(getContext()));
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoodsAPI = new GoodsAPI();
    }

    /**
     * 给RecyclerView添加ObservableList
     */
    public ObservableList<EvaluateEntity> observableList = new ObservableArrayList<>();
    /**
     * 给RecyclerView添加ItemBinding
     */
    public ItemBinding<EvaluateEntity> itemBinding = ItemBinding.of(BR.evaluateEntity, R.layout.item_evaluate);

    /**
     * 给RecyclerView添加Adpter，请使用自定义的Adapter继承BindingRecyclerViewAdapter，重写onBindBinding方法
     */
    public final BindingRecyclerViewAdapter<EvaluateEntity> adapter = new BindingRecyclerViewAdapter<>();


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
        mGoodsAPI.getEvaluate(goodsDetail.getId(), mPageNum, new BaseSimpleResultCallback<BaseResponseEntity<EvaluateListEntity>,EvaluateListEntity>() {

            @Override
            public void onSucceed(EvaluateListEntity entity) {
                //刷新数据
                if (mIsRefresh) {
                    observableList.clear();
                    binding.twinklingRefreshLayout.finishRefreshing();
                } else {
                    binding.twinklingRefreshLayout.onFinishLoadMore();
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

    private void refreshData() {
        mIsRefresh = true;
        mPageNum = 1;
        requestData();
    }
}
