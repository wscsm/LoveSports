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
        url_maps.put("网球", "http://img.taopic.com/uploads/allimg/130102/240404-13010222592682.jpg");
        url_maps.put("高尔夫球", "http://img.taopic.com/uploads/allimg/130102/240404-13010223060638.jpg");
        url_maps.put("保龄球", "http://pic1.nipic.com/2008-10-14/20081014133644627_2.jpg");
        url_maps.put("射击", "http://image2.sina.com.cn/ty/o/2002-07-31/3_6-12-44-13_20020731214725.jpg");
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
