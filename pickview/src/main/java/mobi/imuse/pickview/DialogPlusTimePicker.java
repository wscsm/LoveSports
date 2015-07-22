package mobi.imuse.pickview;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import mobi.imuse.pickview.lib.ScreenInfo;
import mobi.imuse.pickview.lib.WheelTime;

/**
 * 时间选择器
 *
 * @author Sai
 */
public class DialogPlusTimePicker {

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

    public void show(Context context) {

        mContext = context;
        if (dialog != null && dialog.isShowing()) {
            return;
        }

        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        Holder holder = new ViewHolder(createView());

        View footer = new View(mContext);
        footer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getNavigationBarHeight()));
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

    public WheelTime.Type getType() {
        return mType;
    }

    public void setType(WheelTime.Type mType) {
        this.mType = mType;
    }

    public Date getDateSelected() {
        return mDateSelected;
    }

    public void setDateSelected(Date mDateSelected) {
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

    }

    public int[] getRange() {
        return range;
    }

    public void setRange(int[] range) {
        this.range[0] = range[0];
        this.range[1] = range[1];

        WheelTime.setSTART_YEAR(this.range[START_YEAR]);
        WheelTime.setEND_YEAR(this.range[END_YEAR]);
    }

    public boolean isCyclic() {
        return bCyclic;
    }

    public void setCyclic(boolean bCyclic) {
        this.bCyclic = bCyclic;
        if (wheelTime != null) {
            wheelTime.setCyclic(this.bCyclic);
        }
    }

    public OnTimeSelectListener getListener() {
        return mListener;
    }

    public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
        this.mListener = timeSelectListener;
    }

    public interface OnTimeSelectListener {
        public void onTimeSelect(Date date);

    }

    public static Builder createBuilder(Context context) {
        return new Builder(context);
    }

    public static class Builder {

        Context mContext;
        DialogPlusTimePicker timePicker;

        public Builder(Context context) {
            mContext = context;
            if (timePicker == null) {
                timePicker = new DialogPlusTimePicker();
            }
            // 初始化;
            init();
        }

        public void init() {
            if (timePicker != null) {
                setType(WheelTime.Type.ALL);
                setListener(null);
                setDateSelected(null);
                int[] a = {1970, 2038};
                setRange(a);
                setCyclic(false);
            }
        }

        public Builder setType(WheelTime.Type type) {
            timePicker.setType(type);
            return this;
        }

        public Builder setListener(OnTimeSelectListener listener) {
            timePicker.setOnTimeSelectListener(listener);
            return this;
        }

        public Builder setDateSelected(Date dateSelected) {
            timePicker.setDateSelected(dateSelected);
            return this;
        }

        public Builder setRange(int[] range) {
            timePicker.setRange(range);
            return this;
        }

        public Builder setCyclic(boolean cyclic) {
            timePicker.setCyclic(cyclic);
            return this;
        }

        public Bundle prepareArguments() {
            Bundle bundle = new Bundle();
            return bundle;
        }

        public DialogPlusTimePicker show() {
            timePicker.show(mContext);
            return timePicker;
        }

    }
}
