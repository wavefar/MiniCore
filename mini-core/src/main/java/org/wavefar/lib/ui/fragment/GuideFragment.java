package org.wavefar.lib.ui.fragment;

import android.os.Bundle;

import org.wavefar.lib.base.BaseFragment;
import org.wavefar.lib.utils.FileUtils;

import java.io.IOException;

/**
 * <pre>
 * 新手指引Fragment子类的处理类 
 * 如果需要用到新手指引集成该类
 * 在firstPage里处理遮罩
 * </pre>
 * @author summer
 */
public abstract class GuideFragment extends BaseFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		String firstName = getClass().getSimpleName();
		if (!FileUtils.checkFileIsExists(getActivity(), firstName)) {
			firstPage();
			try {
				FileUtils.writeLocalFile(getActivity(), firstName, "isFrist".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 第一次进入该页面
	 */
	public abstract void firstPage();
	
}
