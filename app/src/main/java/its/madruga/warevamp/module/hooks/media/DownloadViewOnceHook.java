package its.madruga.warevamp.module.hooks.media;

import static its.madruga.warevamp.module.hooks.core.HooksLoader.mApp;
import static its.madruga.warevamp.module.hooks.media.DownloadStatusHook.getMimeTypeFromExtension;
import static its.madruga.warevamp.module.references.ModuleResources.drawable.download_icon;
import static its.madruga.warevamp.module.references.ModuleResources.string.download_viewonce;
import static its.madruga.warevamp.module.references.References.menuStyleField;
import static its.madruga.warevamp.module.references.References.menuViewOnceManagerMethod;

import android.content.res.ColorStateList;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import its.madruga.warevamp.module.core.FMessageInfo;
import its.madruga.warevamp.module.hooks.core.HooksBase;
import its.madruga.warevamp.module.references.ReferencesUtils;

public class DownloadViewOnceHook extends HooksBase {
    public DownloadViewOnceHook(@NonNull ClassLoader loader, @NonNull XSharedPreferences preferences) {
        super(loader, preferences);
    }

    @Override
    public void doHook() throws Exception {
        super.doHook();

        if (!prefs.getBoolean("download_viewonce", false)) return;

        XposedBridge.hookMethod(menuViewOnceManagerMethod(loader), new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                Field messageField = ReferencesUtils.getFieldByExtendType(param.thisObject.getClass(), FMessageInfo.TYPE);
                if(messageField == null) return;
                FMessageInfo messageInfo = new FMessageInfo(messageField.get(param.thisObject));

                if(XposedHelpers.getIntField(param.thisObject, menuStyleField(loader).getName()) == 3) {
                    Menu menu = (Menu) param.args[0];
                    MenuItem item = menu.add(0, 0, 0, download_viewonce);
                    item.setIcon(mApp.getDrawable(download_icon));
                    item.setIconTintList(ColorStateList.valueOf(mApp.getResources().getColor(android.R.color.white)));
                    item.setShowAsAction(2);
                    item.setOnMenuItemClickListener(item1 -> {
                        if(messageInfo != null) {
                            File file = messageInfo.getMediaFile();
                            if (copyFile(file)) {
                                Toast.makeText(mApp, "Saved", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mApp, "Error when saving, try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    });
                }
            }
        });
    }

    private static boolean copyFile(File p) {
        if (p == null) return false;

        var folderPath = Environment.getExternalStorageDirectory() + "/Pictures/WhatsApp/MdgWa ViewOnce/";
        var filePath = new File(folderPath);
        if (!filePath.exists()) filePath.mkdirs();
        var destination = filePath.getAbsolutePath() + "/" + p.getName();

        try (FileInputStream in = new FileInputStream(p);
             FileOutputStream out = new FileOutputStream(destination)) {
            byte[] bArr = new byte[1024];
            while (true) {
                int read = in.read(bArr);
                if (read <= 0) {
                    in.close();
                    out.close();

                    String[] parts = destination.split("\\.");
                    String ext = parts[parts.length - 1].toLowerCase();

                    MediaScannerConnection.scanFile(mApp,
                            new String[]{destination},
                            new String[]{getMimeTypeFromExtension(ext)},
                            (path, uri) -> {});

                    return true;
                }
                out.write(bArr, 0, read);
            }
        } catch (IOException e) {
            XposedBridge.log(e.getMessage());
            return false;
        }
    }
}
