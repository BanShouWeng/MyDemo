package com.bsw.mydemo.utils;

/**
 * @author leiming
 * @date 2018/11/19.
 */

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by czq on 2016/11/23.
 * ip地址管理的工具类
 */

public class IpAddressUtils {
    private static final String TAG = "IpAddressUtils";

    /**
     * gps获取ip
     *
     * @return
     */
    public static String getLocalIpAddress() {
        Logger.i(TAG, "getLocalIpAddress");
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (! inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
        return null;
    }

    /**
     * wifi获取ip
     *
     * @param context
     * @return
     */
    public static String getIp(Context context) {
        Logger.i(TAG, "getIp");
        try {
            //获取wifi服务
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            //判断wifi是否开启
            if (! wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = intToIp(ipAddress);
            return ip;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
        return null;
    }

    /**
     * 格式化ip地址（192.168.11.1）
     *
     * @param i
     * @return
     */
    private static String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    /**
     * 3G/4g网络IP
     */
    public static String getIpAddress() {
        Logger.i(TAG, "getIpAddress");
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (! inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
        return null;
    }

    /**
     * 获取本机的ip地址（3中方法都包括）
     *
     * @param context
     * @return
     */
    public static String getIpAddress(Context context) {
        String ip = null;
        try {
            switch (NetJudgeUtil.getNetWorkState(context)) {
                case NetJudgeUtil.NETWORK_MOBILE:
                    ip = getIpAddress();
                    break;

                case NetJudgeUtil.NETWORK_WIFI:
                    ip = getIp(context);
                    break;

                default:
                    ip = getLocalIpAddress();
                    break;
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
        Logger.i("IpAdressUtils", "ip==" + ip);
        return ip;
    }

}

