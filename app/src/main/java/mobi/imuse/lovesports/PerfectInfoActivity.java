package mobi.imuse.lovesports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.imuse.pickview.TimePicker;

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        String currentBirthday = mTvBirthday.getText().toString();
        String[] a = currentBirthday.split("-");
        Date date;
        if (currentBirthday != null && !currentBirthday.equals("")){
            Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(a[0]), Integer.parseInt(a[1])-1, Integer.parseInt(a[2]));
            date = new Date(cal.getTimeInMillis());

        }
        else{
            date = new Date();
        }

        int[] range = {1970, 2038};

        TimePicker.createBuilder(this, getSupportFragmentManager())
                .setType(TimePicker.Type.YEAR_MONTH_DAY)
                .setCyclic(true)
                .setDateSelected(date)
                .setCancelableOnTouchOutside(true)
                .setRange(range)
                .setListener(new TimePicker.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        mTvBirthday.setText(getTime(date));
                    }
                })
                .show();

/*
        TimePopupWindow.createBuilder(this, getSupportFragmentManager())
                .setCancelableOnTouchOutside(true)
                .setType(TimePopupWindow.Type.YEAR_MONTH_DAY)
                .show();
*/
/*
        //时间选择器
        pwBirthdayTime = new TimePopupWindow(PerfectInfoActivity.this, TimePopupWindow.Type.YEAR_MONTH_DAY);
        pwBirthdayTime.setRange(1970, 2100);
        //时间选择后回调
        pwBirthdayTime.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                mTvBirthday.setText(getTime(date));
            }
        });
        String currentBirthday = mTvBirthday.getText().toString();
        String[] a = currentBirthday.split("-");
        Date date;
        if (currentBirthday != null && !currentBirthday.equals("")){
            Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(a[0]), Integer.parseInt(a[1])-1, Integer.parseInt(a[2]));
            date = new Date(cal.getTimeInMillis());

        }
        else{
            date = new Date();
        }
        //弹出时间选择器
        pwBirthdayTime.showAtLocation(findViewById(R.id.activityPerfectInfo), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0, date);
*/
    }
}
