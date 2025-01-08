package its.madruga.warevamp.core.broadcast.senders;

import static its.madruga.warevamp.module.hooks.core.HooksLoader.mApp;

import android.content.Intent;
import android.content.pm.PackageManager;

import its.madruga.warevamp.core.broadcast.Events;

public class WhatsAppSender extends EventEmitter {

    public static WhatsAppSender emitter;

    public WhatsAppSender() {
        super(mApp);
    }

    public static void enabled() {
        var intent = new Intent(Events.ACTION_WA_REVAMP_ENABLED);
        try {
            intent.putExtra("enabled", true);
            intent.putExtra("version", mApp.getPackageManager().getPackageInfo(mApp.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        emitter.emit(Events.ACTION_WA_REVAMP_ENABLED);
    }

    public static void start() {
        emitter = new WhatsAppSender();
    }
}
