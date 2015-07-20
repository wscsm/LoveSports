package mobi.imuse.lovesports;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mobi.imuse.lovesports.util.SystemBarTintManager;

public class BaseActivity extends AppCompatActivity {
    private int key = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setSystemBar() {
        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.primary_dark);
            tintManager.setNavigationBarTintResource(R.color.primary_dark);
        }
    }

}
