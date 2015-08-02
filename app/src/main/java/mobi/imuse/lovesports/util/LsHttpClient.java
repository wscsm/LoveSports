package mobi.imuse.lovesports.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import mobi.imuse.lovesports.Constants;

public class LsHttpClient {
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
        return Constants.BASE_URL + relativeUrl;
    }
}
