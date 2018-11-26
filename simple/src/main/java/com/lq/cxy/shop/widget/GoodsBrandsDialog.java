package com.lq.cxy.shop.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lq.cxy.shop.R;
import com.lq.cxy.shop.model.PublicApi;
import com.lq.cxy.shop.model.entity.BrandEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.http.base.BaseSimpleResultCallback;

import org.wavefar.lib.ui.widget.CounterView;
import org.wavefar.lib.ui.widget.FlowLayout;
import org.wavefar.lib.utils.ScreenUtils;

import java.util.ArrayList;

/**
 * 品牌dialog
 *
 * @author summer
 */
public class GoodsBrandsDialog extends Dialog implements OnClickListener, CounterView.IChangeCountCallback {


    private OnItemChangeListener mChangeListener;


    private PublicApi mPublicAPI;
    private FlowLayout mBrandFl;
    /**
     * 当前选中品牌
     */
    private BrandEntity mCurrentBrand;
    /**
     * 当前选中商品数量
     */
    private int mCount = 1;

    public GoodsBrandsDialog(Context context) {
        super(context, R.style.dialog);
        mPublicAPI = new PublicApi();
        getAllGoodsType();
    }

    private void getAllGoodsType() {
        mPublicAPI.getCategory(new BaseSimpleResultCallback<BaseResponseEntity<ArrayList<BrandEntity>>, ArrayList<BrandEntity>>() {
            @Override
            public void onSucceed(ArrayList<BrandEntity> entity) {
                if (entity != null) {
                    mCurrentBrand = entity.get(0);
                    mCurrentBrand.setChecked(true);
                    initView(entity);
                } else {
                    dismiss();
                }
            }
        });
    }

    private void initView(ArrayList<BrandEntity> entites) {
        View view = getLayoutInflater().inflate(R.layout.dialog_goods_brands_num, null);
        setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_ok).setOnClickListener(this);

        mBrandFl = findViewById(R.id.brand_name_fl);

        for (BrandEntity brandEntity : entites) {

            mBrandFl.addView(buildLabel(brandEntity));
        }


        CounterView counterView = findViewById(R.id.goods_num);
        counterView.setCallback(this);

        Window window = getWindow();
        /**
         * 设置显示动画
         */
        window.setWindowAnimations(R.style.DialogBottomAnim);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = ScreenUtils.getScreenHeight();
        wl.width = LayoutParams.MATCH_PARENT;
        wl.height = LayoutParams.WRAP_CONTENT;
        onWindowAttributesChanged(wl);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                break;
            case R.id.tv_ok:
                mChangeListener.onItemChange(mCurrentBrand, mCount);
                break;
            default:
                break;
        }
        dismiss();
    }


    private TextView buildLabel(BrandEntity brandEntity) {
        TextView textView = new TextView(getContext());
        textView.setTag(brandEntity);
        textView.setEms(5);
        textView.setGravity(Gravity.CENTER);
        textView.setText(brandEntity.getCategoryName());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        textView.setLayoutParams(lp);
        textView.setPadding(20, 8, 20, 8);

        setStyle(textView, brandEntity.isChecked());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentBrand = (BrandEntity) v.getTag();
                setAllSelectStyle(mCurrentBrand);
            }
        });
        return textView;
    }

    /**
     * 设置全部样式
     *
     * @param currentBrand 传入当前选中品牌
     */
    private void setAllSelectStyle(BrandEntity currentBrand) {
        int count = mBrandFl.getChildCount();
        for (int i = 0; i < count; i++) {
            TextView textView = (TextView) mBrandFl.getChildAt(i);
            BrandEntity brandEntity = (BrandEntity) textView.getTag();
            if (currentBrand == brandEntity) {
                brandEntity.setChecked(true);
            } else {
                brandEntity.setChecked(false);
            }
            setStyle(textView, brandEntity.isChecked());
        }
    }

    /**
     * 设置选中样式
     *
     * @param textView
     * @param checked
     */
    private void setStyle(TextView textView, boolean checked) {
        textView.setBackgroundResource(checked ? R.drawable.tag_checked : R.drawable.tag_normal);
        textView.setTextColor(checked ? 0XFFD81B33 : 0Xffaaaaaa);
    }

    /**
     * 商品数量
     *
     * @param count
     */
    @Override
    public void change(int count) {
        mCount = count;
    }


    public interface OnItemChangeListener {
        /**
         * 当前选中回调
         *
         * @param brandEntity 当前选中品牌
         * @param count       数量
         */
        void onItemChange(BrandEntity brandEntity, int count);
    }

    public void setOnItemChangeListener(OnItemChangeListener listener) {
        mChangeListener = listener;
    }

}
