package mobi.imuse.lovesports.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

public class LsHttpClient {
    private static final String BASE_URL = "http://118.192.93.37/";
    
    private static AsyncHttpClient client = new AsyncHttpClient();

    // 这个用原始Url;
    public static void getUrl(String url, ResponseHandlerInterface responseHandler) {
        client.get(url, responseHandler);
    }

    // 下面的都是封装了基本URL的调用;
    public static void get(String url, ResponseHandlerInterface responseHandler) {
        client.get(getAbsoluteUrl(url), responseHandler);
    }
    
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, ResponseHandlerInterface responseHandler) {
        client.post(getAbsoluteUrl(url), responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
