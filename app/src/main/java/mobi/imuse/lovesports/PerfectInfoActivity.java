package mobi.imuse.lovesports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.imuse.actionsheet.ActionSheet;
import mobi.imuse.avatar.CropperActivity;
import mobi.imuse.lovesports.util.SLog;
import mobi.imuse.lovesports.util.T;
import mobi.imuse.pickview.CityPicker;
import mobi.imuse.pickview.GenderPicker;
import mobi.imuse.pickview.SportsPicker;
import mobi.imuse.pickview.TimePicker;
import mobi.imuse.pickview.lib.WheelTime;

public class PerfectInfoActivity extends BaseActivity {
    private static final String TAG = PerfectInfoActivity.class.getSimpleName();

    @Bind(R.id.toolbar)    Toolbar mToolbar;
    @Bind(R.id.ivAvatarImage)    ImageView mIvAvatarImage;
    @Bind(R.id.editRealName)    FormEditText mEditRealName;
    @Bind(R.id.tvBirthday)    TextView mTvBirthday;
    @Bind(R.id.tvProvince)  TextView mTvProvince;
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

        TimePicker.instance(this)
                .setType(WheelTime.Type.YEAR_MONTH_DAY)
                .setCyclic(true)
                .setDateSelected(date)
                .setRange(range)
                .setOnTimeSelectListener(new TimePicker.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        mTvBirthday.setText(getTime(date));
                    }
                })
                .show();
    }
    @OnClick(R.id.rlGender)
    public void onRlGenderClick(){
        String gender = mTvGender.getText().toString();
        GenderPicker.instance(this)
                .setGenderSelectListener(new GenderPicker.OnGenderSelectListener() {
                    @Override
                    public void onGenderSelected(int selected) {
                        mTvGender.setText(GenderPicker.Gender.getGender(selected).zhValue());
                    }
                })
                .show(gender);
    }

    @OnClick(R.id.rlCity)
    public void onRlCityClick(){
        String province = mTvProvince.getText().toString();
        String city = mTvCity.getText().toString();
        CityPicker.instance(this)
                .setCitySelectListener(new CityPicker.OnCitySelectListener() {
                    @Override
                    public void onCitySelected(String province, String city) {
                        mTvProvince.setText(province);
                        mTvCity.setText(city);
                    }
                })
                .show(province, city);
    }

    @OnClick(R.id.rlAvatar)
    public void onRlAvatarClick(){
        ActionSheet.createBuilder(this)
                .setOtherButtonTitles("Pick From Library", "Take Photo")
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet) {
                    }

                    @Override
                    public void onCancel(ActionSheet actionSheet) {
                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        T.showShort(PerfectInfoActivity.this, "Menu Item[" + index + "] is Clicked.");
                        if (index == 0) { // pick;
                            startPickPhoto();
                        }
                        else if (index == 1) { // take;
                            startTakePhoto();
                        }
                        else{

                        }
                    }
                })
                .show();
    }

    @OnClick(R.id.rlSportGame)
    public void onRlSportGameClick(){
        String sportgame = mTvSportGame.getText().toString();
        SportsPicker.instance(this)
                .setSelectListener(new SportsPicker.OnSelectListener() {
                    @Override
                    public void onSelected(int selected) {
                        mTvSportGame.setText(SportsPicker.Sports.getSport(selected).zhValue());
                    }
                })
                .show(sportgame);
    }

    private void startPickPhoto() {
        Intent intent = new Intent(this, CropperActivity.class);
        intent.putExtra("PickWay", "PICK");
        startActivityForResult(intent, 10001);
    }

    private void startTakePhoto() {
        Intent intent = new Intent(this, CropperActivity.class);
        intent.putExtra("PickWay", "TAKE");
        startActivityForResult(intent, 10002);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            SLog.d(TAG, "Canceled Avatar Capture.");
            return;
        }
        // 根据上面发送过去的请求码来区别
        switch (requestCode) {
            case 10001:
            case 10002:
                final String path = data.getStringExtra("CropperPhotoPath");
                SLog.d(TAG, "CropperPhotoPath = " + path);
                if (path != null && path.length() > 0) {
                    Glide.with(this)
                            .load(path)
                            .asBitmap()
                            .fitCenter()
                            .into(mIvAvatarImage);
                }
                break;
            default:
                break;
        }
    }
}
