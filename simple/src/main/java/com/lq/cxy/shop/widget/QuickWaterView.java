package com.lq.cxy.shop.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lq.cxy.shop.R;
import com.lq.cxy.shop.activity.AddressManageActivity;
import com.lq.cxy.shop.activity.LoginActivity;
import com.lq.cxy.shop.fragment.OrderConfirmFragment;
import com.lq.cxy.shop.fragment.StoreFragment;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.model.entity.AddressEntity;
import com.lq.cxy.shop.model.entity.BrandEntity;
import com.lq.cxy.shop.model.entity.OrderEntity;
import com.lq.cxy.shop.model.entity.ProductEntity;

import org.wavefar.lib.ui.widget.CounterView;
import org.wavefar.lib.utils.AlertUtils;
import org.wavefar.lib.utils.IntentUtils;
import org.wavefar.lib.utils.JsonUtils;
import org.wavefar.lib.utils.SharePreferenceUtils;
import org.wavefar.lib.utils.TimeUtil;
import org.wavefar.lib.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import static com.lq.cxy.shop.activity.AddressManageActivity.KEY_IS_SELECT_MODE;
import static org.wavefar.lib.utils.IntentUtils.startContainerActivity;


/**
 * 快捷选水视图
 * @author summer
 */
public class QuickWaterView extends LinearLayout implements View.OnClickListener,CounterView.IChangeCountCallback {

	private Context context;
	private TextView brandTv,dateTv,timeTv, addressTv;

	private ProductEntity mProductEntity;
	/**
	 * 收货地址
	 */
	private AddressEntity mAddressEntity;
	/**
	 * 购买数量
	 */
	private int mNum = 1;

	public QuickWaterView(Context context) {
		this(context, null, 0);
	}

	public QuickWaterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public QuickWaterView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	private void initView(Context context) {
		this.context = context;
		View view = View.inflate(context, R.layout.home_water, null);
		addView(view);

		brandTv = findViewById(R.id.brand_tv);
		dateTv = findViewById(R.id.date_tv);
		timeTv = findViewById(R.id.time_tv);
		addressTv = findViewById(R.id.address_tv);

		CounterView counterView = findViewById(R.id.goods_num);
		counterView.setCallback(this);

		findViewById(R.id.change_address_tv).setOnClickListener(this);
		brandTv.setOnClickListener(this);
		dateTv.setOnClickListener(this);
		timeTv.setOnClickListener(this);
		addressTv.setOnClickListener(this);

		view.findViewById(R.id.saveBtn).setOnClickListener(this);


		//服务时间为早8点到下午6点前显示的时间段
		int hours = Integer.valueOf(TimeUtil.getCurrentDateTime("HH"));
		if ( hours < 18) {
			dateTv.setText(TimeUtil.getTime2String(System.currentTimeMillis(),"MM月dd日 E 今天"));
			if (hours > 8) {
				timeTv.setText(String.format("%s-%s", TimeUtil.getCurrentDateTime("HH:mm"), TimeUtil.timestampPlus("HH:mm", 2)));
			} else {
				timeTv.setText("08:00-10:00");
			}
		} else {
			timeTv.setText("08:00-10:00");
			dateTv.setText(TimeUtil.getTime2String(System.currentTimeMillis()+86400000L,"MM月dd日 E 明天"));
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			//点击获取品牌
			case R.id.brand_tv:
				startContainerActivity((Activity) context,StoreFragment.class.getName(),null,2);
				break;
			case R.id.date_tv:
				DateDialog dateDialog = new DateDialog(context);
				dateDialog.show();
				dateDialog.setOnItemChangeListener(new DateDialog.OnItemChangeListener() {
					@Override
					public void onItemChange(String date) {
						dateTv.setText(date);
					}
				});
				break;
			case R.id.time_tv:
                TimeDialog timeDialog = new TimeDialog(context);
                timeDialog.show();
                timeDialog.setOnItemChangeListener(new TimeDialog.OnItemChangeListener() {
                    @Override
                    public void onItemChange(String date) {
                        timeTv.setText(date);
                    }
                });
				break;
			case R.id.change_address_tv:
			case R.id.address_tv:
				if (!UserAPI.checkLogin()) {
					IntentUtils.redirect(LoginActivity.class);
					return;
				}
				Bundle bundle = new Bundle();
				bundle.putBoolean(KEY_IS_SELECT_MODE,true);
				IntentUtils.redirectAndPrameterResult((Activity) context, AddressManageActivity.class,bundle,1);
				break;
			case R.id.saveBtn:
				submit();
				break;
				default:
		}
	}

	/**
	 * 确定提交数据
	 */
	private void submit() {
		if (mProductEntity == null) {
			ToastUtils.showShort("请选择一个周边送水商家");
			return;
		}
		if (mAddressEntity == null) {
			ToastUtils.showShort("收货地址不能为空");
			return;
		}

		//订单数据保存起来
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setAddress(mAddressEntity);
		orderEntity.setDisDate(dateTv.getText().toString());
		orderEntity.setDisTime(timeTv.getText().toString());

        Bundle mBundle = new Bundle();
        List<ProductEntity> list = new ArrayList<>();
        //购买数量
		mProductEntity.setNum(mNum);
        list.add(mProductEntity);
		orderEntity.setGoods(list);
        mBundle.putParcelable(OrderConfirmFragment.ORDER_KEY, orderEntity);
        startContainerActivity(OrderConfirmFragment.class.getName(),mBundle);
	}

	/**
	 * 回调设置地址
	 * @param addressEntity
	 */
	public void setAddressEntity(AddressEntity addressEntity){
		if (addressEntity != null) {
			mAddressEntity = addressEntity;
			addressTv.setText(addressEntity.getAddress());
		}
	}

	/**
	 * 回调设置地址
	 * @param productEntity
	 */
	public void setProductEntity(ProductEntity productEntity){
		if (productEntity != null) {
			mProductEntity = productEntity;
			brandTv.setText(productEntity.getStoreName()+"\n"+productEntity.getGoodsName());
		}
	}

	@Override
	public void change(int count) {
		mNum = count;
	}
}