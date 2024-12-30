package its.madruga.warevamp.module.hooks.media;

import android.graphics.Bitmap;
import android.graphics.RecordingCanvas;
import android.os.Build;
import android.util.Pair;
import androidx.annotation.NonNull;
import de.robv.android.xposed.*;
import its.madruga.warevamp.module.hooks.core.HooksBase;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static its.madruga.warevamp.module.references.References.*;

public class MediaQualityHook extends HooksBase {

    public MediaQualityHook(@NonNull @NotNull ClassLoader loader, @NonNull @NotNull XSharedPreferences preferences) {
        super(loader, preferences);
    }

    @Override
    public void doHook() throws Exception {
        super.doHook();

    boolean videoQuality = prefs.getBoolean("videoQuality", false);
        boolean imageQuality = prefs.getBoolean("imageQuality", false);

        if (videoQuality) {
            XposedBridge.hookMethod(videoResolutionMethod(loader), new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Pair<?,?> pair = new Pair<>(param.args[0], param.args[1]);
                    param.setResult(pair);
                }
            });
            XposedBridge.hookMethod(videoBitrateMethod(loader), new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    param.setResult(1600000);
                }
            });
            XposedBridge.hookMethod(videoGifBitrateMethod(loader), new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    return new Pair<>(true, new ArrayList<>());
                }
            });
        }

        if (imageQuality) {

            XposedBridge.hookMethod(imageQualityMethod(loader), new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    int p1 = (int) param.args[2];
                    int[] props = {1573, 1575, 1578, 1574, 1576, 1577};
                    int max = 10000;
                    int min = 1000;
                    for (int index = 0; index < props.length; index++) {
                        if (props[index] == p1) {
                            if (index <= 2) {
                                param.setResult(min);
                            } else {
                                param.setResult(max);
                            }
                        }
                    }
                    super.beforeHookedMethod(param);
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                XposedHelpers.findAndHookMethod(RecordingCanvas.class, "throwIfCannotDraw", Bitmap.class, XC_MethodReplacement.DO_NOTHING);
            }
        }
    }
}
