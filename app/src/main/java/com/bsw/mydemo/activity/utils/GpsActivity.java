package com.bsw.mydemo.activity.utils;

import android.Manifest;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.utils.Const;
import com.bsw.mydemo.utils.PermissionUtils;
import com.cjt2325.cameralibrary.util.LogUtil;

import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class GpsActivity extends BaseActivity {

    private TextView gpsInfo;
    private LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showGPSContacts();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gps;
    }

    @Override
    protected void findViews() {
        gpsInfo = getView(R.id.gps_info);
    }

    /**
     * 检测GPS、位置权限是否开启
     */
    public void showGPSContacts() {
        lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (ok) {//开了定位服务
            if (Build.VERSION.SDK_INT >= 23) { //判断是否为android6.0系统版本，如果是，需要动态添加权限
                PermissionUtils.setRequestPermissions(activity, new PermissionUtils.PermissionGrant() {
                    @Override
                    public Integer[] onPermissionGranted() {
                        return new Integer[] {PermissionUtils.CODE_ACCESS_FINE_LOCATION};
                    }

                    @Override
                    public void onRequestResult(List<String> deniedPermission) {
                        if (Const.judgeListNull(deniedPermission) == 0) {
                            getLocation();//getLocation为定位方法
                        } else {
                            toast(R.string.permission_denied);
                        }
                    }
                });
            } else {
                getLocation();//getLocation为定位方法
            }
        } else {
            toast("系统检测到未开启GPS定位服务,请开启");
//            Intent intent = new Intent();
//            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivityForResult(intent, PRIVATE_CODE);
        }
    }

    /**
     * 获取具体位置的经纬度
     */
    private void getLocation() {
        // 获取位置管理服务
        final LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) this.getSystemService(serviceName);
        // 查找到服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
        final String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
        gpsInfo.setText(provider);
        /**这段代码不需要深究，是locationManager.getLastKnownLocation(provider)自动生成的，不加会出错**/
        PermissionUtils.setRequestPermissions(activity, new PermissionUtils.PermissionGrant() {
            @Override
            public Integer[] onPermissionGranted() {
                return new Integer[] {PermissionUtils.CODE_ACCESS_FINE_LOCATION, PermissionUtils.CODE_ACCESS_COARSE_LOCATION, PermissionUtils.CODE_READ_PHONE_STATE};
            }

            @Override
            public void onRequestResult(List<String> deniedPermission) {
                if (Const.judgeListNull(deniedPermission) == 0) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                        return;
                    }
                    /*Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
                    updateLocation(location);*/
                    //监听位置变化，2秒一次，距离10米以上

                    locationManager.requestLocationUpdates(provider, 1000, 10, locationListener);
                } else {
                    toast(R.string.permission_denied);
                }
            }
        });
    }

    /**
     * 获取到当前位置的经纬度
     *
     * @param location
     */
    private void updateLocation(Location location) {
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LogUtil.e("维度：" + latitude + "\n经度" + longitude);
        } else {
            LogUtil.e("无法获取到位置信息");
        }
    }

    /**
     * Android6.0申请权限的回调方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void formatViews() {

    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    public void gpsRequest(View view) {
//        showGPSContacts();
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LogUtil.e("onStatusChanged   provider：" + provider + "\nextras" + extras.toString());
        }

        @Override
        public void onProviderEnabled(String provider) {
            LogUtil.e("onProviderEnabled   provider：" + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            LogUtil.e("onProviderDisabled   provider：" + provider);
        }

//当位置变化时触发

        @Override
        public void onLocationChanged(Location location) {
//使用新的location更新TextView显示
            updateLocation(location);
        }
    };
}
