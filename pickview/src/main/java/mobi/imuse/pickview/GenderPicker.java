package mobi.imuse.pickview;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

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

import mobi.imuse.pickview.lib.ScreenInfo;
import mobi.imuse.pickview.lib.WheelOptions;

/**
 * 选项选择器，可支持一二三级联动选择
 *
 * @author Sai
 */
public class GenderPicker {

    Context mContext;
    private DialogPlus dialog;

    private WheelOptions wheelOptions;
    private OnGenderSelectListener genderSelectListener;

    private static GenderPicker instance;

    private GenderPicker(Context context) {
        mContext = context;
    }

    public static GenderPicker instance(Context context) {
        if (instance == null) {
            instance = new GenderPicker(context);
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
        // ----转轮
        final View optionspicker = dialog.getHolderView().findViewById(R.id.optionspicker);
        ScreenInfo screenInfo = new ScreenInfo((Activity) mContext);
        wheelOptions = new WheelOptions(optionspicker);

        wheelOptions.screenheight = screenInfo.getHeight();

    }

    public void show(String currentGender) {
        this.setPicker(genderList)
                .setGender(currentGender == null ? "不设置" : currentGender)
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
            if (viewId == R.id.btnSubmit && genderSelectListener != null) {
                int[] optionsCurrentItems = wheelOptions.getCurrentItems();
                genderSelectListener.onGenderSelected(optionsCurrentItems[0]);
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

    private GenderPicker setPicker(ArrayList<String> optionsItems) {
        wheelOptions.setPicker(optionsItems, null, null, false);
        return this;
    }

    private GenderPicker setGender(String gender) {
        Gender g = Gender.getGender(gender);
        if (g == null) g = Gender.UnSet;
        wheelOptions.setCurrentItems(g.intValue(), 0, 0);
        return this;
    }

    private GenderPicker setCyclic(boolean cyclic) {
        wheelOptions.setCyclic(cyclic);
        return this;
    }

    public interface OnGenderSelectListener {
        public void onGenderSelected(int selected);
    }

    public GenderPicker setGenderSelectListener(OnGenderSelectListener genderSelectListener) {
        this.genderSelectListener = genderSelectListener;
        return this;
    }

    public enum Gender {
        //通过括号赋值,而且必须带有一个参构造器和一个属性跟方法，否则编译出错
        //赋值必须都赋值或都不赋值，不能一部分赋值一部分不赋值；如果不赋值则不能写构造器，赋值编译也出错
        UnSet (0, "UnSet",  "不设置"),
        Male  (1, "Male",   "男"),
        Female(2, "Female", "女");

        private int intValue;
        private String value;
        private String zhValue;

        //构造器默认也只能是private, 从而保证构造函数只能在内部使用
        Gender(int intValue, String value, String zhValue) {
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
        private static Map<Integer, Gender> intGenderList = new HashMap<>();
        private static Map<String, Gender> enGenderList = new HashMap<>();
        private static Map<String, Gender> zhGenderList = new HashMap<>();
        //使用 for-loop 為每個項目代入其定義的數值
        static {
            for (Gender gender : EnumSet.allOf(Gender.class)) {
                intGenderList.put(gender.intValue(), gender);
                enGenderList.put(gender.value(), gender);
                zhGenderList.put(gender.zhValue(), gender);
            }
        }

        /**
         * 定義給外界取 Gender 的函式
         *
         * @param _value 定義給項的數值
         * @return 該項目鍵值
         */
        public static Gender getGender(int _value) {
            return intGenderList.get(_value);
        }
        public static Gender getGender(String _value){
            Gender gender = enGenderList.get(_value);
            if (gender == null){
                return zhGenderList.get(_value);
            }
            else{
                return gender;
            }
        }
    }

    public static ArrayList<String> genderList = new ArrayList<>();
    private static int language = 1; // 0: english, 1:chinese;

    static {
        if (language == 0) {
            for (Gender gender : EnumSet.allOf(Gender.class)) {
                genderList.add(gender.value());
            }
        }
        else {
            for (Gender gender : EnumSet.allOf(Gender.class)) {
                genderList.add(gender.zhValue());
            }
        }
    }
}
