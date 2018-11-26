package com.lq.cxy.shop.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bumptech.glide.Glide;
import com.lq.cxy.shop.Constant;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.databinding.ActivityAddGoodsBinding;
import com.lq.cxy.shop.model.GoodsAPI;
import com.lq.cxy.shop.model.entity.BrandEntity;
import com.lq.cxy.shop.model.entity.ProductEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.viewmodel.MerchandiseFragViewModel;
import com.lq.cxy.shop.utils.UiUtil;
import com.lq.cxy.shop.utils.Util;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;

import org.greenrobot.eventbus.EventBus;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.KeyBoardUtil;
import org.wavefar.lib.utils.ScreenUtils;
import org.wavefar.lib.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MerchantGoodsDetailActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String KEY_MERCHANTGOODS_PRODUCT = "key_str_merchant_goods";

    private static final int REQUEST_CODE_COVER_IMG = 12;
    private static final int REQUEST_CODE_DETAIL_IMG = 24;
    private boolean isUpdateOperation = false;
    ActivityAddGoodsBinding binding;
    ISListConfig coverConfig, detailConfig;
    private ProductEntity requestEntity = new ProductEntity();
    private File coverImg, detailImg;
    private List<BrandEntity> cateList;
    private List<String> cateNameList;

    public static void launch(Context context, ProductEntity product) {
        Intent i = new Intent(context, MerchantGoodsDetailActivity.class);
        i.putExtra(KEY_MERCHANTGOODS_PRODUCT, product);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_goods);
        TextView tv = binding.merchantAddGoodsHead.findViewById(R.id.head_title);
        tv.setText(R.string.merchant_add_goods);
        ImageView headMore = binding.merchantAddGoodsHead.findViewById(R.id.head_more);
        headMore.setImageResource(R.drawable.delete_white);
        headMore.setVisibility(View.GONE);
        headMore.setOnClickListener(this);
        binding.merchantAddGoodsHead.findViewById(R.id.head_back_root).setOnClickListener(this);
        binding.realAddGoodsCoverImgRoot.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        binding.realAddGoodsCoverImgRoot.getViewTreeObserver()
                                .removeOnGlobalLayoutListener(this);
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) binding
                                .realAddGoodsCoverImgRoot.getLayoutParams();
                        lp.width = binding.realAddGoodsCoverImgRoot.getWidth();
                        lp.height = lp.width / 2;
                        binding.realAddGoodsCoverImgRoot.setLayoutParams(lp);
                    }
                });
        binding.realAddGoodsDetailImgRoot.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        binding.realAddGoodsDetailImgRoot.getViewTreeObserver()
                                .removeOnGlobalLayoutListener(this);
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) binding
                                .realAddGoodsDetailImgRoot.getLayoutParams();
                        lp.width = ScreenUtils.getScreenWidth() / 2;
                        lp.height = lp.width * 2;
                        binding.realAddGoodsDetailImgRoot.setLayoutParams(lp);
                    }
                });
        binding.realAddGoodsDetailImgRoot.setOnClickListener(this);
        binding.realAddGoodsCoverImgRoot.setOnClickListener(this);
        binding.realAddGoodsClass.setOnClickListener(this);
        binding.realAddGoodsStatus.setOnClickListener(this);
        binding.adAddGoodsStatus.setOnClickListener(this);
        binding.clickToSave.setOnClickListener(this);
        binding.clickToSubmitPublish.setOnClickListener(this);
        binding.realAddGoodsName.addTextChangedListener(contentChangeListener);
