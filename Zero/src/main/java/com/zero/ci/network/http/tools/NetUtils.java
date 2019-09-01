package com.zero.ci.network.http.tools;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.zero.ci.widget.logger.Logger;
import com.zero.ci.network.http.NoHttp;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

import static com.zero.ci.network.http.tools.NetUtils.NetType.Mobile;
import static com.zero.ci.network.http.tools.NetUtils.NetType.Mobile2G;
import static com.zero.ci.network.http.tools.NetUtils.NetType.Mobile3G;
import static com.zero.ci.network.http.tools.NetUtils.NetType.Mobile4G;
import static com.zero.ci.network.http.tools.NetUtils.NetType.Wifi;
import static com.zero.ci.network.http.tools.NetUtils.NetType.Wired;

/**
 * Check the network utility class.
 */
public class NetUtils {

    public enum NetType {
        Wifi,
        Wired,
        Mobile,
        Mobile2G,
        Mobile3G,
        Mobile4G
    }

    private static ConnectivityManager sConnectivityManager;

    private static ConnectivityManager getConnectivityManager() {
        if (sConnectivityManager == null) {
            synchronized (NetUtils.class) {
                if (sConnectivityManager == null)
                    sConnectivityManager = (ConnectivityManager) NoHttp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            }
        }
        return sConnectivityManager;
    }

    /**
     * Open network settings page.
     */
    public static void openSetting() {
        Intent settingIntent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        NoHttp.getContext().startActivity(settingIntent);
    }

    /**
     * Check the network is enable.
     *
     * @return Available returns true, unavailable returns false.
     */
    public static boolean isNetworkAvailable() {
        return isWifiConnected() ||
                isWiredConnected() ||
                isMobileConnected();
    }

    /**
     * To determine whether a WiFi network is available.
     *
     * @return Open myreturn true, close returns false.
     */
    public static boolean isWifiConnected() {
        return isNetworkAvailable(Wifi);
    }

    /**
     * To determine whether a wired network is available.
     */
    public static boolean isWiredConnected() {
        return isNetworkAvailable(Wired);
    }

    /**
     * Mobile Internet connection.
     *
     * @return Open myreturn true, close returns false.
     */
    public static boolean isMobileConnected() {
        return isNetworkAvailable(Mobile);
    }

    /**
     * 2G Mobile Internet connection.
     *
     * @return Open myreturn true, close returns false.
     */
    public static boolean isMobile2GConnected() {
        return isNetworkAvailable(Mobile2G);
    }

    /**
     * 3G Mobile Internet connection.
     *
     * @return Open myreturn true, close returns false.
     */
    public static boolean isMobile3GConnected() {
        return isNetworkAvailable(Mobile3G);
    }

    /**
     * 4G Mobile Internet connection.
     *
     * @return Open myreturn true, close returns false.
     */
    public static boolean isMobile4GConnected() {
        return isNetworkAvailable(Mobile4G);
    }

    /**
     * According to the different type of network to determine whether the network connection.
     *
     * @param netType from {@link NetType}.
     * @return Connection state myreturn true, otherwise it returns false.
     */
    public static boolean isNetworkAvailable(NetType netType) {
        getConnectivityManager();
        return isConnected(netType, sConnectivityManager.getActiveNetworkInfo());
    }

