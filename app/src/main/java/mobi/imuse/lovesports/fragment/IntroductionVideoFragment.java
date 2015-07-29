package mobi.imuse.lovesports.fragment;


import android.hardware.Camera;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skd.androidrecording.video.AdaptiveSurfaceView;
import com.skd.androidrecording.video.CameraHelper;
import com.skd.androidrecording.video.VideoRecordingHandler;
import com.skd.androidrecording.video.VideoRecordingManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.imuse.lovesports.Constants;
import mobi.imuse.lovesports.R;
import mobi.imuse.lovesports.util.T;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IntroductionVideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("deprecation")
public class IntroductionVideoFragment extends BackHandledFragment {
    private static final String TAG = IntroductionVideoFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @Bind(R.id.rlSurfaceView)    RelativeLayout mRlSurfaceView;
    @Bind(R.id.controlsLayout)    LinearLayout mControlsLayout;
    @Bind(R.id.videoView)    AdaptiveSurfaceView mVideoView;
    @Bind(R.id.tvRecordingTime)    TextView mTvRecordingTime;
    @Bind(R.id.btnSwitch)    ImageButton mBtnSwitch;
    @Bind(R.id.btnFlash)    Button mBtnFlash;
    @Bind(R.id.btnRecord)    Button mBtnRecord;

    private String mParam1;
    private String mParam2;

    private Camera.Size videoSize = null;
    private VideoRecordingManager recordingManager;
    private String[] flashModes = {"auto", "torch", "off"};

    private VideoRecordingHandler recordingHandler = new VideoRecordingHandler() {
        @Override
        public boolean onPrepareRecording() {
            if (videoSize == null) {
                initVideoSize();
                return true;
            }
            return false;
        }

        @Override
        public Camera.Size getVideoSize() {
            return videoSize;
        }

        @Override
        public int getDisplayRotation() {
            return getActivity().getWindowManager().getDefaultDisplay().getRotation();
        }
    };

    public static IntroductionVideoFragment newInstance(String param1, String param2) {
        IntroductionVideoFragment fragment = new IntroductionVideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public IntroductionVideoFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_introduction_video, container, false);
        ButterKnife.bind(this, view);
        recordingManager = new VideoRecordingManager(mVideoView, recordingHandler);
        ViewTreeObserver vto = mVideoView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mVideoView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = mRlSurfaceView.getWidth();
                int height = mRlSurfaceView.getHeight() - width*3/4 - getActionBarSize();
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)mControlsLayout.getLayoutParams();
                lp.width = width;lp.height = height;
                mControlsLayout.setLayoutParams(lp);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        recordingManager.dispose();
        recordingHandler = null;

        super.onDestroyView();

        ButterKnife.unbind(this);
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double)h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetWidth = w;

        //  优先640x480;
        for (Camera.Size size : sizes){
            if (size.width == 640 && size.height == 480){
                return size;
            }
        }

        // 再选择是否有等宽高的;
        for (Camera.Size size : sizes){
            if (size.width == size.height && size.width == w){
                return size;
            }
        }
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetWidth) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetWidth);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetWidth) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetWidth);
                }
            }
        }

        if (optimalSize != null && h >= optimalSize.height*optimalSize.width/w){
            return optimalSize;
        }
        else {
            Camera camera = recordingManager.getCameraManager().getCamera();
            return camera.new Size(640, 480);
        }
    }

    private void initVideoSize() {
        if (VERSION.SDK_INT >= 11) {
            List<Camera.Size> sizes = CameraHelper.getCameraSupportedVideoSizes(recordingManager.getCameraManager().getCamera());
            // 因为是竖屏,所以这里s的height才是显示的surface的宽度;
            videoSize = getOptimalPreviewSize(sizes, mRlSurfaceView.getHeight(), mRlSurfaceView.getWidth());

            Camera camera = recordingManager.getCameraManager().getCamera();
            int screenW = mRlSurfaceView.getWidth();
            Camera.Size sizePreview;
            sizePreview = camera.new Size(screenW, screenW * videoSize.width / videoSize.height);
            recordingManager.setPreviewSize(sizePreview);
        }
    }

    private void updateVideoSizes() {
        if (VERSION.SDK_INT >= 11) {
            List<Camera.Size> sizes = CameraHelper.getCameraSupportedVideoSizes(recordingManager.getCameraManager().getCamera());
            // 因为是竖屏,所以这里s的height才是显示的surface的宽度;
            videoSize = getOptimalPreviewSize(sizes, mRlSurfaceView.getHeight(), mRlSurfaceView.getWidth());

            Camera camera = recordingManager.getCameraManager().getCamera();
            int screenW = mRlSurfaceView.getWidth();
            Camera.Size sizePreview;
            sizePreview = camera.new Size(screenW, screenW * videoSize.width / videoSize.height);
            recordingManager.setPreviewSize(sizePreview);
        }
    }

    @OnClick(R.id.btnRecord)
    public void onBtnRecordClick() {
        if (recordingManager.stopRecording()) {
            mBtnRecord.setText("Start Record");
            mBtnSwitch.setEnabled(true);
        }
        else {
            startRecording();
        }
    }

    private void startRecording() {
        long now = System.currentTimeMillis();
        String videoId = String.format("%d.%03d", now / 1000, now % 1000);
        String fileName = videoId + ".mp4";
        if (recordingManager.startRecording(Constants.BasePhotoUrlDiskCached + "/" + fileName, videoSize)) {
            mBtnRecord.setText("Stop");
            mBtnSwitch.setEnabled(false);
            return;
        }
        T.showLong(getActivity(), "Recording video failed");
    }

    @OnClick(R.id.btnSwitch)
    public void onBtnSwitchClick() {
        recordingManager.getCameraManager().switchCamera();
        if (recordingManager.getCameraManager().getSupportedFlashModes()==null){
            mBtnFlash.setEnabled(false);
        }
        else{
            mBtnFlash.setEnabled(true);
            mBtnFlash.setText(flashModes[0]);
            recordingManager.getCameraManager().setFlash(flashModes[0]);
        }
        updateVideoSizes();
    }

    @OnClick(R.id.btnFlash)
    public void onBtnFlashClick(){
        String currentMode = mBtnFlash.getText().toString().toLowerCase();
        int length = flashModes.length;
        String nextMode = flashModes[0];
        for (int i=0; i<length; i++){
            if (currentMode.equals(flashModes[i].toLowerCase())){
                nextMode = flashModes[(i+1)%length];
                break;
            }
        }
        mBtnFlash.setText(nextMode);
        recordingManager.getCameraManager().setFlash(nextMode);
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

}
