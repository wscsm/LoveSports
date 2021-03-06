package mobi.imuse.pickview;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import mobi.imuse.pickview.lib.NavigationBarUtil;
import mobi.imuse.pickview.lib.ScreenInfo;
import mobi.imuse.pickview.lib.WheelTime;

/**
 * 时间选择器
 *
 * @author Sai
 */
public class TimePicker {

    Context mContext;
    private DialogPlus dialog;
    private WheelTime wheelTime;

    private WheelTime.Type mType = WheelTime.Type.ALL;
    private Date mDateSelected; // 选中的日期;
    private static final int START_YEAR = 0;
    private static final int END_YEAR = 1;
    private int[] range = {1970, 2038};
    private boolean bCyclic = false; // 是否循环显示;
    private OnTimeSelectListener mListener;

    private static TimePicker instance;

    public static TimePicker instance(Context context){
        if (instance == null){
            instance = new TimePicker(context);
        }
        instance.init();
        return instance;
    }

    private TimePicker(Context context){
        mContext = context;
    }

    public void init() {
        if (instance != null) {
            int[] a = {1970, 2038};
            this.setType(WheelTime.Type.ALL)
                    .setOnTimeSelectListener(null)
                    .setDateSelected(null)
                    .setRange(a)
                    .setCyclic(false);
        }
    }

    private void closeInputKeyboard(){
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            View focusView = ((AppCompatActivity)mContext).getCurrentFocus();
            if (focusView != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }
    }

    public void show() {
        if (dialog != null && dialog.isShowing()) {
            return;
        }

        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        Holder holder = new ViewHolder(createView());

        View footer = new View(mContext);
        // 只有在4.4以上版本，采用这个View来填充下面导航栏的位置，避免错位.
        footer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, NavigationBarUtil.getNavigationBarHeight(mContext)));
        dialog = DialogPlus.newDialog(mContext)
                .setContentHolder(holder)
                .setGravity(Gravity.BOTTOM)
                .setOnDismissListener(dismissListener)
                .setOnCancelListener(cancelListener)
                .setOnClickListener(clickListener)
                .setExpanded(false)
                .setCancelable(true)
                .setFooter(footer)
                .create();
        closeInputKeyboard();
        dialog.show();
    }

    OnDismissListener dismissListener = new OnDismissListener() {
        @Override
        public void onDismiss(DialogPlus dialog) {
        }
    };

    OnCancelListener cancelListener = new OnCancelListener() {
        @Override
        public void onCancel(DialogPlus dialog) {
        }
    };

    OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(DialogPlus dialog, View view) {
            int viewId = view.getId();
            if (viewId == R.id.btnSubmit) {
                try {
                    Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                    mListener.onTimeSelect(date);
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            dialog.dismiss();
        }
    };

    private int getNavigationBarHeight(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Resources resources = mContext.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return resources.getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }
    private View createView() {
        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        LinearLayout rootView = (LinearLayout) mLayoutInflater.inflate(R.layout.pw_time, null);

        // ----时间转轮
        final View timepickerview = rootView.findViewById(R.id.timepicker);
        ScreenInfo screenInfo = new ScreenInfo((AppCompatActivity) mContext);
        wheelTime = new WheelTime(timepickerview, mType);

        wheelTime.screenheight = screenInfo.getHeight();

        //默认选中当前时间
        Calendar calendar = Calendar.getInstance();
        if (mDateSelected == null) {
            calendar.setTimeInMillis(System.currentTimeMillis());
        }
        else {
            calendar.setTime(mDateSelected);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelTime.setPicker(year, month, day, hours, minute);

        wheelTime.setCyclic(bCyclic);
        return rootView;
    }

    public TimePicker setType(WheelTime.Type mType) {
        this.mType = mType;
        return this;
    }

    public TimePicker setDateSelected(Date mDateSelected) {
        this.mDateSelected = mDateSelected;
        Calendar calendar = Calendar.getInstance();
        if (wheelTime != null) {
            if (this.mDateSelected == null)
                calendar.setTimeInMillis(System.currentTimeMillis());
            else
                calendar.setTime(this.mDateSelected);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            wheelTime.setPicker(year, month, day, hours, minute);
        }
        return this;
    }

    public TimePicker setRange(int[] range) {
        this.range = range;

        WheelTime.setSTART_YEAR(this.range[START_YEAR]);
        WheelTime.setEND_YEAR(this.range[END_YEAR]);
        return this;
    }

    public TimePicker setCyclic(boolean bCyclic) {
        this.bCyclic = bCyclic;
        if (wheelTime != null) {
            wheelTime.setCyclic(this.bCyclic);
        }
        return this;
    }

    public TimePicker setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
        this.mListener = timeSelectListener;
        return this;
    }

    public interface OnTimeSelectListener {
        public void onTimeSelect(Date date);
    }

}
