package mobi.imuse.lovesports;

import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreLoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);
        ButterKnife.bind(this);
        setSystemBar();
    }

    @OnClick(R.id.btnLoginWithMobile)
    public void onBtnLoginWithMobileClick(){
        startActivity(new Intent(PreLoginActivity.this, LoveSportsActivity.class));
    }

    @OnClick(R.id.btnRegister)
    public void onBtnRegisterClick(){
        
    }
}
