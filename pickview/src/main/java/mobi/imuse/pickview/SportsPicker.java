package mobi.imuse.pickview;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import mobi.imuse.pickview.lib.NavigationBarUtil;
import mobi.imuse.pickview.lib.ScreenInfo;
import mobi.imuse.pickview.lib.WheelOptions;

/**
 * 选项选择器，可支持一二三级联动选择
 *
 * @author Sai
 */
public class SportsPicker {

    Context mContext;
    private DialogPlus dialog;

    private WheelOptions wheelOptions;
    private OnSelectListener selectListener;

    private static SportsPicker instance;

    private SportsPicker(Context context) {
        mContext = context;
    }

    public static SportsPicker instance(Context context) {
        if (instance == null) {
            instance = new SportsPicker(context);
        }
        instance.init();
        return instance;
    }

    private void init() {
        if (dialog != null && dialog.isShowing()) {
            return;
        }

        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        Holder holder = new ViewHolder(R.layout.pw_options);
//        View footer = new View(mContext);
//        footer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, NavigationBarUtil.getNavigationBarHeight(mContext)));
        dialog = DialogPlus.newDialog(mContext)
                .setContentHolder(holder)
                .setGravity(Gravity.BOTTOM)
                .setOnDismissListener(dismissListener)
                .setOnCancelListener(cancelListener)
                .setOnClickListener(clickListener)
                .setExpanded(false)
                .setCancelable(true)
//                .setFooter(footer)
                .create();
        // ----转轮
        final View optionspicker = dialog.getHolderView().findViewById(R.id.optionspicker);
        ScreenInfo screenInfo = new ScreenInfo((Activity) mContext);
        wheelOptions = new WheelOptions(optionspicker);

        wheelOptions.screenheight = screenInfo.getHeight();

        optionspicker.setPadding(
                optionspicker.getPaddingLeft(),
                optionspicker.getPaddingTop(),
                optionspicker.getPaddingRight(),
                optionspicker.getPaddingBottom() + NavigationBarUtil.getNavigationBarHeight(mContext));
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

    public void show(String currentSport) {
        closeInputKeyboard();
        this.setPicker(sportsList)
                .setSport(currentSport == null ? "不设置" : currentSport)
                .setCyclic(false);
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
            if (viewId == R.id.btnSubmit && selectListener != null) {
                int[] optionsCurrentItems = wheelOptions.getCurrentItems();
                selectListener.onSelected(optionsCurrentItems[0]);
            }
            dialog.dismiss();
        }
    };

    private int getNavigationBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Resources resources = mContext.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return resources.getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    private SportsPicker setPicker(ArrayList<String> optionsItems) {
        wheelOptions.setPicker(optionsItems, null, null, false);
        return this;
    }

    private SportsPicker setSport(String gender) {
        Sports g = Sports.getSport(gender);
        if (g == null) g = Sports.Tennis;
        wheelOptions.setCurrentItems(g.intValue(), 0, 0);
        return this;
    }

    private SportsPicker setCyclic(boolean cyclic) {
        wheelOptions.setCyclic(cyclic);
        return this;
    }

    public interface OnSelectListener {
        public void onSelected(int selected);
    }

    public SportsPicker setSelectListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
        return this;
    }

    public enum Sports {
        //通过括号赋值,而且必须带有一个参构造器和一个属性跟方法，否则编译出错
        //赋值必须都赋值或都不赋值，不能一部分赋值一部分不赋值；如果不赋值则不能写构造器，赋值编译也出错
        Tennis      (0, "Tennis",   "网球"),
        Golf        (1, "Golf",     "高尔夫球"),
        Badminton   (2, "Badminton","羽毛球"),
        PingPong    (3, "Pingpong", "乒乓球");

        private int intValue;
        private String value;
        private String zhValue;

        //构造器默认也只能是private, 从而保证构造函数只能在内部使用
        Sports(int intValue, String value, String zhValue) {
            this.intValue = intValue;
            this.value = value;
            this.zhValue = zhValue;
        }

        public int intValue() {
            return intValue;
        }

        public String value() {
            return value;
        }

        public String zhValue() {
            return zhValue;
        }

        //定義這個列舉項目所需要的 HashMap
        private static Map<Integer, Sports> intSportsList = new HashMap<>();
        private static Map<String, Sports> enSportsList = new HashMap<>();
        private static Map<String, Sports> zhSportsList = new HashMap<>();
        //使用 for-loop 為每個項目代入其定義的數值
        static {
            for (Sports gender : EnumSet.allOf(Sports.class)) {
                intSportsList.put(gender.intValue(), gender);
                enSportsList.put(gender.value(), gender);
                zhSportsList.put(gender.zhValue(), gender);
            }
        }

        /**
         * 定義給外界取 Gender 的函式
         *
         * @param _value 定義給項的數值
         * @return 該項目鍵值
         */
        public static Sports getSport(int _value) {
            return intSportsList.get(_value);
        }
        public static Sports getSport(String _value){
            Sports sport = enSportsList.get(_value);
            if (sport == null){
                return zhSportsList.get(_value);
            }
            else{
                return sport;
            }
        }
    }

    public static ArrayList<String> sportsList = new ArrayList<>();
    private static int language = 1; // 0: english, 1:chinese;

    static {
        if (language == 0) {
            for (Sports gender : EnumSet.allOf(Sports.class)) {
                sportsList.add(gender.value());
            }
        }
        else {
            for (Sports gender : EnumSet.allOf(Sports.class)) {
                sportsList.add(gender.zhValue());
            }
        }
    }
}
