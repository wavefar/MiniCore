package com.lq.cxy.shop.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;

import org.wavefar.lib.BuildConfig;
import org.wavefar.lib.utils.LogUtil;
import org.wavefar.lib.utils.ToastUtils;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 支付封装
 * <pre>
 * 1、调用{@link #pay(String orderInfo)}开始支付操作
 * </pre>
 * @author summer
 */
public class PayUtil {
	
	private static final String TAG = PayUtil.class.getSimpleName();
	
	private Activity context;
	private static final int SDK_PAY_FLAG = 1;
	ExecutorService executorService;
	private PayCallback payCallback;

	public PayUtil(Activity context) {
		this.context = context;
//		//测试环境添加沙箱标示
//		if (BuildConfig.DEBUG) {
//			EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
//		}
	}

	/**
	 * 设置支付回调
	 * @param payCallback
	 */
	public void setPayCallback(PayCallback payCallback) {
		this.payCallback = payCallback;
	}

	/**
	 * 支付订单
	 * @param orderInfo
	 */
	public void pay(final String orderInfo) {
		executorService = Executors.newSingleThreadExecutor();
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				PayTask aliPay = new PayTask(context);
				Map<String, String> result = aliPay.payV2(orderInfo, true);
				LogUtil.i(TAG,result.toString());
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (!executorService.isShutdown()) {
				executorService.shutdown();
			}
			switch (msg.what) {
				case SDK_PAY_FLAG: {
					PayResult payResult = new PayResult((Map<String, String>) msg.obj);
					/**
					 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
					 */
					String resultStatus = payResult.getResultStatus();
					// 判断resultStatus 为9000则代表支付成功
					if (TextUtils.equals(resultStatus, "9000")) {
						// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
						ToastUtils.showShort("支付成功");
						if (payCallback != null) {
							payCallback.onSucceed(payResult);
						}

					} else {
						// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
						ToastUtils.showShort("支付失败");
						if (payCallback != null) {
							payCallback.onFailure(payResult);
						}
					}
					break;
				}
				default:
					break;
			}
		}
	};

	/**
	 * 支付结果解析
	 */
	public class PayResult {
		private String resultStatus;
		private String result;
		private String memo;

		private PayResult(Map<String, String> rawResult) {
			if (rawResult == null) {
				return;
			}

			for (String key : rawResult.keySet()) {
				if (TextUtils.equals(key, "resultStatus")) {
					resultStatus = rawResult.get(key);
				} else if (TextUtils.equals(key, "result")) {
					result = rawResult.get(key);
				} else if (TextUtils.equals(key, "memo")) {
					memo = rawResult.get(key);
				}
			}
		}

		@Override
		public String toString() {
			return "resultStatus={" + resultStatus + "};memo={" + memo
					+ "};result={" + result + "}";
		}

		/**
		 * @return the resultStatus
		 */
		public String getResultStatus() {
			return resultStatus;
		}

		/**
		 * @return the memo
		 */
		public String getMemo() {
			return memo;
		}

		/**
		 * @return the result
		 */
		public String getResult() {
			return result;
		}
	}

	/**
	 * 支付回调
	 */
	public interface PayCallback {
		/**
		 * 支付成功
		 * @param payResult
		 */
		void onSucceed(PayResult payResult);

		/**
		 * 支付失败
		 * @param payResult
		 */
		void onFailure(PayResult payResult);
	}
}

