package mobi.imuse.lovesports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.andreabaccega.widget.FormEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.toolbar)    Toolbar mToolbar;
    @Bind(R.id.editMobilePhone)    FormEditText mEditMobilePhone;
    @Bind(R.id.editSmsAuthCode)    FormEditText mEditSmsAuthCode;
    @Bind(R.id.editRealName)    FormEditText mEditRealName;
    @Bind(R.id.editLoginPassword)    FormEditText mEditLoginPassword;

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
    public void onTvTitleArrowBtnLeftClick(){
        onBackPressed();
    }
}
