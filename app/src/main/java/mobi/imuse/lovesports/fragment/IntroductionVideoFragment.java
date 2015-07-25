package mobi.imuse.lovesports.fragment;


import android.hardware.Camera;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    private void initVideoSize() {
        if (VERSION.SDK_INT >= 11) {
            List<Camera.Size> sizes = CameraHelper.getCameraSupportedVideoSizes(recordingManager.getCameraManager().getCamera());
            // 因为是竖屏,所以这里s的height才是显示的surface的宽度;
            videoSize = getOptimalPreviewSize(sizes, mVideoView.getHeight(), mVideoView.getWidth());
/*
            for (Camera.Size s : sizes) {
                // 因为是竖屏,所以这里s的height才是显示的surface的宽度;
                if (s.height == metric.widthPixels) {
//                    SLog.d(TAG, "video size matched: " + s.height + " x " + s.width);
                    videoSize = s;
                    break;
                }
            }
*/
            recordingManager.setPreviewSize(videoSize);
        }
    }

    private void updateVideoSizes() {
        if (VERSION.SDK_INT >= 11) {
            List<Camera.Size> sizes = CameraHelper.getCameraSupportedVideoSizes(recordingManager.getCameraManager().getCamera());
            // 因为是竖屏,所以这里s的height才是显示的surface的宽度;
            videoSize = getOptimalPreviewSize(sizes, mVideoView.getHeight(), mVideoView.getWidth());
/*
            for (Camera.Size s : sizes) {
                if (s.height == metric.widthPixels) {
//                    SLog.d(TAG, "video size matched: " + s.height + " x " + s.width);
                    videoSize = s;
                    break;
                }
            }
*/
            recordingManager.setPreviewSize(videoSize);
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
