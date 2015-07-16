package mobi.imuse.lovesports;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.easemob.chat.EMChat;

import java.io.File;


@SuppressWarnings("unused")
public class LoveSportsApp extends Application {
    @SuppressLint("NewApi")
    @Override
	public void onCreate() {
        if (Constants.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
        }
        super.onCreate();
        // begin: 初始化环信;
        EMChat.getInstance().init(getApplicationContext());

        /**
         * debugMode == true 时为打开，sdk 会在log里输入调试信息
         * @param debugMode
         * 在做代码混淆的时候需要设置成false
         */
        EMChat.getInstance().setDebugMode(true);//在做打包混淆时，要关闭debug模式，如果未被关闭，则会出现程序无法运行问题
        // end: 初始化环信;
        preventCachedMediaScaned();

    }

    private void preventCachedMediaScaned(){
        String dirPath = Constants.BasePhotoUrlDiskCached + "/" + ".nomedia";
        File file = new File(dirPath);
        if (file.exists() && file.isDirectory()){

            return;
        }
        if (file.isFile()){
            file.delete();
        }
        file.mkdir();
    }
	@Override
	public void onLowMemory() {
        super.onLowMemory();
	}

	@Override
	public void onTerminate() {
        super.onTerminate();
	}

}
