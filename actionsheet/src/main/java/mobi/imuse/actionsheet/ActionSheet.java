package mobi.imuse.actionsheet;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

/**
 * Created by suyanlu on 15/7/23.
 */
public class ActionSheet{
    private Context mContext;
    private LinearLayout mPanel;
    private Attributes mAttrs;
    private static final int CANCEL_BUTTON_ID = 100;
    private String mCancelButtonTitle;
    private String[] mOtherButtonTitles;
    private ActionSheetListener mListener;

    private static class Attributes {
        private Context mContext;

        public Attributes(Context context) {
            mContext = context;
            this.background = new ColorDrawable(Color.TRANSPARENT);
            this.cancelButtonBackground = new ColorDrawable(Color.BLACK);
            ColorDrawable gray = new ColorDrawable(Color.GRAY);
            this.otherButtonTopBackground = gray;
            this.otherButtonMiddleBackground = gray;
            this.otherButtonBottomBackground = gray;
            this.otherButtonSingleBackground = gray;
            this.cancelButtonTextColor = Color.WHITE;
            this.otherButtonTextColor = Color.BLACK;
            this.padding = 20;
            this.otherButtonSpacing = 2;
            this.cancelButtonMarginTop = 10;
            this.actionSheetTextSize = 16;
        }

        public Drawable getOtherButtonMiddleBackground() {
            if (otherButtonMiddleBackground instanceof StateListDrawable) {
                TypedArray a = mContext.getTheme().obtainStyledAttributes(null, R.styleable.ActionSheet, R.attr.actionSheetStyle, 0);
                otherButtonMiddleBackground = a.getDrawable(R.styleable.ActionSheet_otherButtonMiddleBackground);
                a.recycle();
            }
            return otherButtonMiddleBackground;
        }

        Drawable background;
        Drawable cancelButtonBackground;
        Drawable otherButtonTopBackground;
        Drawable otherButtonMiddleBackground;
        Drawable otherButtonBottomBackground;
        Drawable otherButtonSingleBackground;
        int cancelButtonTextColor;
        int otherButtonTextColor;
        int padding;
        int otherButtonSpacing;
        int cancelButtonMarginTop;
        float actionSheetTextSize;
    }

    public ActionSheet(Context context) {
        mContext = context;
    }

    private Attributes readAttribute() {
        Attributes attrs = new Attributes(mContext);
        TypedArray a = mContext.getTheme().obtainStyledAttributes(null, R.styleable.ActionSheet, R.attr.actionSheetStyle, R.style.ActionSheetStyleIOS7);
        Drawable background = a.getDrawable(R.styleable.ActionSheet_actionSheetBackground);
        if (background != null) {
            attrs.background = background;
        }
        Drawable cancelButtonBackground = a.getDrawable(R.styleable.ActionSheet_cancelButtonBackground);
        if (cancelButtonBackground != null) {
            attrs.cancelButtonBackground = cancelButtonBackground;
        }
        Drawable otherButtonTopBackground = a.getDrawable(R.styleable.ActionSheet_otherButtonTopBackground);
        if (otherButtonTopBackground != null) {
            attrs.otherButtonTopBackground = otherButtonTopBackground;
        }
        Drawable otherButtonMiddleBackground = a.getDrawable(R.styleable.ActionSheet_otherButtonMiddleBackground);
        if (otherButtonMiddleBackground != null) {
            attrs.otherButtonMiddleBackground = otherButtonMiddleBackground;
        }
        Drawable otherButtonBottomBackground = a.getDrawable(R.styleable.ActionSheet_otherButtonBottomBackground);
        if (otherButtonBottomBackground != null) {
            attrs.otherButtonBottomBackground = otherButtonBottomBackground;
        }
        Drawable otherButtonSingleBackground = a.getDrawable(R.styleable.ActionSheet_otherButtonSingleBackground);
        if (otherButtonSingleBackground != null) {
            attrs.otherButtonSingleBackground = otherButtonSingleBackground;
        }
        attrs.cancelButtonTextColor = a.getColor(R.styleable.ActionSheet_cancelButtonTextColor, attrs.cancelButtonTextColor);
        attrs.otherButtonTextColor = a.getColor(R.styleable.ActionSheet_otherButtonTextColor, attrs.otherButtonTextColor);
        attrs.padding = (int) a.getDimension(R.styleable.ActionSheet_actionSheetPadding, attrs.padding);
        attrs.otherButtonSpacing = (int) a.getDimension(R.styleable.ActionSheet_otherButtonSpacing, attrs.otherButtonSpacing);
        attrs.cancelButtonMarginTop = (int) a.getDimension(R.styleable.ActionSheet_cancelButtonMarginTop, attrs.cancelButtonMarginTop);
        attrs.actionSheetTextSize = a.getDimension(R.styleable.ActionSheet_actionSheetTextSize, attrs.actionSheetTextSize);

        a.recycle();
        return attrs;
    }