//        binding.realAddGoodsCode.addTextChangedListener(contentChangeListener);
        binding.realAddGoodsPrice.addTextChangedListener(contentChangeListener);
        binding.realAddGoodsRemark.addTextChangedListener(contentChangeListener);
        binding.realAddGoodsStock.addTextChangedListener(contentChangeListener);

        coverConfig = UiUtil.getImageSelectorConfig(getApplicationContext(), 2, 1, 800, 400);
        detailConfig = UiUtil.getImageSelectorConfig(getApplicationContext(), 1, 2, 600, 1200);

        if (getIntent() != null && getIntent().getParcelableExtra(KEY_MERCHANTGOODS_PRODUCT) != null) {
            requestEntity = getIntent().getParcelableExtra(KEY_MERCHANTGOODS_PRODUCT);
            isUpdateOperation = true;
            boolean enbleEdit = requestEntity.getGoodStatus() == 0 || requestEntity.getGoodStatus() == 4;
            if (enbleEdit) {
                tv.setText(R.string.merchant_update_goods);
            } else {
                tv.setText(R.string.merchant_detail_goods);
            }
            headMore.setVisibility(View.VISIBLE);
            binding.adAddGoodsStatus.setVisibility(View.VISIBLE);
            binding.realAddGoodsStatus.setVisibility(View.VISIBLE);
            initViewWidthGoodsInfo();
        } else {
            Util.loadCategory();
            cateList = Util.getCategoryList();
            cateNameList = Util.getCategoryNameList(cateList);
            if (cateNameList != null && cateNameList.size() > 0) {
                binding.realAddGoodsClass.setText(cateNameList.get(0));
            }
        }
        KeyBoardUtil.hideSoftInput(this);
    }

    private void initViewWidthGoodsInfo() {
        binding.realAddGoodsPrice.setText(String.valueOf(requestEntity.getPrice()));
        binding.realAddGoodsStock.setText(String.valueOf(requestEntity.getStock()));
        binding.realAddGoodsName.setText(String.valueOf(requestEntity.getGoodsName()));
//        binding.realAddGoodsCode.setText(String.valueOf(requestEntity.getGoodsCode()));
        binding.realAddGoodsRemark.setText(String.valueOf(requestEntity.getRemark()));
        Glide.with(this).load(requestEntity.getAvatar()).into(binding.realAddGoodsCoverImg);
        Glide.with(this).load(requestEntity.getDetail()).into(binding.realAddGoodsDetailImg);
        binding.realAddGoodsClass.setText(Util.getCategoryNameBaseId(requestEntity.getCategoryId()));
        binding.realAddGoodsStatus.setText(Constant.GOODS_STATUS.get(requestEntity.getGoodStatus()));
        boolean enableView = requestEntity.getGoodStatus() == 0 || requestEntity.getGoodStatus() == 4;
        binding.realAddGoodsPrice.setEnabled(enableView);
        binding.realAddGoodsStock.setEnabled(enableView);
        binding.realAddGoodsName.setEnabled(enableView);
        binding.realAddGoodsRemark.setEnabled(enableView);
        binding.realAddGoodsCoverImg.setEnabled(enableView);
        binding.realAddGoodsDetailImg.setEnabled(enableView);
        binding.realAddGoodsDetailImgRoot.setEnabled(enableView);
        binding.realAddGoodsCoverImgRoot.setEnabled(enableView);
        binding.realAddGoodsClass.setEnabled(enableView);
        binding.realAddGoodsStatus.setEnabled(enableView);
        binding.clickToSave.setVisibility(enableView ? View.VISIBLE : View.GONE);
        binding.clickToSubmitPublish.setVisibility(enableView ? View.VISIBLE : View.GONE);
        binding.adAddGoodsStatus.setEnabled(enableView);
    }

    private void setToCheckState() {
        binding.realAddGoodsStatus.setText((Constant.GOODS_STATUS.get(0)));
        binding.realAddGoodsStatus.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_more:
                new GoodsAPI().deleteGoods(requestEntity, new BaseResultCallback<BaseResponseEntity>() {
                    @Override
                    public void onResponse(BaseResponseEntity response) {
                        if (response.isSuccess()) {
                            EventBus.getDefault().post(new MerchandiseFragViewModel.GoodsInfoUpdateEvent() {
                            });
                            ToastUtils.showShort("商品删除成功");
                            finish();
                        } else {
                            ToastUtils.showShort(response.getMessage());
                        }
                    }
                });
                break;
            case R.id.clickToSave:
                checkAndSave(false);
                break;
            case R.id.clickToSubmitPublish:
                checkAndSave(true);
                break;
            case R.id.head_back_root:
                finish();
                break;
            case R.id.realAddGoodsDetailImgRoot:
                ISNav.getInstance().toListActivity(this, detailConfig, REQUEST_CODE_DETAIL_IMG);
                break;
            case R.id.realAddGoodsCoverImgRoot:
                ISNav.getInstance().toListActivity(this, coverConfig, REQUEST_CODE_COVER_IMG);
                break;
            case R.id.realAddGoodsClass:
                cateList = Util.getCategoryList();
                cateNameList = Util.getCategoryNameList(cateList);
                UiUtil.showOptionDialog(this, cateNameList, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        requestEntity.setCategoryId(Util.getIdBaseName(cateList, cateNameList.get(options1)));
                        setToCheckState();
                        binding.realAddGoodsClass.setText(cateNameList.get(options1));
                    }
                });
                break;
            case R.id.realAddGoodsStatus:
            case R.id.adAddGoodsStatus:
                UiUtil.showOptionDialog(this, Constant.GOODS_STATUS, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        requestEntity.setGoodStatus(options1);
                        binding.realAddGoodsStatus.setText(Constant.GOODS_STATUS.get(options1));
                    }
                });
                break;
        }
    }

    private void checkAndSave(final boolean publish) {
        String str = binding.realAddGoodsName.getText().toString().trim();
        if (TextUtils.isEmpty(str)) {
            ToastUtils.showShort("请输入商品名称");
            return;
        }
        requestEntity.setGoodsName(str);
//        str = binding.realAddGoodsCode.getText().toString().trim();
//        if (TextUtils.isEmpty(str)) {
//            ToastUtils.showShort("请输入商品编码");
//            return;
//        }
//        requestEntity.setGoodsCode(str);
        str = binding.realAddGoodsRemark.getText().toString().trim();
        if (TextUtils.isEmpty(str)) {
            ToastUtils.showShort("请简单介绍一下商品");
            return;
        }
        requestEntity.setRemark(str);
        str = binding.realAddGoodsStock.getText().toString().trim();
        if (TextUtils.isEmpty(str)) {
            ToastUtils.showShort("请输入商品库存量");
            return;
        }
        requestEntity.setStock(Integer.valueOf(str));
        str = binding.realAddGoodsPrice.getText().toString().trim();
        if (TextUtils.isEmpty(str)) {
            ToastUtils.showShort("请输入商品价格");
            return;
        }
        requestEntity.setPrice(Double.valueOf(str));
        if (!isUpdateOperation) {
            if (coverImg == null) {
                ToastUtils.showShort("请选择商品简介图片");
                return;
            }
            if (detailImg == null) {
                ToastUtils.showShort("请选择商品详情图片");
                return;
            }
        }

        new GoodsAPI().addGoods(requestEntity, coverImg, detailImg, isUpdateOperation,
                new BaseResultCallback<BaseResponseEntity<ProductEntity>>() {
                    @Override
                    public void onResponse(BaseResponseEntity<ProductEntity> response) {
                        if (response.isSuccess()) {
                            if (publish) {
                                publishGoods(response.getContent());
                            } else {
                                EventBus.getDefault().post(new MerchandiseFragViewModel.GoodsInfoUpdateEvent() {
                                });
                                ToastUtils.showShort("商品信息保存成功");
                                finish();
                            }
                        } else {
                            ToastUtils.showShort(response.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e, String body) {
                        super.onError(e, body);
                        ToastUtils.showShort(body);
                    }
                });
    }

    private void publishGoods(ProductEntity entity) {
        new GoodsAPI().publishGoods(entity, new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                if (response.isSuccess()) {

                    EventBus.getDefault().post(new MerchandiseFragViewModel.GoodsInfoUpdateEvent() {
                    });
                    ToastUtils.showShort("商品信息发布成功");
                    finish();
                } else {
                    ToastUtils.showShort(response.getMessage());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_DETAIL_IMG:
                if (resultCode == RESULT_OK && data != null) {
                    setToCheckState();
                    detailImg = setImageView(binding.realAddGoodsDetailImg, data);
                }
                break;
            case REQUEST_CODE_COVER_IMG:
                if (resultCode == RESULT_OK && data != null) {
                    setToCheckState();
                    coverImg = setImageView(binding.realAddGoodsCoverImg, data);
                }
                break;
        }
    }

    private File setImageView(ImageView imgView, Intent data) {
        List<String> pathList = data.getStringArrayListExtra("result");
        File file = null;
        for (String path: pathList) {
            file = new File(path);
            imgView.setImageURI(Uri.fromFile(file));
        }
        return file;
    }

    private TextWatcher contentChangeListener = new TextWatcher() {
        private CharSequence cacheStr;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            cacheStr = charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (cacheStr != null && editable.toString() != null && TextUtils.equals(cacheStr, editable)) {
                setToCheckState();
            }
        }
    };
}
