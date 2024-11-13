package its.madruga.warevamp.module.hooks.core;

import android.util.Log;
import androidx.annotation.NonNull;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;

public class HooksBase {
    public final ClassLoader loader;
    public final XSharedPreferences prefs;

    public HooksBase(@NonNull ClassLoader loader, @NonNull XSharedPreferences preferences) {
        this.loader = loader;
        this.prefs = preferences;
    }

    public void log(@NonNull String msg) {
        XposedBridge.log("[-] Log | " + this.getClass().getSimpleName() +
                " -> " + msg);
    }

    public void wppLog(@NonNull String msg) {
        Log.e("WaRevamp", "[-] Log | " + this.getClass().getSimpleName() +
                " -> " + msg);
    }

    public void doHook() throws Exception {

    }
}
