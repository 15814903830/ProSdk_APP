package com.zero.ci.tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static com.zero.ci.network.http.NoHttp.getContext;


/**
 * @author: Zero Yuan
 * @Email: zero.yuan.xin@gmail.com
 * @DATE : 17/9/18/上午10:54
 * @Description: 设备ID
 */
public class DeviceUuidUtil {

    protected static final String PREFS_FILE = "device_id.xml";
    protected static final String PREFS_DEVICE_ID = "device_id";
    protected static UUID uuid;
    Context mContext;

    public static DeviceUuidUtil instance;


    public static DeviceUuidUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (DeviceUuidUtil.class) {
                instance = new DeviceUuidUtil(context);
            }
        }
        return instance;
    }

    public DeviceUuidUtil(Context context) {
        this.mContext = context;
        initUUID(mContext);
    }

    private void initUUID(Context context) {
        if (uuid == null) {
            synchronized (DeviceUuidUtil.class) {
                if (uuid == null) {
                    final SharedPreferences prefs = context
                            .getSharedPreferences(PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null);
                    if (id != null) {
                        // Use the ids previously computed and stored in the
                        // prefs file
                        uuid = UUID.fromString(id);
                    } else {
                        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                        // Use the Android ID unless it's broken, in which case
                        // fallback on deviceId,
                        // unless it's not available, then fallback on a random
                        // number which we store to a prefs file
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                            } else {
                                @SuppressLint("MissingPermission") final String deviceId =
                                        ((TelephonyManager) context.
                                                getSystemService(Context.TELEPHONY_SERVICE)).
                                                getDeviceId();
                                uuid = deviceId
                                        !=
                                        null
                                        ?
                                        UUID.nameUUIDFromBytes(deviceId.getBytes("utf8"))
                                        :
                                        UUID.randomUUID();
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                        // Write the value out to the prefs file
                        prefs.edit()
                                .putString(PREFS_DEVICE_ID, uuid.toString())
                                .commit();
                    }
                }
            }
        }
    }

    public UUID getDeviceUuid() {
        return uuid;
    }

//=================================================================================================================================================
    /**
     * 1.不需要特定权限.
     2.在99.5% Android装置（包括root过的）上，即API => 9，保证唯一性.
     3.重装app之后仍能取得相同唯一值.
     */
    /**
     * Return pseudo unique ID
     *
     * @return ID
     */
    public static String getUniquePseudoID() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their device or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        // 35是IMEI开头的号
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10;

        // Thanks to @Roman SL!
        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their device, there will be a duplicate entry
        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and myreturn the serial for api => 9
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            //serial需要一个初始化
            serial = "serial"; // some value  随便一个初始化
        }

        // Thanks @Joe!
        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

//=======================================================================================================================
    /***
     *好处：

     1.不需要特定权限.
     2.在100% Android装置（包括root过的）上，保证唯一性.
     坏处

     1.重装app之后不能取得相同唯一值.
     */
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    public synchronized String getUniqueID() {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = mContext.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }


    //====================================================================================================================

    public static String getAndroidId() {
        return Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}