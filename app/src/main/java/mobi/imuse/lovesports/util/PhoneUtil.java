package mobi.imuse.lovesports.util;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by suyanlu on 15/2/27.
 */
public class PhoneUtil {
    private static TelephonyManager mTelephonyManager;
    private static String mDeviceId;
    private static String mDeviceSoftwareVersion;
    private static String mSubscriberId;

    public static TelephonyManager getTelephonyManager(Context context) {
        // 获取telephony系统服务，用于取得SIM卡和网络相关信息
        if (mTelephonyManager == null) {
            mTelephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        }
        return mTelephonyManager;
    }

    /**
     * 唯一的设备ID： GSM手机的 IMEI 和 CDMA手机的 MEID. Return null if device ID is not
     * 取得手机IMEI
     * available.
     */
    public static String getDeviceId(Context context) {
        mDeviceId = getTelephonyManager(context).getDeviceId();// String
        return mDeviceId;
    }

    /**
     * 取得IMEI SV
     * 设备的软件版本号： 返回移动终端的软件版本，例如：GSM手机的IMEI/SV码。 例如：the IMEI/SV(software version)
     * for GSM phones. Return null if the software version is not available.
     */
    public static String getDeviceSoftwareVersion(Context context) {
        mDeviceSoftwareVersion = getTelephonyManager(context).getDeviceSoftwareVersion();// String
        return mDeviceSoftwareVersion;
    }

    /**
     * 取得手机IMSI
     * 返回用户唯一标识，比如GSM网络的IMSI编号 唯一的用户ID： 例如：IMSI(国际移动用户识别码) for a GSM phone.
     * 需要权限：READ_PHONE_STATE
     */
    public static String getSubscriberId(Context context) {
        mSubscriberId = getTelephonyManager(context).getSubscriberId();// String
        return mSubscriberId;
    }
}
