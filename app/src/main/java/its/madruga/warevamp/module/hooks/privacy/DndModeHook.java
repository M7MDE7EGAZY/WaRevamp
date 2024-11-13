package its.madruga.warevamp.module.hooks.privacy;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import its.madruga.warevamp.BuildConfig;
import its.madruga.warevamp.module.hooks.core.HooksBase;
import org.jetbrains.annotations.NotNull;

import static its.madruga.warevamp.module.hooks.core.HooksLoader.mApp;
import static its.madruga.warevamp.module.references.References.dndModeMethod;

public class DndModeHook extends HooksBase {
    private static SharedPreferences prefs;

    public DndModeHook(@NonNull @NotNull ClassLoader loader, @NonNull @NotNull XSharedPreferences preferences) {
        super(loader, preferences);
        prefs = mApp.getSharedPreferences(BuildConfig.APPLICATION_ID + "_preferences", Context.MODE_PRIVATE);
    }

    @Override
    public void doHook() throws Exception {
        super.doHook();
        boolean dndMode = prefs.getBoolean("dnd_Mode", false);

        if(!dndMode) return;

        XposedBridge.hookMethod(dndModeMethod(loader), XC_MethodReplacement.DO_NOTHING);
    }
}
