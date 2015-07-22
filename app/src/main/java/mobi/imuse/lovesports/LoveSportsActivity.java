package mobi.imuse.lovesports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.imuse.lovesports.fragment.BackHandledFragment;
import mobi.imuse.lovesports.fragment.HomeFragment;
import mobi.imuse.lovesports.util.T;
import mobi.imuse.slidingmenu.SlidingMenu;

public class LoveSportsActivity extends BaseActivity implements BackHandledFragment.BackHandlerInterface, HomeFragment.OnHomeFragmentListener {
    private static final String TAG = LoveSportsActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private SlidingMenu menu;

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
            menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
            menu.setMenu(R.layout.slidemenu_left);
            menu.setSecondaryMenu(R.layout.slidemenu_right);
            menu.setSecondaryShadowDrawable(R.drawable.right_menu_shadow);
        }
    }

    @Override
    public void onImageSliderInitilized(View slider) {
        menu.addIgnoredView(slider);
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
        if (this.selectedFragment == backHandledFragment){
            return;
        }

        this.selectedFragment = backHandledFragment;

    }
/*
    @OnClick(R.id.tvButtonLocalPhoto)
    public void onPickPhotoClick() {
        Intent intent = new Intent(this, CropperActivity.class);
        intent.putExtra("PickWay", "PICK");
        startActivityForResult(intent, 10001);
    }

    @OnClick(R.id.tvButtonTakePhoto)
    public void onTakePhotoClick() {
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
                            .into(mIvClipped);
                }
                break;
            default:
                break;
        }
    }
*/
}
