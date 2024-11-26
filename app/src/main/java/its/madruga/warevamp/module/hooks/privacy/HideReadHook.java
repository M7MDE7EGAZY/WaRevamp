package its.madruga.warevamp.module.hooks.privacy;

import static its.madruga.warevamp.module.references.References.hideReadJobMethod;
import static its.madruga.warevamp.module.references.References.hideViewInChatMethod;
import static its.madruga.warevamp.module.references.References.hideViewMethod;
import static its.madruga.warevamp.module.references.References.senderPlayedMethod;

import androidx.annotation.NonNull;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import its.madruga.warevamp.module.hooks.core.HooksBase;
import its.madruga.warevamp.module.references.ReferencesUtils;

public class HideReadHook extends HooksBase {
    public HideReadHook(@NonNull ClassLoader loader, @NonNull XSharedPreferences preferences) {
        super(loader, preferences);
    }

    @Override
    public void doHook() throws Exception {
        super.doHook();

        boolean hideread = prefs.getBoolean("hide_read", false);

        if (!hideread) return;

        Class<?> sendJob = XposedHelpers.findClass("com.whatsapp.jobqueue.job.SendReadReceiptJob", loader);

        XposedBridge.hookMethod(hideReadJobMethod(loader), new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (!sendJob.isInstance(param.thisObject)) return;
                param.setResult(null);
            }
        });

        XposedBridge.hookMethod(hideViewMethod(loader), new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (!ReferencesUtils.isCalledFromMethod(hideViewInChatMethod(loader))) return;
                var p4 = param.args[4];
                if (p4 != null && p4.equals("read")) {
                    param.args[4] = null;
                    super.beforeHookedMethod(param);
                }
            }
        });

        XposedBridge.hookMethod(senderPlayedMethod(loader), new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                param.setResult(null);
            }
        });
    }
}
