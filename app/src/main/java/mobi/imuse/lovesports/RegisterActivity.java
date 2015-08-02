package mobi.imuse.lovesports;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.andreabaccega.widget.FormEditText;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.imuse.lovesports.model.SmsResponse;
import mobi.imuse.lovesports.util.LsHttpClient;
import mobi.imuse.lovesports.util.SLog;
import mobi.imuse.lovesports.util.T;

@SuppressWarnings("deprecation")
public class RegisterActivity extends BaseActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Bind(R.id.toolbar)    Toolbar mToolbar;
    @Bind(R.id.btnSendSms)    Button mBtnSendSms;
    @Bind(R.id.editMobilePhone)    FormEditText mEditMobilePhone;
    @Bind(R.id.editSmsAuthCode)    FormEditText mEditSmsAuthCode;
    @Bind(R.id.editRealName)    FormEditText mEditRealName;
    @Bind(R.id.editLoginPassword)    FormEditText mEditLoginPassword;

    private CountDownTimer mCountDownTimer = new CountDownTimer(60000, 1000) {
        // 每秒变化一次;
        public void onTick(long millisUntilFinished) {
            mBtnSendSms.setText("" + millisUntilFinished / 1000 + "秒后重新发送");
        }

        public void onFinish() {
            mBtnSendSms.setText("重新发送");
            mBtnSendSms.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setSystemBar();
        setSupportActionBar(mToolbar);
    }

    @OnClick(R.id.btnNextStep)
    public void onBtnNextStepClick() {
        startActivity(new Intent(RegisterActivity.this, PerfectInfoActivity.class));
    }

    @OnClick(R.id.tvTitleArrowBtnLeft)
    public void onTvTitleArrowBtnLeftClick() {
        onBackPressed();
    }

    @OnClick(R.id.btnSendSms)
    public void onBtnSendSmsClick() {
        if (!mEditMobilePhone.testValidity()) {
            T.showShort(this, getString(R.string.error_input_phone));
            return;
        }
        mBtnSendSms.setEnabled(false);
        mCountDownTimer.cancel();
        mCountDownTimer.start();

        RequestParams params = new RequestParams(
                "phone", mEditMobilePhone.getText().toString(),
                "type", "Register");
        SLog.d(TAG, "params is : " + params.toString());

        LsHttpClient.post("haodong/api/sms/send_sms.php", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                SLog.d(TAG, "response of SMS send: " + response.toString());
                try {
                    int errorCode = response.getInt("Error");
                    Gson gson = new Gson();
                    if (errorCode == 0) {
                        SmsResponse rsp = gson.fromJson(response.toString(), SmsResponse.class);
                    }
                    else {
                        SLog.d(TAG, "ErrorCode: " + errorCode + ", ErrorMsg: " + response.getString("ErrorMsgCN"));
                        T.showShort(RegisterActivity.this, "Error: " + errorCode);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                SLog.d(TAG, "statusCode = " + statusCode);
                if (errorResponse != null) {
                    SLog.d(TAG, "reponse is : " + errorResponse.toString());
                    T.showShort(RegisterActivity.this, errorResponse.toString());
                }
            }
        });
    }
}
