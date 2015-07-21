package mobi.imuse.lovesports.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.imuse.lovesports.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    @Bind(R.id.imageSlider) SliderLayout mImageSlider;
    @Bind(R.id.swipeRefreshLayout)    PullRefreshLayout mSwipeRefreshLayout;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnHomeFragmentListener mListener;


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        initImageSlider();

        mSwipeRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        mSwipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        mListener.onImageSliderInitilized(mImageSlider);
        return rootView;
    }

    private void initImageSlider() {
        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("网球", "http://img.taopic.com/uploads/allimg/130102/240404-13010222592682.jpg");
        url_maps.put("高尔夫球", "http://img.taopic.com/uploads/allimg/130102/240404-13010223060638.jpg");
        url_maps.put("保龄球", "http://pic1.nipic.com/2008-10-14/20081014133644627_2.jpg");
        url_maps.put("射击", "http://image2.sina.com.cn/ty/o/2002-07-31/3_6-12-44-13_20020731214725.jpg");
        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getActivity());
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
        mImageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mImageSlider.setCustomAnimation(new DescriptionAnimation());
        mImageSlider.setDuration(3000);
    }

    public interface OnHomeFragmentListener{
        public void onImageSliderInitilized(View slider);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnHomeFragmentListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHomeFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
