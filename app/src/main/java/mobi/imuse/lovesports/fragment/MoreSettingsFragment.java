package mobi.imuse.lovesports.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.imuse.lovesports.R;

public class MoreSettingsFragment extends BackHandledFragment {
    private static final String TAG = MoreSettingsFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    public static MoreSettingsFragment newInstance(String param1, String param2) {
        MoreSettingsFragment fragment = new MoreSettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MoreSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        return false;
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
        View view = inflater.inflate(R.layout.fragment_more_settings, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.tvChangePasswd)
    public void onChangePasswdClick(){

    }

    @OnClick(R.id.tvUpdateInfo)
    public void onUpdateInfoClick(){

    }

    @OnClick(R.id.tvConsultationWelcome)
    public void onConsultationWelcomeClick(){

    }

    @OnClick(R.id.tvConsultationCompleted)
    public void onConsultationCompletedClick(){

    }

    @OnClick(R.id.tvInviteFriend)
    public void onInviteFriendClick(){

    }

    @OnClick(R.id.tvFeedback)
    public void onFeedbackClick(){

    }

    @OnClick(R.id.tvRateApp)
    public void onRateAppClick(){

    }

    @OnClick(R.id.tvAbout)
    public void onAboutClick(){

    }

    @OnClick(R.id.btnLogout)
    public void onBtnLogoutClick(){

    }
}
