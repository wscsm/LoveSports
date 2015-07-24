package mobi.imuse.lovesports.fragment;


import android.hardware.Camera;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import mobi.imuse.lovesports.model.SizeAdapter;
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

    @Bind(R.id.videoView)
    AdaptiveSurfaceView mVideoView;
    @Bind(R.id.tvRecordingTime)
    TextView mTvRecordingTime;
    @Bind(R.id.btnSwitch)
    ImageButton mBtnSwitch;
    @Bind(R.id.btnRecord)
    Button mBtnRecord;
    @Bind(R.id.videoSizeSpinner)
    Spinner mVideoSizeSpinner;

    private String mParam1;
    private String mParam2;

    private Camera.Size videoSize = null;
    private VideoRecordingManager recordingManager;

    private VideoRecordingHandler recordingHandler = new VideoRecordingHandler() {
        @Override
        public boolean onPrepareRecording() {
            if (videoSize == null) {
                initVideoSizeSpinner();
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
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recordingManager = new VideoRecordingManager(mVideoView, recordingHandler);
    }

    @Override
    public void onStop() {
        recordingManager.dispose();
        recordingHandler = null;

        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
    }

    private void initVideoSizeSpinner() {
//        mVideoSizeSpinner = ButterKnife.findById(getActivity(), R.id.videoSizeSpinner);
        if (VERSION.SDK_INT >= 11) {
            List<Camera.Size> sizes = CameraHelper.getCameraSupportedVideoSizes(recordingManager.getCameraManager().getCamera());
            if (sizes == null) {
                mVideoSizeSpinner.setVisibility(View.GONE);
            }
            else {
                mVideoSizeSpinner.setAdapter(new SizeAdapter(sizes));
                mVideoSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        videoSize = (Camera.Size) arg0.getItemAtPosition(arg2);
                        recordingManager.setPreviewSize(videoSize);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
                videoSize = (Camera.Size) mVideoSizeSpinner.getItemAtPosition(0);
            }
        }
        else {
            mVideoSizeSpinner.setVisibility(View.GONE);
        }
    }

    private void updateVideoSizes() {
        if (VERSION.SDK_INT >= 11) {
            ((SizeAdapter) mVideoSizeSpinner.getAdapter()).set(CameraHelper.getCameraSupportedVideoSizes(recordingManager.getCameraManager().getCamera()));
            mVideoSizeSpinner.setSelection(0);
            videoSize = (Camera.Size) mVideoSizeSpinner.getItemAtPosition(0);
            recordingManager.setPreviewSize(videoSize);
        }
    }

    @OnClick(R.id.btnRecord)
    public void onBtnRecordClick() {
        if (recordingManager.stopRecording()) {
            mBtnRecord.setText("Start Record");
            mBtnSwitch.setEnabled(true);
            mVideoSizeSpinner.setEnabled(true);
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
            mVideoSizeSpinner.setEnabled(false);
            return;
        }
        T.showLong(getActivity(), "Recording video failed");
    }

    @OnClick(R.id.btnSwitch)
    public void onBtnSwitchClick() {
        recordingManager.getCameraManager().switchCamera();
        updateVideoSizes();
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
