package its.madruga.warevamp.core;

import static its.madruga.warevamp.module.hooks.core.HooksLoader.home;
import static its.madruga.warevamp.module.hooks.core.HooksLoader.mApp;
import static its.madruga.warevamp.module.references.ModuleResources.string.clean_database_ok;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.File;

import its.madruga.warevamp.App;
import its.madruga.warevamp.BuildConfig;
import its.madruga.warevamp.module.core.db.AxolotlDatabase;
import its.madruga.warevamp.module.core.db.MessageStore;
import its.madruga.warevamp.module.core.db.StickerDatabase;
import its.madruga.warevamp.module.core.db.WaDatabase;

public class Receivers {
    public static boolean needRestart = false;

    public static void getReceivers() {
        sendEnableWpp();

        BroadcastReceiver wppEnableReceive = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                sendEnableWpp();
            }
        };
        ContextCompat.registerReceiver(mApp, wppEnableReceive, new IntentFilter(BuildConfig.APPLICATION_ID + ".WaRevampCheck"), ContextCompat.RECEIVER_EXPORTED);

        BroadcastReceiver wppRebootReceive = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                CharSequence appName = mApp.getPackageManager().getApplicationLabel(mApp.getApplicationInfo());
                Toast.makeText(mApp,  "Rebooting " + appName + "...", Toast.LENGTH_SHORT).show();

                if (!doRestart(home))
                    Toast.makeText(mApp, "Unable to rebooting " + appName, Toast.LENGTH_SHORT).show();
            }
        };
        ContextCompat.registerReceiver(mApp, wppRebootReceive, new IntentFilter(BuildConfig.APPLICATION_ID + ".WaRevampReboot"), ContextCompat.RECEIVER_EXPORTED);

        BroadcastReceiver wppNeedReboot = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                needRestart = true;
            }
        };
        ContextCompat.registerReceiver(mApp, wppNeedReboot, new IntentFilter(BuildConfig.APPLICATION_ID + ".WaRevampNeedReboot"), ContextCompat.RECEIVER_EXPORTED);

        BroadcastReceiver wppCleanDatabase = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String dbName = intent.getStringExtra("dbName");
                if (dbName == null) return;
                switch (dbName) {
                    case "msgstore":
                        cleanDatabase(MessageStore.getInstance().getDatabase().getPath(), context);
                        break;
                    case "sticker":
                        cleanDatabase(StickerDatabase.getInstance().getDatabase().getPath(), context);
                        break;
                    case "wa":
                        cleanDatabase(WaDatabase.getInstance().getDatabase().getPath(), context);
                        break;
                    case "axolotl":
                        cleanDatabase(AxolotlDatabase.getInstance().getDatabase().getPath(), context);
                        break;
                    default:
                        break;
                }
            }
        };

        ContextCompat.registerReceiver(mApp, wppCleanDatabase, new IntentFilter(BuildConfig.APPLICATION_ID + ".WaRevampCleanDatabase"), ContextCompat.RECEIVER_EXPORTED);
    }

    private static void cleanDatabase(String path, Context context) {
        File dbFile = new File(path);
        if (!dbFile.exists()) return;
        dbFile.deleteOnExit();
        Toast.makeText(context, clean_database_ok, Toast.LENGTH_SHORT).show();
        needRestart = true;
    }

    public static void sendCheckWpp() {
        Intent intent = new Intent(BuildConfig.APPLICATION_ID + ".WaRevampCheck");
        App.getInstance().sendBroadcast(intent);
    }

    public static void sendRebootWpp() {
        Intent intent = new Intent(BuildConfig.APPLICATION_ID + ".WaRevampReboot");
        App.getInstance().sendBroadcast(intent);
    }

    public static void sendNeedRebootWpp() {
        needRestart = true;
        Intent intent = new Intent(BuildConfig.APPLICATION_ID + ".WaRevampNeedReboot");
        App.getInstance().sendBroadcast(intent);
    }

    public static void sendCleanDatabase(String dbName) {
        Intent intent = new Intent(BuildConfig.APPLICATION_ID + ".WaRevampCleanDatabase");
        intent.putExtra("dbName", dbName);
        App.getInstance().sendBroadcast(intent);
    }

    public static boolean doRestart(Activity home) {
        Intent intent = mApp.getPackageManager().getLaunchIntentForPackage(mApp.getPackageName());
        if (mApp != null && home != null) {
            home.finishAffinity();
            mApp.startActivity(intent);
        }
        Runtime.getRuntime().exit(0);
        return true;
    }

    public static void sendEnableWpp() {
        try {
            Intent intent = new Intent(BuildConfig.APPLICATION_ID + ".WaRevampEnable");
            intent.putExtra("wa_version", mApp.getPackageManager().getPackageInfo(mApp.getPackageName(), 0).versionName);
            intent.putExtra("pkg", mApp.getPackageName());
            mApp.sendBroadcast(intent);
        }catch (Exception ignore) {
        }
    }
}
