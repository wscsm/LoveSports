package mobi.imuse.lovesports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.imuse.avatar.CropperActivity;
import mobi.imuse.lovesports.util.SLog;

public class LoveSportsActivity extends BaseActivity {
    private static final String TAG = LoveSportsActivity.class.getSimpleName();

    @Bind(R.id.toolbar)    Toolbar mToolbar;
    @Bind(R.id.ivClipped)    ImageView mIvClipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_sports);
        ButterKnife.bind(this);
        setSystemBar();
        setSupportActionBar(mToolbar);


    }

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
}
