package com.lq.cxy.shop.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.utils.LocationUtil;

import org.wavefar.lib.utils.NetworkUtils;
import org.wavefar.lib.utils.ToastUtils;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_KEY_LATITUCE = "loc_result_lat";
    public static final String EXTRA_KEY_LONGITUDE = "loc_result_lon";
    public static final String EXTRA_KEY_ADDRESS = "loc_result_address";
    public final static int GET_LOCATION_CODE = 512;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    MapView mMapView;
    BaiduMap mBaiduMap;

    // UI相关
    boolean isFirstLoc = true;// 是否首次定位
    private EditText shop_address_edit;
    private TextView confirm_btn_text;

    private double lat = 0.1;
    private double lng = 0.1;
    private String address = "";

    private ImageView relocation_img;
//    private Animation operatingAnim;


    LocationUtil locationUtil;

    private static final int REQUEST_CONTACTS = 1000;
    private static final String[] PERMISSIONS_CONTACT = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};

    private void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
        } else {
            locationUtil.start();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        locationUtil = new LocationUtil(getApplicationContext());
        locationUtil.registerLocationListener(new LocationListener());
        setupViews();
        showContacts();
        shop_address_edit = findViewById(R.id.shop_address_edit);
        confirm_btn_text = findViewById(R.id.confirm_btn_text);
        confirm_btn_text.setOnClickListener(new View.OnClickListener() {  //确定
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_KEY_LATITUCE, String.valueOf(lat));
                bundle.putString(EXTRA_KEY_LONGITUDE, String.valueOf(lng));
                bundle.putString(EXTRA_KEY_ADDRESS, shop_address_edit.getText().toString());
                data.putExtras(bundle);
                //请求代码可以自己设置，这里设置成20
                setResult(RESULT_OK, data);
                //关闭掉这个Activity
                finish();
            }
        });
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

        // 注册位置监听器

        //调用BaiduMap对象的setOnMarkerDragListener方法设置marker拖拽的监听
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mBaiduMap.clear();
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.location_icon);
                LatLng point = latLng;
                OverlayOptions option1 = new MarkerOptions()
                        .position(point)
                        .icon(bitmap);
                mBaiduMap.addOverlay(option1);
                lat = latLng.latitude;
                lng = latLng.longitude;
                GeoCoder coder = GeoCoder.newInstance();
                ReverseGeoCodeOption reverseCode = new ReverseGeoCodeOption();
                ReverseGeoCodeOption result = reverseCode.location(point);
                coder.reverseGeoCode(result);
                Log.i("address", "=lat====" + lat + "==lng==" + lng);
                coder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                        shop_address_edit.setText(result.getAddress());
                        Log.i("address", "==onGetReverseGeoCodeResult===" + result.getAddress());
                        address = result.getAddress();
                    }

                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult result) {
                        Log.i("address", "==onGetGeoCodeResult===" + result.getAddress());
                    }
                });
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                Log.i("address", "==onMapPoiClick===");
                return false;
            }
        });
    }


    private class LocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null || mBaiduMap == null) {
                return;
            }
            if (isFirstLoc) {
                mBaiduMap.clear();
                isFirstLoc = false;
                LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
                //定义Maker坐标点
                LatLng point = ll;
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.location_icon);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option1 = new MarkerOptions()
                        .position(point)
                        .icon(bitmap);
                //在地图上添加Marker，并显示
                mBaiduMap.addOverlay(option1);
                OverlayOptions options = new MarkerOptions()
                        .position(point)  //设置marker的位置
                        .icon(bitmap)  //设置marker图标
                        .zIndex(9)  //设置marker所在层级
                        .draggable(true);  //设置手势拖拽
                //将marker添加到地图上
                lat = bdLocation.getLatitude();
                lng = bdLocation.getLongitude();
                mBaiduMap.addOverlay(options);
                GeoCoder coder = GeoCoder.newInstance();
                ReverseGeoCodeOption reverseCode = new ReverseGeoCodeOption();
                ReverseGeoCodeOption result = reverseCode.location(point);
                coder.reverseGeoCode(result);
                relocation_img.clearAnimation();
                if (NetworkUtils.isConnected()) {
                    ToastUtils.showShort("定位成功");
                    shop_address_edit.setText(bdLocation.getAddress().address);
                } else {
                    ToastUtils.showShort("网络未连接");
                }
                coder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                        shop_address_edit.setText(result.getAddress());
                        address = result.getAddress();
                    }

                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult result) {

                    }
                });
            }
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    public void setupViews() {
        relocation_img = findViewById(R.id.relocation_img);
        relocation_img.setOnClickListener(this);

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(16).build()));
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relocation_img:  //重新定位
                if (!NetworkUtils.isConnected()) {
                    ToastUtils.showShort("网络未连接");
                    return;
                }
                /** 设置旋转动画 */
                isFirstLoc = true;
                locationUtil.start();
                break;
        }
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        locationUtil.destroy();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CONTACTS) {
            boolean isGrantedAll = true;
            for (String p: permissions) {
                if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                    isGrantedAll = false;
                    break;
                }
            }
            if (isGrantedAll) {
                locationUtil.start();
            } else {
                ToastUtils.showShort("授权不通过");
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

