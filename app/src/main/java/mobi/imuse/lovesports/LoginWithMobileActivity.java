package mobi.imuse.lovesports;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.andreabaccega.widget.FormEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginWithMobileActivity extends BaseActivity {

    @Bind(R.id.toolbar)    Toolbar mToolbar;
    @Bind(R.id.editMobilePhone)    FormEditText mEditMobilePhone;
    @Bind(R.id.editPassword)    FormEditText mEditPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_mobile);
        ButterKnife.bind(this);

        setSystemBar();
        setSupportActionBar(mToolbar);
    }

    @OnClick(R.id.tvTitleArrowBtnLeft)
    public void onArrowBtnLeftClick(){
        onBackPressed();
    }

    @OnClick(R.id.btnLogin)
    public void onBtnLoginClick(){
        if (!mEditMobilePhone.testValidity() || !mEditPassword.testValidity()){
            return;
        }
    }

    @OnClick(R.id.btnRegister)
    public void onBtnRegisterClick(){

    }
}
