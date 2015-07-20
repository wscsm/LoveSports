package mobi.imuse.lovesports;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoveSportsActivity extends BaseActivity {
    private static final String TAG = LoveSportsActivity.class.getSimpleName();

    @Bind(R.id.toolbar)    Toolbar mToolbar;
    @Bind(R.id.imageSlider) SliderLayout mImageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_sports);
        ButterKnife.bind(this);
        setSystemBar();
        setSupportActionBar(mToolbar);
        initImageSlider();
    }

    private void initImageSlider() {
        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("牛肝菌蘑菇", "http://pic1.sc.chinaz.com/files/pic/pic9/201504/apic10875.jpg");
        url_maps.put("法国香肠", "http://pic1.sc.chinaz.com/files/pic/pic9/201504/apic10863.jpg");
        url_maps.put("绿竹笋", "http://pic.sc.chinaz.com/files/pic/pic9/201504/apic10922.jpg");
        url_maps.put("红葡萄酒", "http://pics.sc.chinaz.com/files/pic/pic9/201505/apic11282.jpg");
        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);
//                    .setOnSliderClickListener(this); // must implements BaseSliderView.OnSliderClickListener

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra",name);

            mImageSlider.addSlider(textSliderView);
        }
        mImageSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
//        mImageSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mImageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mImageSlider.setCustomAnimation(new DescriptionAnimation());
        mImageSlider.setDuration(4000);
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
