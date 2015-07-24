package mobi.imuse.lovesports.model;

import android.hardware.Camera;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by suyanlu on 15/7/24.
 */
@SuppressWarnings("deprecation")
public class SizeAdapter extends BaseAdapter {

    private List<Camera.Size> sizes;

    public SizeAdapter(List<Camera.Size> sizes) {
        this.sizes = sizes;
    }

    public void set(List<Camera.Size> sizes) {
        this.sizes.clear();
        this.sizes = sizes;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return sizes != null ? sizes.size() : 0;
    }

    @Override
    public Camera.Size getItem(int position) {
        return sizes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(parent.getContext());
        view.setText(String.format("%s: %sx%s", "Video", sizes.get(position).width, sizes.get(position).height));
        view.setEllipsize(TextUtils.TruncateAt.END);
        view.setPadding(16, 16, 16, 16);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(parent.getContext());
        view.setText(String.format("%s: %sx%s", "Video", sizes.get(position).width, sizes.get(position).height));
        view.setPadding(16, 16, 16, 16);
        return view;
    }

}