    private static boolean isConnected(NetType netType, NetworkInfo networkInfo) {
        if (networkInfo == null) return false;

        switch (netType) {
            case Wifi: {
                if (!isConnected(networkInfo)) return false;
                return networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            }
            case Wired: {
                if (!isConnected(networkInfo)) return false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
                    return networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET;
                return false;
            }
            case Mobile: {
                if (!isConnected(networkInfo)) return false;
                return networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
            case Mobile2G: {
                if (!isConnected(Mobile, networkInfo)) return false;
                return isMobileSubType(Mobile2G, networkInfo);
            }
            case Mobile3G: {
                if (!isConnected(Mobile, networkInfo)) return false;
                return isMobileSubType(Mobile3G, networkInfo);
            }
            case Mobile4G: {
                if (!isConnected(Mobile, networkInfo)) return false;
                return isMobileSubType(Mobile4G, networkInfo);
            }
        }
        return false;
    }

    /**
     * Whether network connection.
     *
     * @param networkInfo from {@link NetworkInfo}.
     * @return Connection state myreturn true, otherwise it returns false.
     */
    private static boolean isConnected(NetworkInfo networkInfo) {
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    private static boolean isMobileSubType(NetType netType, NetworkInfo networkInfo) {
        switch (networkInfo.getType()) {
            case TelephonyManager.NETWORK_TYPE_GSM:
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN: {
                return netType == Mobile2G;
            }
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP: {
                return netType == Mobile3G;
            }
            case TelephonyManager.NETWORK_TYPE_IWLAN:
            case TelephonyManager.NETWORK_TYPE_LTE: {
                return netType == Mobile4G;
            }
            default: {
                String subtypeName = networkInfo.getSubtypeName();
                if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                        || subtypeName.equalsIgnoreCase("WCDMA")
                        || subtypeName.equalsIgnoreCase("CDMA2000")) {
                    return netType == Mobile3G;
                }
                break;
            }
        }
        return false;
    }

    /**
     * Check the GPRS whether available.
     *
     * @return Open myreturn true, close returns false.
     */
    public static boolean isGPRSOpen() {
        getConnectivityManager();

        Class<?> cmClass = sConnectivityManager.getClass();
        try {
            Method getMobileDataEnabledMethod = cmClass.getMethod("getMobileDataEnabled");
            getMobileDataEnabledMethod.setAccessible(true);
            return (Boolean) getMobileDataEnabledMethod.invoke(sConnectivityManager);
        } catch (Throwable ignored) {
        }
        return false;
    }

    /**
     * Open or close the GPRS.
     *
     * @param isEnable Open to true, close to false.
     */
    public static void setGPRSEnable(boolean isEnable) {
        getConnectivityManager();
        Class<?> cmClass = sConnectivityManager.getClass();
        try {
            Method setMobileDataEnabledMethod = cmClass.getMethod("setMobileDataEnabled", boolean.class);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(sConnectivityManager, isEnable);
        } catch (Throwable ignored) {
        }
    }

    /**
     * Tet local ip address.
     *
     * @return Such as: {@code 192.168.1.1}.
     */
    public static String getLocalIPAddress() {
        Enumeration<NetworkInterface> enumeration = null;
        try {
            enumeration = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            Logger.w(e.getMessage());
        }
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                NetworkInterface nif = enumeration.nextElement();
                Enumeration<InetAddress> inetAddresses = nif.getInetAddresses();
                if (inetAddresses != null)
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress ip = inetAddresses.nextElement();
                        if (!ip.isLoopbackAddress() && isIPv4Address(ip.getHostAddress())) {
                            return ip.getHostAddress();
                        }
                    }
            }
        }
        return "";
    }

    /**
     * Ipv4 address check.
     */
    private static final Pattern IPV4_PATTERN = Pattern.compile("^(" +
            "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
            "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");

    /**
     * Check if valid IPV4 address.
     *
     * @param input the address string to check for validity.
     * @return True if the input parameter is a valid IPv4 address.
     */
    public static boolean isIPv4Address(String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    // -------------------- IPv6 Check -------------------- */

    // 未压缩过的IPv6地址检查
    private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){7}$");

    // 压缩过的IPv6地址检查
    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^(([0-9A-Fa-f]{1,4}" +
            "(:[0-9A-Fa-f]{1,4}){0,5})?)" +                                                              // 0-6
            "::" + "(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){0,5})?)$");                                    // 0-6 hex fields.

    /**
     * Check whether the parameter is effective standard (uncompressed) IPv6 address.
     *
     * @param input IPV6 address.
     * @return True or false.
     * @see #isIPv6HexCompressedAddress(String)
     */
    public static boolean isIPv6StdAddress(final String input) {
        return IPV6_STD_PATTERN.matcher(input).matches();
    }

    /**
     * Check whether the parameter is effective compression IPv6 address.
     *
     * @param input IPV6 address.
     * @return True or false.
     * @see #isIPv6StdAddress(String)
     */
    public static boolean isIPv6HexCompressedAddress(final String input) {
        int colonCount = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ':') {
                colonCount++;
            }
        }
        return colonCount <= 7 && IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
    }

    /**
     * Check whether the IPV6 address of compressed or uncompressed.
     *
     * @param input IPV6 address.
     * @return True or false.
     * @see #isIPv6HexCompressedAddress(String)
     * @see #isIPv6StdAddress(String)
     */
    public static boolean isIPv6Address(final String input) {
        return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
    }

}
