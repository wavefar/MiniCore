package com.lq.cxy.shop.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lq.cxy.shop.Constant;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.activity.RegisterActivity;
import com.lq.cxy.shop.activity.SetPasswordActivity;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.utils.AppDownloadManager;

import org.wavefar.lib.utils.AppUtils;
import org.wavefar.lib.utils.IntentUtils;
import org.wavefar.lib.utils.ToastUtils;
/**
 * 设置页面
 * @description
 * @author summer
 */
public class SettingFragment extends Fragment implements OnClickListener {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, container, false);
		initView(view);
		return  view;
	}


	private void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        view.findViewById(R.id.update_pwd_ll).setOnClickListener(this);
		view.findViewById(R.id.about_tv).setOnClickListener(this);
		view.findViewById(R.id.relate_update_soft).setOnClickListener(this);

		View btn  = view.findViewById(R.id.logout_btn);
		btn.setVisibility(UserAPI.checkLogin() ? View.VISIBLE : View.GONE);
		btn.setOnClickListener(this);
        TextView title_tv = view.findViewById(R.id.toolbar_title);
        title_tv.setText("设置");

        //显示当前版本
		TextView  versionTv = view.findViewById(R.id.tv_version);
		versionTv.setText(AppUtils.getAppVersionName());

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.logout_btn:
				new UserAPI().logout(getActivity());
				break;
			//修改密码
			case R.id.update_pwd_ll:
				Bundle bundle = new Bundle();
				bundle.putInt(SetPasswordActivity.TYPE_KEY,2);
				IntentUtils.redirectAndPrameter(RegisterActivity.class,bundle);
				break;
			case R.id.about_tv:
				IntentUtils.startWeb(Constant.ABOUT,"关于我们");
				break;
			case R.id.relate_update_soft:
				AppDownloadManager appDownloadManager = new AppDownloadManager(getActivity(),
						true);
				appDownloadManager.update();
				break;
			case R.id.iv_back:
				getActivity().finish();
				break;
			default:
		}
	}
}
