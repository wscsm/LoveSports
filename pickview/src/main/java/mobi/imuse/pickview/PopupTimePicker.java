package mobi.imuse.pickview;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
public class PopupTimePicker extends Fragment implements OnClickListener {

    private static final String ARG_CANCELABLE_ONTOUCHOUTSIDE = "cancelable_ontouchoutside";
    private static final String ARG_TYPE = "type";

    private static final int TRANSLATE_DURATION = 200;
    private static final int ALPHA_DURATION = 300;

    private View mView;
    private LinearLayout mPanel; // 扩展pw_time.xml;
    private ViewGroup mGroup;
    private View mBg;
    private WheelTime wheelTime;

    private boolean mDismissed = true;
    private WheelTime.Type mType = WheelTime.Type.ALL;

    private static final int START_YEAR = 0;
    private static final int END_YEAR = 1;

    private Date mDateSelected; // 选中的日期;
    private int[] range = {1970, 2038};

    private boolean bCyclic = false; // 是否循环显示;

    private boolean bCancelableOnTouchOutside = false; // 界面外是否可以dismiss;

    private static final String TAG_BG = "timebg";
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";

    private OnTimeSelectListener mListener;

    public void show(FragmentManager manager, String tag) {
        if (!mDismissed) {
            return;
        }
        mDismissed = false;
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void dismiss() {
        if (mDismissed) {
            return;
        }
        mDismissed = true;
        getFragmentManager().popBackStack();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(this);
        ft.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            View focusView = getActivity().getCurrentFocus();
            if (focusView != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }

        mView = createView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mView.setFitsSystemWindows(true);
        }

        mGroup = (ViewGroup) getActivity().getWindow().getDecorView();

        mGroup.addView(mView);
        mBg.startAnimation(createAlphaInAnimation());
        mPanel.startAnimation(createTranslationInAnimation());


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private View createView() {
        FrameLayout parent = new FrameLayout(getActivity());
        parent.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mBg = new View(getActivity());
        mBg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mBg.setBackgroundColor(Color.argb(136, 0, 0, 0));
        mBg.setTag(TAG_BG);
        mBg.setOnClickListener(this);

        LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
        mPanel = (LinearLayout) mLayoutInflater.inflate(R.layout.pw_time, null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        mPanel.setLayoutParams(params);
        mPanel.setOrientation(LinearLayout.VERTICAL);

        parent.addView(mBg);
        parent.addView(mPanel);

        // -----确定和取消按钮
        View btnSubmit = mPanel.findViewById(R.id.btnSubmit);
        btnSubmit.setTag(TAG_SUBMIT);
        View btnCancel = mPanel.findViewById(R.id.btnCancel);
        btnCancel.setTag(TAG_CANCEL);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        // ----时间转轮
        final View timepickerview = mPanel.findViewById(R.id.timepicker);
        ScreenInfo screenInfo = new ScreenInfo(getActivity());
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
        return parent;
    }

    @Override
    public void onDestroyView() {
        mPanel.startAnimation(createTranslationOutAnimation());
        mBg.startAnimation(createAlphaOutAnimation());
        mView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mGroup.removeView(mView);
            }
        }, ALPHA_DURATION);

        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag != null && tag.equals(TAG_BG) && !isCancelableOnTouchOutside()) {
            return;
        }

        if (tag == null || tag.equals(TAG_CANCEL)) {
            dismiss();
            return;
        }
        else {
            if (mListener != null) {
                try {
                    Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                    mListener.onTimeSelect(date);
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            dismiss();
            return;
        }
    }

    private Animation createTranslationInAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type, 1, type, 0);
        an.setDuration(TRANSLATE_DURATION);
        return an;
    }

    private Animation createAlphaInAnimation() {
        AlphaAnimation an = new AlphaAnimation(0, 1);
        an.setDuration(ALPHA_DURATION);
        return an;
    }

    private Animation createTranslationOutAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type, 0, type, 1);
        an.setDuration(TRANSLATE_DURATION);
        an.setFillAfter(true);
        return an;
    }

    private Animation createAlphaOutAnimation() {
        AlphaAnimation an = new AlphaAnimation(1, 0);
        an.setDuration(ALPHA_DURATION);
        an.setFillAfter(true);
        return an;
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

    public boolean isCancelableOnTouchOutside() {
        return bCancelableOnTouchOutside;
    }

    public void setCancelableOnTouchOutside(boolean bCancelableOnTouchOutside) {
        this.bCancelableOnTouchOutside = bCancelableOnTouchOutside;
    }

    public OnTimeSelectListener getmListener() {
        return mListener;
    }

    public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
        this.mListener = timeSelectListener;
    }

    public interface OnTimeSelectListener {
        public void onTimeSelect(Date date);

    }

    public static Builder createBuilder(Context context, FragmentManager fragmentManager) {
        return new Builder(context, fragmentManager);
    }

    public static class Builder {

        PopupTimePicker timePicker;
        private FragmentManager mFragmentManager;
        private String mTag = "timePicker";

        public Builder(Context context, FragmentManager fragmentManager) {
            mFragmentManager = fragmentManager;
            if (timePicker == null) {
                timePicker = (PopupTimePicker) Fragment.instantiate(context, PopupTimePicker.class.getName(), prepareArguments());
            }
            // 初始化;
            init();
        }

        public void init(){
            if (timePicker != null){
                setType(WheelTime.Type.ALL);
                setTag(mTag);
                setListener(null);
                setCancelableOnTouchOutside(false);
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

        public Builder setTag(String tag) {
            mTag = tag;
            return this;
        }

        public Builder setListener(OnTimeSelectListener listener) {
            timePicker.setOnTimeSelectListener(listener);
            return this;
        }

        public Builder setCancelableOnTouchOutside(boolean cancelable) {
            timePicker.setCancelableOnTouchOutside(cancelable);
            return this;
        }

        public Builder setDateSelected(Date dateSelected){
            timePicker.setDateSelected(dateSelected);
            return this;
        }

        public Builder setRange(int[] range){
            timePicker.setRange(range);
            return this;
        }

        public Builder setCyclic(boolean cyclic){
            timePicker.setCyclic(cyclic);
            return this;
        }

        public Bundle prepareArguments() {
            Bundle bundle = new Bundle();
            return bundle;
        }

        public PopupTimePicker show() {
            timePicker.show(mFragmentManager, mTag);
            return timePicker;
        }

    }
}
