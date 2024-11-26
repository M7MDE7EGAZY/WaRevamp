package its.madruga.warevamp.module.hooks.others;

import static its.madruga.warevamp.module.references.References.pinnedChatsMethod;
import static its.madruga.warevamp.module.references.References.pinnedHashSetMethod;
import static its.madruga.warevamp.module.references.References.pinnedLimitMethod;

import android.view.MenuItem;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import its.madruga.warevamp.module.core.WppUtils;
import its.madruga.warevamp.module.hooks.core.HooksBase;
import its.madruga.warevamp.module.references.ReferencesUtils;

public class PinnedLimit extends HooksBase {
    public PinnedLimit(@NonNull ClassLoader loader, @NonNull XSharedPreferences preferences) {
        super(loader, preferences);
    }

    @Override
    public void doHook() throws Exception {
        super.doHook();

        boolean pinnedLimit = prefs.getBoolean("pinnedLimit", false);

        if(!pinnedLimit) return;

        XposedBridge.hookMethod(pinnedChatsMethod(loader), XC_MethodReplacement.returnConstant(60));

        XposedBridge.hookMethod(pinnedHashSetMethod(loader), new XC_MethodHook() {
            @Override
            @SuppressWarnings("unchecked")
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                var pinnedset = (Set) param.getResult();
                PinnedLinkedHashSet<Object> pinnedMod;

                if (!(pinnedset instanceof PinnedLinkedHashSet)) {
                    pinnedMod = new PinnedLinkedHashSet<>();
                    pinnedMod.addAll(pinnedset);
                    var setField = ReferencesUtils.getFieldByType(pinnedHashSetMethod(loader).getDeclaringClass(), Set.class);
                    XposedHelpers.setObjectField(param.thisObject, setField.getName(), pinnedMod);
                    param.setResult(pinnedMod);
                } else {
                    pinnedMod = (PinnedLinkedHashSet<Object>) pinnedset;
                }
                pinnedMod.setLimit(60);
            }
        });

        int idPin = WppUtils.getResourceId("menuitem_conversations_pin", "id");
        XposedBridge.hookMethod(pinnedLimitMethod(loader), new XC_MethodHook() {
            private Unhook hooked;

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (param.args.length > 0 && param.args[0] instanceof MenuItem menuItem) {
                    if (menuItem.getItemId() != idPin) return;
                    hooked = XposedHelpers.findAndHookMethod(HashSet.class, "size", XC_MethodReplacement.returnConstant(1));
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (hooked != null) hooked.unhook();
            }
        });
    }

    private static class PinnedLinkedHashSet<T> extends java.util.LinkedHashSet<T> {

        private int limit;


        @Override
        public int size() {
            if (super.size() >= limit) {
                return 3;
            }
            return 0;
        }

        public void setLimit(int i) {
            this.limit = i;
        }
    }
}
