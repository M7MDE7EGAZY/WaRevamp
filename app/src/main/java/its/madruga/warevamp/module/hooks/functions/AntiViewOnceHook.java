package its.madruga.warevamp.module.hooks.functions;

import androidx.annotation.NonNull;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import its.madruga.warevamp.module.hooks.core.HooksBase;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static its.madruga.warevamp.module.references.References.viewOnceMethods;

public class AntiViewOnceHook extends HooksBase {
    public AntiViewOnceHook(@NonNull @NotNull ClassLoader loader, @NonNull @NotNull XSharedPreferences preferences) {
        super(loader, preferences);
    }

    @Override
    public void doHook() throws Exception {
        super.doHook();

        boolean viewOnce = prefs.getBoolean("viewOnce", false);
        if (!viewOnce) return;

        for(Method m : viewOnceMethods(loader)) {
            if (Modifier.isAbstract(m.getModifiers())) continue;
            XposedBridge.hookMethod(m, XC_MethodReplacement.DO_NOTHING);
        }
    }
}
