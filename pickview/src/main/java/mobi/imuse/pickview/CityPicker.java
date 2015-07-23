package mobi.imuse.pickview;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mobi.imuse.pickview.city.CityCodeUtil;
import mobi.imuse.pickview.city.CityInfo;
import mobi.imuse.pickview.city.FileUtil;
import mobi.imuse.pickview.lib.ScreenInfo;
import mobi.imuse.pickview.lib.WheelOptions;

/**
 * 选项选择器，可支持一二三级联动选择
 *
 * @author Sai
 */
public class CityPicker {

    Context mContext;
    private DialogPlus dialog;

    private WheelOptions wheelOptions;
    private OnCitySelectListener mListener;

    private static CityPicker instance;

    private CityPicker(Context context) {
        mContext = context;
        getaddressinfo();
    }

    public static CityPicker instance(Context context) {
        if (instance == null) {
            instance = new CityPicker(context);
        }
        instance.init();
        return instance;
    }

    private void init() {
        cityCodeUtil = CityCodeUtil.getSingleton();

        if (dialog != null && dialog.isShowing()) {
            return;
        }

        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        Holder holder = new ViewHolder(R.layout.pw_options);
        View footer = new View(mContext);
        footer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getNavigationBarHeight()));
        dialog = DialogPlus.newDialog(mContext)
                .setContentHolder(holder)
                .setGravity(Gravity.BOTTOM)
                .setOnDismissListener(dismissListener)
                .setOnCancelListener(cancelListener)
                .setOnClickListener(clickListener)
                .setExpanded(false)
                .setCancelable(true)
                .setFooter(footer)
                .create();
        // ----转轮
        final View optionspicker = dialog.getHolderView().findViewById(R.id.optionspicker);
        ScreenInfo screenInfo = new ScreenInfo((Activity) mContext);
        wheelOptions = new WheelOptions(optionspicker);

        wheelOptions.screenheight = screenInfo.getHeight();

    }

    public void show(String currentProvince, String currentCity) {
        this.setPicker(stringListProvinces, stringListCityOfProvinces)
                .setProvinceCity(currentProvince, currentCity)
                .setCyclic(false);
        dialog.show();
    }

    OnDismissListener dismissListener = new OnDismissListener() {
        @Override
        public void onDismiss(DialogPlus dialog) {
        }
    };

    OnCancelListener cancelListener = new OnCancelListener() {
        @Override
        public void onCancel(DialogPlus dialog) {
        }
    };

    OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(DialogPlus dialog, View view) {
            int viewId = view.getId();
            if (viewId == R.id.btnSubmit && mListener != null) {
                int[] optionsCurrentItems = wheelOptions.getCurrentItems();
                int idx1 = optionsCurrentItems[0];
                int idx2 = optionsCurrentItems[1];
                mListener.onCitySelected(stringListProvinces.get(idx1), stringListCityOfProvinces.get(idx1).get(idx2));
            }
            dialog.dismiss();
        }
    };

    private int getNavigationBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Resources resources = mContext.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return resources.getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    private CityPicker setPicker(ArrayList<String> province, ArrayList<ArrayList<String>> city) {
        wheelOptions.setPicker(province, city, null, true);
        return this;
    }

    private CityPicker setProvinceCity(String province, String city) {
        // TODO: 获取省份和城市的索引后设置进去;
        int idxProvince = -1;
        int idxCity = -1;
        if (province != null && city != null) {
            for (String strProvince : stringListProvinces) {
                if (strProvince.equals(province)) {
                    idxProvince = stringListCityOfProvinces.indexOf(strProvince);
                    break;
                }
            }
            if (idxProvince == -1) {
                idxProvince = 0;
                idxCity = 0;
            }
            else {
                ArrayList<String> strListCity = stringListCityOfProvinces.get(idxProvince);
                for (String strCity : strListCity) {
                    if (strCity.equals(city)) {
                        idxCity = stringListCityOfProvinces.indexOf(strCity);
                        break;
                    }
                }
            }
            if (idxCity == -1) {
                idxCity = 0;
            }
        }
        else{
            idxProvince = 0;
            idxCity = 0;
        }
        wheelOptions.setCurrentItems(idxProvince, idxCity, 0);
        return this;
    }

    private CityPicker setCyclic(boolean cyclic) {
        wheelOptions.setCyclic(cyclic);
        return this;
    }

    public interface OnCitySelectListener {
        public void onCitySelected(String province, String city);
    }

    public CityPicker setCitySelectListener(OnCitySelectListener citySelectListener) {
        this.mListener = citySelectListener;
        return this;
    }

    // begin: added by suyanlu;
    private List<CityInfo> province_list = new ArrayList<>();
    private HashMap<String, List<CityInfo>> city_map = new HashMap<>();
    private HashMap<String, List<CityInfo>> couny_map = new HashMap<>();
    private static ArrayList<String> province_list_code = new ArrayList<>();
    private static ArrayList<String> city_list_code = new ArrayList<>();
    private static ArrayList<String> couny_list_code = new ArrayList<>();

    private CityCodeUtil cityCodeUtil;
    // 省市乡三级联动数组;
    private static ArrayList<String> stringListProvinces = new ArrayList<>();
    private static ArrayList<ArrayList<String>> stringListCityOfProvinces = new ArrayList<>();

    // end: added by suyanlu;

    // 获取城市信息
    private void getaddressinfo() {
        // 读取城市信息string
        JSONParser parser = new JSONParser();
        String area_str = FileUtil.readAssets(mContext, "area.json");
        province_list = parser.getJSONParserResult(area_str, "area0");
        city_map = parser.getJSONParserResultArray(area_str, "area1");
        couny_map = parser.getJSONParserResultArray(area_str, "area2");

        // begin: added by suyanlu;
        // 获取省份和对应的城市名称列表;
        for (CityInfo province : province_list) {
            stringListProvinces.add(province.getCity_name());
            ArrayList<String> cityOfProvince = new ArrayList<>();
            List<CityInfo> citys = city_map.get(province.getId());
            for (CityInfo city : citys) {
                cityOfProvince.add(city.getCity_name());
            }
            stringListCityOfProvinces.add(cityOfProvince);
        }
        // end: added by suyanlu;
    }

    @SuppressWarnings("unchecked")
    public static class JSONParser {
        public ArrayList<String> province_list_code = new ArrayList<>();
        public ArrayList<String> city_list_code = new ArrayList<>();

        public List<CityInfo> getJSONParserResult(String JSONString, String key) {
            List<CityInfo> list = new ArrayList<>();
            JsonObject result = new JsonParser().parse(JSONString).getAsJsonObject().getAsJsonObject(key);

            Iterator iterator = result.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) iterator.next();
                CityInfo cityinfo = new CityInfo();

                cityinfo.setCity_name(entry.getValue().getAsString());
                cityinfo.setId(entry.getKey());
                province_list_code.add(entry.getKey());
                list.add(cityinfo);
            }
            System.out.println(province_list_code.size());
            return list;
        }

        public HashMap<String, List<CityInfo>> getJSONParserResultArray(String JSONString, String key) {
            HashMap<String, List<CityInfo>> hashMap = new HashMap<>();
            JsonObject result = new JsonParser().parse(JSONString).getAsJsonObject().getAsJsonObject(key);

            Iterator iterator = result.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) iterator.next();
                List<CityInfo> list = new ArrayList<>();
                JsonArray array = entry.getValue().getAsJsonArray();
                for (int i = 0; i < array.size(); i++) {
                    CityInfo cityinfo = new CityInfo();
                    cityinfo.setCity_name(array.get(i).getAsJsonArray().get(0).getAsString());
                    cityinfo.setId(array.get(i).getAsJsonArray().get(1).getAsString());
                    city_list_code.add(array.get(i).getAsJsonArray().get(1).getAsString());
                    list.add(cityinfo);
                }
                hashMap.put(entry.getKey(), list);
            }
            return hashMap;
        }
    }

}
