package com.lq.cxy.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.activity.AddressEditActivity;
import com.lq.cxy.shop.databinding.FragmentHomeBinding;
import com.lq.cxy.shop.model.PublicApi;
import com.lq.cxy.shop.model.entity.AddressEntity;
import com.lq.cxy.shop.model.entity.BannerEntity;
import com.lq.cxy.shop.model.entity.ProductEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.http.base.BaseSimpleResultCallback;
import com.lq.cxy.shop.model.viewmodel.GoodViewModel;
import com.lq.cxy.shop.model.viewmodel.StoreItemViewModel;
import com.lq.cxy.shop.widget.QuickWaterView;

import org.wavefar.lib.base.BaseFragment;
import org.wavefar.lib.ui.widget.BannerView;
import org.wavefar.lib.ui.widget.pageindicator.CirclePageIndicator;
import org.wavefar.lib.utils.IntentUtils;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * 首页
 * @author summer
 */
public class HomeFragment extends BaseFragment<FragmentHomeBinding, GoodViewModel> {

    private ArrayList<View> mViews = new ArrayList<>();
    private BannerView mBannerView;
    private PublicApi mPublicApi;
    private QuickWaterView quickWaterView;

    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.fragment_home;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public GoodViewModel initViewModel() {
        return new GoodViewModel(getContext());
    }

    @Override
    public void initData() {
        View view = binding.getRoot();
        initView(view);
        mPublicApi = new PublicApi();
    }

    private void initView(View view) {
        // banner广告
        mBannerView = view.findViewById(R.id.bannerView);
        mBannerView.getBannerTitleView().setVisibility(View.GONE);
        mBannerView.getBannerBottomView().setBackgroundColor(0x00ffffff);
        CirclePageIndicator circlePageIndicator = mBannerView
                .getmCircleFlowIndicator();
        circlePageIndicator.setStrokeColor(0xFFD81B33);
        circlePageIndicator.setFillColor(0xFFD81B33);
        mBannerView.initBannerViewData(mViews);

        //快捷选水
         quickWaterView = view.findViewById(R.id.quick_water);
    }

    /**
     * 初始化Banner 数据
     *
     * @param adEntities
     */
    private void bindBannerData(ArrayList<BannerEntity> adEntities) {
        mViews.clear();
        if (null != adEntities && adEntities.size() > 0) {
            for (BannerEntity adEntity : adEntities) {

                FrameLayout frameLayout = new FrameLayout(getActivity());
                ImageView bannerView = new ImageView(getActivity());
                bannerView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                frameLayout.addView(bannerView);
                Glide.with(getActivity()).load(adEntity.getImgPath()).
                        thumbnail(0.1f)
                        .into(bannerView);
                frameLayout.setTag(adEntity);
                frameLayout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        browserAd((BannerEntity) v.getTag());
                    }
                });
                mViews.add(frameLayout);
            }
        } else {
            // 设置空广告
            View emptyView = new View(getActivity());
            emptyView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gray));
            mViews.add(emptyView);
        }
        mBannerView.getBannerAdaptor().notifyDataSetChanged();
    }


    /**
     * 点击广告处理
     *
     * @param tag
     */
    protected void browserAd(BannerEntity tag) {
        IntentUtils.startWeb(tag.getUrl(), tag.getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBannerView != null) {
            mBannerView.stopAutoScroll();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initRequest();

        if (mBannerView != null) {
            mBannerView.startAutoScroll();
        }
    }


    /**
     * 初始化网络请求
     */
    private void initRequest() {
        mPublicApi.getBanner(new BaseSimpleResultCallback<BaseResponseEntity<ArrayList<BannerEntity>>, ArrayList<BannerEntity>>() {
            @Override
            public void onSucceed(ArrayList<BannerEntity> entity) {
                bindBannerData(entity);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                //收货地址
                case 1:
                    quickWaterView.setAddressEntity((AddressEntity) data.getParcelableExtra(AddressEditActivity.KEY_ADDREES_TO_EDIT));
                break;
                //选择商品
                case 2:
                    quickWaterView.setProductEntity((ProductEntity) data.getParcelableExtra(StoreItemViewModel.PRODUCT_KEY));
                    break;
                    default:break;
            }
        }
    }
}
