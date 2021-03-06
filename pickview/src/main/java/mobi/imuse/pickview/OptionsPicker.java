package mobi.imuse.pickview;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

import mobi.imuse.pickview.lib.NavigationBarUtil;
import mobi.imuse.pickview.lib.ScreenInfo;
import mobi.imuse.pickview.lib.WheelOptions;

/**
 * 选项选择器，可支持一二三级联动选择
 *
 * @author Sai
 */
public class OptionsPicker {
    Context mContext;
    private DialogPlus dialog;

    private WheelOptions wheelOptions;
    private OnOptionsSelectListener optionsSelectListener;

    private static OptionsPicker instance;

    private OptionsPicker(Context context) {
        mContext = context;
    }

    public static OptionsPicker instance(Context context) {
        if (instance == null) {
            instance = new OptionsPicker(context);
        }
        instance.init();
        return instance;
    }

    public void init() {
        if (dialog != null && dialog.isShowing()) {
            return;
        }

        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        Holder holder = new ViewHolder(R.layout.pw_options);
        // 只有在4.4以上版本，采用这个View来填充下面导航栏的位置，避免错位.
        View footer = new View(mContext);
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
        // ----转轮
        final View optionspicker = dialog.getHolderView().findViewById(R.id.optionspicker);
        ScreenInfo screenInfo = new ScreenInfo((Activity) mContext);
        wheelOptions = new WheelOptions(optionspicker);

        wheelOptions.screenheight = screenInfo.getHeight();

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
            if (viewId == R.id.btnSubmit && optionsSelectListener != null) {
                int[] optionsCurrentItems = wheelOptions.getCurrentItems();
                optionsSelectListener.onOptionsSelect(optionsCurrentItems[0], optionsCurrentItems[1], optionsCurrentItems[2]);
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

    public OptionsPicker setPicker(ArrayList<String> optionsItems) {
        wheelOptions.setPicker(optionsItems, null, null, false);
        return this;
    }

    public OptionsPicker setPicker(ArrayList<String> options1Items, ArrayList<ArrayList<String>> options2Items, boolean linkage) {
        wheelOptions.setPicker(options1Items, options2Items, null, linkage);
        return this;
    }

    public OptionsPicker setPicker(ArrayList<String> options1Items, ArrayList<ArrayList<String>> options2Items,
                                             ArrayList<ArrayList<ArrayList<String>>> options3Items, boolean linkage) {
        wheelOptions.setPicker(options1Items, options2Items, options3Items, linkage);
        return this;
    }

    public OptionsPicker setSelectOptions(int option1) {
        wheelOptions.setCurrentItems(option1, 0, 0);
        return this;
    }

    public OptionsPicker setSelectOptions(int option1, int option2) {
        wheelOptions.setCurrentItems(option1, option2, 0);
        return this;
    }

    public OptionsPicker setSelectOptions(int option1, int option2, int option3) {
        wheelOptions.setCurrentItems(option1, option2, option3);
        return this;
    }

    public OptionsPicker setLabels(String label1) {
        wheelOptions.setLabels(label1, null, null);
        return this;
    }

    public OptionsPicker setLabels(String label1, String label2) {
        wheelOptions.setLabels(label1, label2, null);
        return this;
    }

    public OptionsPicker setLabels(String label1, String label2, String label3) {
        wheelOptions.setLabels(label1, label2, label3);
        return this;
    }

    public OptionsPicker setCyclic(boolean cyclic) {
        wheelOptions.setCyclic(cyclic);
        return this;
    }

    public interface OnOptionsSelectListener {
        public void onOptionsSelect(int options1, int options2, int options3);
    }

    public OptionsPicker setOnoptionsSelectListener(OnOptionsSelectListener optionsSelectListener) {
        this.optionsSelectListener = optionsSelectListener;
        return this;
    }
}
