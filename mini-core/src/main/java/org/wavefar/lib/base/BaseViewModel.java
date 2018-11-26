package org.wavefar.lib.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.wavefar.lib.utils.AlertUtils;
import org.wavefar.lib.utils.IntentUtils;

/**
 * ViewModel基类
 * @author summer
 */
public class BaseViewModel implements IBaseViewModel {
    protected Context context;
    protected Fragment fragment;


    public BaseViewModel(Context context) {
        this.context = context;
    }

    public BaseViewModel(Fragment fragment) {
        this(fragment.getContext());
        this.fragment = fragment;
    }


    public void showDialog() {
        showDialog("请稍后...");
    }

    public void showDialog(String title) {
        AlertUtils.showProgressDialog(context,"",title);
    }

    public void dismissDialog() {
        AlertUtils.closeProgressDialog();
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(clz,null,false);
    }


    /**
     * 跳转页面带参数
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz,Bundle bundle) {
        startActivity(clz,bundle,false);
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     * @param isFinish 是否消耗当前页面
     */
    public void startActivity(Class<?> clz, Bundle bundle,boolean isFinish) {
        if (bundle != null) {
            IntentUtils.redirectAndPrameter(clz, bundle);
        } else {
            IntentUtils.redirect(clz);
        }
        if (isFinish){
            ((Activity)context).finish();
        }
    }


    /**
     * 跳转Fragment容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     */
    public void startContainerActivity(String canonicalName, Bundle bundle) {
        Bundle bd = new Bundle();
        if (bundle != null){
            bd.putBundle(ContainerActivity.BUNDLE, bundle);
        }
        bd.putString(ContainerActivity.FRAGMENT,canonicalName);
        IntentUtils.redirectAndPrameter(ContainerActivity.class,bd);
    }

    /**
     * 跳转Fragment公共容器页面，带回调监听
     *
     * @param activity 当前Activity
     * @param canonicalName 规范名 : Fragment.class.getName()
     * @param bundle        跳转所携带的信息
     * @param requestCode 请求code
     */
    public static  void startContainerActivity(Activity activity,String canonicalName, Bundle bundle,int requestCode) {
        Bundle bd = new Bundle();
        if (bundle != null){
            bd.putBundle(ContainerActivity.BUNDLE, bundle);
        }
        bd.putString(ContainerActivity.FRAGMENT,canonicalName);
        IntentUtils.redirectAndPrameterResult(activity,ContainerActivity.class,bd,requestCode);
    }

    /**
     * 跳转Fragment容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     */
    public void startContainerActivity(String canonicalName) {
        startContainerActivity(canonicalName,null);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void registerRxBus() {
    }

    @Override
    public void removeRxBus() {
    }
}
