package mobi.imuse.actionsheet;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;

/**
 * Created by suyanlu on 15/7/23.
 */
public class NavigationBarUtil {

    public static int getNavigationBarHeight(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return resources.getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

}
