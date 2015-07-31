package mobi.imuse.lovesports.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.andreabaccega.widget.FormEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.imuse.lovesports.R;

public class ServicePriceSettingFragment extends BackHandledFragment {
    private static final String TAG = ServicePriceSettingFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.editPriceOnlineConsultation)
    FormEditText mEditPriceOnlineConsultation;
    @Bind(R.id.editSpecialOffer)
    FormEditText mEditSpecialOffer;
    @Bind(R.id.editQuotaSpecialOffer)
    FormEditText mEditQuotaSpecialOffer;
    @Bind(R.id.cbProvideSpecialOffer)
    CheckBox mCbProvideSpecialOffer;

    private String mParam1;
    private String mParam2;


    public static ServicePriceSettingFragment newInstance(String param1, String param2) {
        ServicePriceSettingFragment fragment = new ServicePriceSettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ServicePriceSettingFragment() {
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
        View view = inflater.inflate(R.layout.fragment_price_setting, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.rlProvideSpecialOffer)
    public void onRlProvideSpecialOfferClick() {
        boolean bChecked = mCbProvideSpecialOffer.isChecked();
        mCbProvideSpecialOffer.setChecked(!bChecked);
    }

    @OnClick(R.id.btnSave)
    public void onSaveClick(){

    }
}
