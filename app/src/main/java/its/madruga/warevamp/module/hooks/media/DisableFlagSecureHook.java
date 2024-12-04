package its.madruga.warevamp.module.hooks.media;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class DisableFlagSecureHook {

    public static void doHook(XC_LoadPackage.LoadPackageParam loadPackageParam, XSharedPreferences pref) {
        if (!pref.getBoolean("disable_secure_flags", false)) return;

        XposedHelpers.findAndHookMethod("android.view.Window", loadPackageParam.classLoader, "setFlags", int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                int flags = (int) param.args[0];

                if ((flags & android.view.WindowManager.LayoutParams.FLAG_SECURE) != 0) {
                    flags &= ~android.view.WindowManager.LayoutParams.FLAG_SECURE;
                    param.args[0] = flags;
                }
            }
        });
    }
}