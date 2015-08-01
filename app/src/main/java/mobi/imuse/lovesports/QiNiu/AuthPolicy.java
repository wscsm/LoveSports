package mobi.imuse.lovesports.QiNiu;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONStringer;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import mobi.imuse.lovesports.Constants;

public class AuthPolicy {
    public String scope;
    public String callbackUrl;
    public String returnUrl;
    public long deadline;

    public AuthPolicy(String scope, long expires) {
        this.scope = scope;
        this.deadline = System.currentTimeMillis() / 1000 + expires;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String marshal() throws JSONException {

        JSONStringer stringer = new JSONStringer();

        stringer.object();
        stringer.key("scope").value(this.scope);
        if (this.callbackUrl != null) {
            stringer.key("callbackUrl").value(this.callbackUrl);
        }
        if (this.returnUrl != null) {
            stringer.key("returnUrl").value(this.returnUrl);
        }
        stringer.key("deadline").value(this.deadline);
        stringer.endObject();

        return stringer.toString();
    }

    public byte[] makeAuthToken() {

        byte[] accessKey = Config.ACCESS_KEY.getBytes();
        byte[] secretKey = Config.SECRET_KEY.getBytes();
        try {
            String policyJson = this.marshal();
            byte[] policyBase64 = urlsafeEncodeBytes(policyJson.getBytes());

            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec keySpec = new SecretKeySpec(secretKey, "HmacSHA1");
            mac.init(keySpec);

            byte[] digest = mac.doFinal(policyBase64);
            byte[] digestBase64 = urlsafeEncodeBytes(digest);
            byte[] token = new byte[accessKey.length + 30 + policyBase64.length];

            System.arraycopy(accessKey, 0, token, 0, accessKey.length);
            token[accessKey.length] = ':';
            System.arraycopy(digestBase64, 0, token, accessKey.length + 1, digestBase64.length);
            token[accessKey.length + 29] = ':';
            System.arraycopy(policyBase64, 0, token, accessKey.length + 30, policyBase64.length);

            return token;
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String makeAuthTokenString() {
        byte[] authToken = this.makeAuthToken();
        return new String(authToken);
    }

    public static String makeSportsToken(){
        AuthPolicy policy = new AuthPolicy(Constants.QINIU_BUCKET_NAME, 3600);
        String token = policy.makeAuthTokenString();
        return token;
    }

    public String tokenWithEncodedEntryURI(String key) {
        byte[] accessKey = Config.ACCESS_KEY.getBytes();
        byte[] secretKey = Config.SECRET_KEY.getBytes();

        // 这里scope传入的就是bucketName;
        String entryURI = this.scope + ":" + key;
        String url = "/delete/" + urlsafeEncode(entryURI);

        try {
            byte[] policyBase64 = (url + "\n").getBytes();

            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec keySpec = new SecretKeySpec(secretKey, "HmacSHA1");
            mac.init(keySpec);

            byte[] digest = mac.doFinal(policyBase64);
            byte[] digestBase64 = urlsafeEncodeBytes(digest);
            byte[] token = new byte[accessKey.length + 29];

            System.arraycopy(accessKey, 0, token, 0, accessKey.length);
            token[accessKey.length] = ':';
            System.arraycopy(digestBase64, 0, token, accessKey.length + 1, digestBase64.length);

            return new String(token);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] urlsafeEncodeBytes(byte[] src) {
        if (src.length % 3 == 0) {
            return Base64.encode(src, Base64.URL_SAFE | Base64.NO_WRAP);
        }

        byte[] b = Base64.encode(src, Base64.URL_SAFE | Base64.NO_WRAP);
        if (b.length % 4 == 0) {
            return b;
        }

        int pad = 4 - b.length % 4;
        byte[] b2 = new byte[b.length + pad];
        System.arraycopy(b, 0, b2, 0, b.length);
        b2[b.length] = '=';
        if (pad > 1) {
            b2[b.length + 1] = '=';
        }
        return b2;
    }

    public static String urlsafeEncodeString(byte[] src) {
        return new String(urlsafeEncodeBytes(src));
    }

    public static String urlsafeEncode(String text) {
        return new String(urlsafeEncodeBytes(text.getBytes()));
    }

    public static byte[] urlsafeDecode(String text) {
        byte[] buf = Base64.decode(text, Base64.URL_SAFE | Base64.NO_WRAP);
        return buf;
    }
}
