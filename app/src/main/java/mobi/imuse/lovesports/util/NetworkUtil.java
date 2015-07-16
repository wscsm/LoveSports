package mobi.imuse.lovesports.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    public static final int TYPE_WIFI = 0;
    public static final int TYPE_MOBILE = 1;
    public static final int TYPE_NOT_CONNECTED = -1;
    
    public static final String TYPE_WIFI_STRING = "wifi";
    public static final String TYPE_MOBILE_STRING = "mobile data";
    public static final String TYPE_NOT_CONNECTED_STRING = "not connected";

    public static final String TYPES[] = {TYPE_WIFI_STRING, TYPE_MOBILE_STRING, TYPE_NOT_CONNECTED_STRING};

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        } 
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = "Wifi enabled";
        } 
        else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = "Mobile data enabled";
        } 
        else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }
}