package its.madruga.warevamp.module.hooks.others;

import androidx.annotation.NonNull;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import its.madruga.warevamp.module.hooks.core.HooksBase;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import static its.madruga.warevamp.module.references.References.expirationTimeClass;
import static its.madruga.warevamp.module.references.References.propsMethod;
import static its.madruga.warevamp.module.references.ReferencesUtils.findMethodUsingFilter;

import android.util.Log;

public class OthersHook extends HooksBase {
    HashMap<Integer, Boolean> propList;

    public OthersHook(@NonNull @NotNull ClassLoader loader, @NonNull @NotNull XSharedPreferences preferences) {
        super(loader, preferences);
    }

    @Override
    public void doHook() throws Exception {
        super.doHook();



        propList = new HashMap<>();

        propList.put(4524, prefs.getBoolean("novoTema", false));

//        // Transcrição
//        propList.put(8632, true);
//        propList.put(2890, true);
//        propList.put(9215, true);

        // Novo menu
        propList.put(2889, prefs.getBoolean("novoMenu", false));

        hookProps();
        hookExpirationTime();
    }

    private void hookProps() throws Exception {
        XposedBridge.hookMethod(propsMethod(loader), new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                int i = (int) param.args[param.args.length - 1];

                if (propList.containsKey(i)) {
                    param.setResult(propList.get(i));
                }
            }
        });
    }

    private void hookExpirationTime() throws Exception {
        Class<?> expirationClass = expirationTimeClass(loader);
        XposedBridge.hookAllConstructors(expirationClass, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Method method = findMethodUsingFilter(param.thisObject.getClass(), m -> m.getReturnType().equals(Date.class));
                XposedBridge.hookMethod(method, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(new Date(61728058798000L));
                    }
                });
            }
        });
    }
}
