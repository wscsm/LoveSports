package mobi.imuse.lovesports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.imuse.pickview.TimePopupWindow;

public class PerfectInfoActivity extends BaseActivity {

    @Bind(R.id.toolbar)    Toolbar mToolbar;
    @Bind(R.id.ivAvatarImage)    ImageView mIvAvatarImage;
    @Bind(R.id.editRealName)    FormEditText mEditRealName;
    @Bind(R.id.tvBirthday)    TextView mTvBirthday;
    @Bind(R.id.tvCity)    TextView mTvCity;
    @Bind(R.id.tvSportGame)    TextView mTvSportGame;
    @Bind(R.id.editBodyLength)    FormEditText mEditBodyLength;
    @Bind(R.id.editBodyWeight)    FormEditText mEditBodyWeight;
    @Bind(R.id.editTeachingExperience)    FormEditText mEditTeachingExperience;
    @Bind(R.id.tvGender)    TextView mTvGender;
    @Bind(R.id.editOrganisation)    EditText mEditOrganisation;
    @Bind(R.id.editSkills)  EditText mEditSkills;
    @Bind(R.id.editDetailIntroduction)  EditText mEditeditDetailIntroduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_info);
        ButterKnife.bind(this);
        setSystemBar();
        setSupportActionBar(mToolbar);
    }

    @OnClick(R.id.btnSubmit)
    public void onBtnSubmitClick(){
        Intent intent = new Intent(PerfectInfoActivity.this, LoveSportsActivity.class);
        intent.putExtra("key", 6);
        startActivity(intent);
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    // 获取生日;
    @OnClick(R.id.rlBirthday)
    public void onRlBirthdayClick(){
        TimePopupWindow pwTime;
        //时间选择器
        pwTime = new TimePopupWindow(PerfectInfoActivity.this, TimePopupWindow.Type.YEAR_MONTH_DAY);
        pwTime.setRange(1970, 2100);
        //时间选择后回调
        pwTime.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                mTvBirthday.setText(getTime(date));
            }
        });
        //弹出时间选择器
        pwTime.showAtLocation(mTvBirthday, Gravity.BOTTOM, 0, 0, new Date());
    }
}
