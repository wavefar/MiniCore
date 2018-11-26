package com.lq.cxy.shop.utils;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.poi.BaiduMapPoiSearch;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapsdkplatform.comapi.location.CoordinateType;

import org.wavefar.lib.utils.LogUtil;
import org.wavefar.lib.utils.StringUtil;

/**
 * 定位相关工具类
 * @author summer
 */
public class LocationUtil {

	private LocationClient  mLocationClient;
	private BDAbstractLocationListener mlistener;
	private Context context;
    // 地球半径
	private final static double EARTH_RADIUS = 6378.137;
	public LocationUtil(Context context) {
		 this.context = context;
		 mLocationClient = new LocationClient(context.getApplicationContext());
	     LocationClientOption option = initLocationOption();
	     mLocationClient.setLocOption(option);
	     LogUtil.d(LocationUtil.class.getSimpleName(), "当前定位库版本" + mLocationClient.getVersion());
	}

	private LocationClientOption initLocationOption() {
		LocationClientOption option = new LocationClientOption();
        // 设置定位模式
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 返回的定位结果是百度经纬度,默认值gcj02
		option.setCoorType(CoordinateType.BD09LL);
        // 设置发起定位请求的间隔时间为5000ms
		option.setScanSpan(5000);
        // 返回的定位结果包含地址信息
		option.setIsNeedAddress(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIsNeedLocationPoiList(true);
        // 返回的定位结果包含手机机头的方向
		option.setNeedDeviceDirect(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        option.setOpenGps(true);
		return option;
	}
	
	/**
	 * 注册定位回调
	 * @param listener
	 */
	public void registerLocationListener(BDAbstractLocationListener listener) {
		mlistener = listener;
		if (mlistener != null) {
            mLocationClient.registerLocationListener(mlistener);
        }
	}
	
    /**
     * 启动定位
     */
    public void start() {
        if (mLocationClient.isStarted()) {
            return;
        }
        mLocationClient.start();
    }

    /**
     * 重新定位
     */
    public void reStart() {
        if (mLocationClient.isStarted()) {
            return;
        }
        mLocationClient.restart();
    }

    /**
     * 停止定位
     */
    public void stop() {
        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }
	
    /**
     * 销毁定位
     */
    public void destroy() {
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mlistener);
            mLocationClient.stop();
            mLocationClient = null;
        }
    }
    
    /**
     * 启动百度地图导航(Native),如果本地没有安装百度地图，调用WEBAPP
     * @param pt1 起点
     * @param pt2 终点
     */
    public void startNavi(LatLng pt1, LatLng pt2) {
        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2);
        try {
            BaiduMapNavigation.openBaiduMapNavi(para, context);
        } catch (BaiduMapAppNotSupportNaviException e) {
            BaiduMapNavigation.openWebBaiduMapNavi(para, context);
            e.printStackTrace();
        }
    }

    /**
     * 启动百度地图导航(Web)
     */
    public void startWebNavi(LatLng pt1,LatLng pt2) {
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2);
        BaiduMapNavigation.openWebBaiduMapNavi(para, context);
    }
    
    /**
     * 用完导航后需要调用该函数销毁
     */
    public void stopNavi() {
	   try {
           BaiduMapNavigation.finish(context);
           BaiduMapRoutePlan.finish(context);
           BaiduMapPoiSearch.finish(context);
       } catch (Exception e) {
           e.printStackTrace();
       }
    }
    
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 计算距离 返回单位km
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2,
                                     double lng2) {
        LogUtil.d("LocationUtil", "lat1:" + lat1 + "lng1:" + lng1 + "lat2:" + lat2 + "lng2:" + lng2);
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        // 如果有一方等于零，直接返回0
        if (radLat1 == 0 || radLat2 == 0) {
            return 0;
        }
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 距离格式化
     *
     * @param distance 以千米为单位
     * @return
     */
    public static String distanceKMFormat(double distance) {
        return distance > 1 ? (distance + "KM") : (distance * 1000 + "M");
    }


    /**
     * 距离只保留两位小数
     * @param distance 以米为单位
     * @return
     */
    public static String distanceFormat(double distance) {
        String str;
        double value = distance;
        if (distance >= 1000) {
            value = value / 1000;
            str = "KM";
        } else {
            str = "M";
        }
        return String.format("%s%s",StringUtil.formatDecimal(value,null),str);
    }
    
}