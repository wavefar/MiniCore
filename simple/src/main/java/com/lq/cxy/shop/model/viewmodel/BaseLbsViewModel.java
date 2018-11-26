package com.lq.cxy.shop.model.viewmodel;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.lq.cxy.shop.utils.LocationUtil;

import org.wavefar.lib.base.BaseViewModel;
import org.wavefar.lib.utils.LogUtil;
import org.wavefar.lib.utils.PermissionUtils;
import org.wavefar.lib.utils.constant.PermissionConstants;
import org.wavefar.lib.utils.helper.DialogHelper;

import java.util.List;

/**
 * 带定位功能的ViewModel
 *
 * @author summer
 * @date 2018/9/4 17:06
 */
public abstract class BaseLbsViewModel extends BaseViewModel{

    public BaseLbsViewModel(Context context) {
        super(context);
    }

    protected LocationUtil locationUtil;
    @Override
    public void onCreate() {
        super.onCreate();
        locationUtil = new LocationUtil(context.getApplicationContext());
        locationUtil.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null) {return;}
                //Receive Location
                StringBuilder sb = new StringBuilder(256);
                sb.append("time : ");
                sb.append(bdLocation.getTime());
                sb.append("\nerror code : ");
                sb.append(bdLocation.getLocType());
                sb.append("\nlatitude : ");
                sb.append(bdLocation.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(bdLocation.getLongitude());
                sb.append("\nradius : ");
                sb.append(bdLocation.getRadius());
                sb.append("\nLocationDescribe : ");
                sb.append(bdLocation.getLocationDescribe());
                sb.append("\npoiList:");
                sb.append(bdLocation.getPoiList());
                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                    sb.append("\nspeed : ");
                    sb.append(bdLocation.getSpeed());
                    sb.append("\nsatellite : ");
                    sb.append(bdLocation.getSatelliteNumber());
                    sb.append("\ndirection : ");
                    sb.append("\naddr : ");
                    sb.append(bdLocation.getAddrStr());
                    sb.append(bdLocation.getDirection());
                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                    sb.append("\naddr : ");
                    sb.append(bdLocation.getAddrStr());
                    //运营商信息
                    sb.append("\noperationers : ");
                    sb.append(bdLocation.getOperators());
                }
                LogUtil.i(BaseLbsViewModel.this.getClass().getSimpleName(), sb.toString());
                onLocation(bdLocation);
            }
        });
        locationUtil.start();

        //请求动态权限
        PermissionUtils.permission(PermissionConstants.LOCATION,PermissionConstants.PHONE,PermissionConstants.STORAGE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        DialogHelper.showRationaleDialog(shouldRequest);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        locationUtil.reStart();
                    }
                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            DialogHelper.showOpenAppSettingDialog();
                        }
                    }
                })
                .request();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationUtil.destroy();
    }


    /**
     * 位置回调
     * @param bdLocation 返回不为null定位信息
     */
    public abstract void onLocation(BDLocation bdLocation);
}
