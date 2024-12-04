package its.madruga.warevamp.core;


import static its.madruga.warevamp.module.hooks.core.HooksLoader.mApp;

import android.util.Log;

import its.madruga.warevamp.App;

public class Utils {
    public static String WHATSAPP_PACKAGE = "com.whatsapp";
    public static String WHATSAPP_WEB_PACKAGE = "com.whatsapp.w4b";

    public static boolean isInstalled(String packageWpp) {
        try {
            App.getInstance().getPackageManager().getPackageInfo(packageWpp, 0);
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public static int getResourceId(String name, String type) {
        int id = App.getInstance().getResources().getIdentifier(name, type, App.getInstance().getPackageName());
        if (id == 0) {
            Log.e("WaRevamp", "Resource not found: " + name);
        }
        return id;
    }
}
