package mobi.imuse.lovesports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.imuse.lovesports.fragment.BackHandledFragment;
import mobi.imuse.lovesports.fragment.HomeFragment;
import mobi.imuse.lovesports.fragment.IntroductionVideoFragment;
import mobi.imuse.lovesports.fragment.ServicePriceSettingFragment;
import mobi.imuse.lovesports.util.T;
import mobi.imuse.slidingmenu.SlidingMenu;

public class LoveSportsActivity extends BaseActivity implements BackHandledFragment.BackHandlerInterface, HomeFragment.OnHomeFragmentListener {
    private static final String TAG = LoveSportsActivity.class.getSimpleName();

    @Bind(R.id.toolbar)    Toolbar mToolbar;
    @Bind(R.id.tvTitleName)    TextView mTvTitleName;
    @Bind(R.id.tvBtnLeft)    TextView mTvBtnLeft;
    @Bind(R.id.tvBtnRight)    TextView mTvBtnRight;
    @Bind(R.id.tvTextBtnRight)    TextView mTvTextBtnRight;

    private SlidingMenu menu;
    private SliderLayout mImageSlider;

    private BackHandledFragment selectedFragment;
    private static long back_pressed; //按两次back退出;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_sports);
        ButterKnife.bind(this);
        setSystemBar();
        setSupportActionBar(mToolbar);

        initSlidingMenu();

        HomeFragment hf = HomeFragment.newInstance("p1", "p2");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fl_container, hf).commit();
        }

    }

    protected void initSlidingMenu() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        if (menu == null) {
            menu = new SlidingMenu(this);
            menu.setMode(SlidingMenu.LEFT_RIGHT);
            menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            menu.setShadowWidth(15);
            menu.setShadowDrawable(R.drawable.left_menu_shadow);
            menu.setBehindOffset((int) (screenWidth * (1 - 0.618)));
//            menuLeft.setBehindScrollScale(1.0f);
            menu.setFadeDegree(0.35f);
            menu.setBehindScrollScale(0.0f);
            menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
            menu.setMenu(R.layout.slidemenu_left);
            menu.setSecondaryMenu(R.layout.slidemenu_right);
            menu.setSecondaryShadowDrawable(R.drawable.right_menu_shadow);
        }
        ButterKnife.findById(menu, R.id.leftMiHomePage).setOnClickListener(onMenuItemClickListener);
        ButterKnife.findById(menu, R.id.leftMiMyConsultation).setOnClickListener(onMenuItemClickListener);
        ButterKnife.findById(menu, R.id.leftMiPriceSetting).setOnClickListener(onMenuItemClickListener);
        ButterKnife.findById(menu, R.id.leftMiMyPhotoes).setOnClickListener(onMenuItemClickListener);
        ButterKnife.findById(menu, R.id.leftMiIntroductionVideo).setOnClickListener(onMenuItemClickListener);
        ButterKnife.findById(menu, R.id.leftMiTeachingVideo).setOnClickListener(onMenuItemClickListener);
        ButterKnife.findById(menu, R.id.leftMiMoreSettings).setOnClickListener(onMenuItemClickListener);
    }

    @Override
    public void onImageSliderChanged(SliderLayout slider) {
        mImageSlider = slider;
        if (slider != null) {
            menu.addIgnoredView(slider);
        }
    }

    @Override
    public void onBackPressed() {
        if (menu.isMenuShowing()) {
            menu.showContent(true);
            return;
        }

        if (selectedFragment == null || !selectedFragment.onBackPressed()) {
            // Selected fragment did not consume the back press event.
            if (back_pressed + 2000 > System.currentTimeMillis()) {
                // 实现Home键效果
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addCategory(Intent.CATEGORY_HOME);
                startActivity(i);
            }
            else {
                // 沉浸模式下用Activity作为Context会导致Toast文字错位；
                T.showShort(getBaseContext(), "再按一次退出");
            }
            back_pressed = System.currentTimeMillis();
        }
    }

    @OnClick(R.id.tvBtnLeft)
    public void onBtnLeftClick() {
        if (!menu.isMenuShowing()) {
            menu.showMenu(true);
        }
    }

    @OnClick(R.id.tvBtnRight)
    public void onBtnRightClick() {
        if (!menu.isMenuShowing()) {
            menu.showSecondaryMenu(true);
        }
    }

    @Override
    public void setSelectedFragment(BackHandledFragment backHandledFragment) {
        if (this.selectedFragment == backHandledFragment) {
            return;
        }

        this.selectedFragment = backHandledFragment;

    }

    View.OnClickListener onMenuItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.leftMiHomePage:
                    onLeftMenuHomePage();
                    break;
                case R.id.leftMiMyConsultation:
                    T.showShort(getApplicationContext(), "My Consultation");
                    break;
                case R.id.leftMiPriceSetting:
                    onLeftMenuPriceSetting();
                    break;
                case R.id.leftMiMyPhotoes:
                    T.showShort(getApplicationContext(), "My Photoes");
                    break;
                case R.id.leftMiIntroductionVideo:
                    onLeftMenuIntroductionVideo();
                    break;
                case R.id.leftMiTeachingVideo:
                    T.showShort(getApplicationContext(), "Teaching Video");
                    break;
                case R.id.leftMiMoreSettings:
                    T.showShort(getApplicationContext(), "More Settings");
                    break;
                default:
                    break;
            }
        }
    };

    private void onLeftMenuHomePage() {
        if (!(selectedFragment instanceof HomeFragment)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_container, HomeFragment.newInstance("param1", "param2"))
                    .commit();
            mTvBtnLeft.setVisibility(View.VISIBLE);
            mTvBtnRight.setVisibility(View.VISIBLE);
        }
        menu.showContent(true);
    }

    private void onLeftMenuIntroductionVideo() {
        if (!(selectedFragment instanceof IntroductionVideoFragment)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_container, IntroductionVideoFragment.newInstance("param1", "param2"))
                    .commit();
            mTvBtnLeft.setVisibility(View.VISIBLE);
            mTvBtnRight.setVisibility(View.GONE);
        }
        menu.showContent(true);
    }

    private void onLeftMenuPriceSetting() {
        if (!(selectedFragment instanceof ServicePriceSettingFragment)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_container, ServicePriceSettingFragment.newInstance("param1", "param2"))
                    .commit();
            mTvBtnLeft.setVisibility(View.VISIBLE);
            mTvBtnRight.setVisibility(View.GONE);
        }
        menu.showContent(true);
    }
}