    public void show(){
        Holder holder = new ViewHolder(createView());
        View footer = new View(mContext);
        // 只有在4.4以上版本，采用这个View来填充下面导航栏的位置，避免错位.
        footer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, NavigationBarUtil.getNavigationBarHeight(mContext)));
        DialogPlus.newDialog(mContext)
                .setContentHolder(holder)
                .setBackgroundColorResourceId(android.R.color.transparent)
                .setGravity(Gravity.BOTTOM)
                .setOnClickListener(clickListener)
                .setExpanded(false)
                .setCancelable(true)
                .setFooter(footer)
                .create()
                .show();
    }

    OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(DialogPlus dialog, View view) {
            int viewId = view.getId();
            if (mListener != null){
                if (viewId == CANCEL_BUTTON_ID){
                    mListener.onCancel(ActionSheet.this);
                }
                else if (viewId >= CANCEL_BUTTON_ID+1 && viewId < CANCEL_BUTTON_ID+1+mOtherButtonTitles.length){
                    mListener.onOtherButtonClick(ActionSheet.this, viewId-CANCEL_BUTTON_ID-1);
                }
                else{

                }
            }
            dialog.dismiss();
        }
    };

    private View createView() {
        mAttrs = readAttribute();
        FrameLayout parent = new FrameLayout(mContext);
        parent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  ViewGroup.LayoutParams.WRAP_CONTENT));
        parent.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
        mPanel = new LinearLayout(mContext);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        mPanel.setLayoutParams(params);
        mPanel.setOrientation(LinearLayout.VERTICAL);
        parent.addView(mPanel);
        createItems();
        return parent;
    }

    @SuppressWarnings("deprecation")
    private void createItems() {
        String[] titles = mOtherButtonTitles;
        if (titles != null) {
            for (int i = 0; i < titles.length; i++) {
                Button bt = new Button(mContext);
                bt.setId(CANCEL_BUTTON_ID + i + 1);
                bt.setBackgroundDrawable(getOtherButtonBg(titles, i));
                bt.setText(titles[i]);
                bt.setTextColor(mAttrs.otherButtonTextColor);
                bt.setTextSize(mAttrs.actionSheetTextSize);
                if (i > 0) {
                    LinearLayout.LayoutParams params = createButtonLayoutParams();
                    params.topMargin = mAttrs.otherButtonSpacing;
                    mPanel.addView(bt, params);
                } else {
                    mPanel.addView(bt);
                }
            }
        }
        Button bt = new Button(mContext);
        bt.getPaint().setFakeBoldText(true);
        bt.setTextSize(mAttrs.actionSheetTextSize);
        bt.setId(CANCEL_BUTTON_ID);
        bt.setBackgroundDrawable(mAttrs.cancelButtonBackground);
        if (mCancelButtonTitle == null){
            mCancelButtonTitle = mContext.getResources().getString(R.string.cancel_button_title);
        }
        bt.setText(mCancelButtonTitle);
        bt.setTextColor(mAttrs.cancelButtonTextColor);
        LinearLayout.LayoutParams params = createButtonLayoutParams();
        params.topMargin = mAttrs.cancelButtonMarginTop;
        mPanel.addView(bt, params);

        mPanel.setBackgroundDrawable(mAttrs.background);
        mPanel.setPadding(mAttrs.padding, mAttrs.padding, mAttrs.padding, mAttrs.padding);
    }

    public LinearLayout.LayoutParams createButtonLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        return params;
    }

    private Drawable getOtherButtonBg(String[] titles, int i) {
        if (titles.length == 1) {
            return mAttrs.otherButtonSingleBackground;
        }
        if (titles.length == 2) {
            switch (i) {
                case 0:
                    return mAttrs.otherButtonTopBackground;
                case 1:
                    return mAttrs.otherButtonBottomBackground;
            }
        }
        if (titles.length > 2) {
            if (i == 0) {
                return mAttrs.otherButtonTopBackground;
            }
            if (i == (titles.length - 1)) {
                return mAttrs.otherButtonBottomBackground;
            }
            return mAttrs.getOtherButtonMiddleBackground();
        }
        return null;
    }

    public void setActionSheetListener(ActionSheetListener listener) {
        mListener = listener;
    }

    public  void setCancelButtonTitle(String cancelButtonTitle){
        this.mCancelButtonTitle = cancelButtonTitle;
    }


    public void setOtherButtonTitles(String[] otherButtonTitles){
        this.mOtherButtonTitles = otherButtonTitles;
    }

    public static Builder createBuilder(Context context) {
        return new Builder(context);
    }

    public static class Builder {

        private Context mContext;
        private String mCancelButtonTitle;
        private String[] mOtherButtonTitles;
        private ActionSheetListener mListener;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setCancelButtonTitle(String title) {
            mCancelButtonTitle = title;
            return this;
        }

        public Builder setCancelButtonTitle(int strId) {
            return setCancelButtonTitle(mContext.getString(strId));
        }

        public Builder setOtherButtonTitles(String... titles) {
            mOtherButtonTitles = titles;
            return this;
        }

        public Builder setListener(ActionSheetListener listener) {
            this.mListener = listener;
            return this;
        }

        public ActionSheet show() {
            ActionSheet actionSheet = new ActionSheet(mContext);
            actionSheet.setActionSheetListener(mListener);
            actionSheet.setCancelButtonTitle(mCancelButtonTitle);
            actionSheet.setOtherButtonTitles(mOtherButtonTitles);
            actionSheet.show();
            return actionSheet;
        }

    }

    public static interface ActionSheetListener {

        void onDismiss(ActionSheet actionSheet);

        void onCancel(ActionSheet actionSheet);

        void onOtherButtonClick(ActionSheet actionSheet, int index);
    }
}
