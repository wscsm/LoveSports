package mobi.imuse.lovesports;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.imuse.lovesports.fragment.HomeFragment;
import mobi.imuse.slidingmenu.SlidingMenu;

public class LoveSportsActivity extends BaseActivity {
    private static final String TAG = LoveSportsActivity.class.getSimpleName();

    @Bind(R.id.toolbar)    Toolbar mToolbar;

    private SlidingMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_sports);
        ButterKnife.bind(this);
        setSystemBar();
        setSupportActionBar(mToolbar);

        initSlidingMenu();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fl_container, HomeFragment.newInstance("p1", "p2")).commit();
        }

    }

    protected void initSlidingMenu(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        if (menu == null){
            menu = new SlidingMenu(this);
            menu.setMode(SlidingMenu.LEFT_RIGHT);
            menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            menu.setShadowWidth(15);
            menu.setShadowDrawable(R.drawable.shadow);
            menu.setBehindOffset((int) (screenWidth * (1-0.618)));
//            menuLeft.setBehindScrollScale(1.0f);
            menu.setFadeDegree(0.35f);
            menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
            menu.setMenu(R.layout.slidemenu_left);
            menu.setSecondaryMenu(R.layout.slidemenu_right);
            menu.setSecondaryShadowDrawable(R.drawable.shadow);
        }
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
