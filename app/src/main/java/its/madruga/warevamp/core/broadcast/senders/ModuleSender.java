package its.madruga.warevamp.core.broadcast.senders;

import android.content.Intent;

import its.madruga.warevamp.App;
import its.madruga.warevamp.core.broadcast.Events;

public class ModuleSender extends EventEmitter {

    public ModuleSender() {
        super(App.getInstance());
    }

    public static void sendNeedsReboot(String... reason) {
        var intent = new Intent(Events.ACTION_WA_REVAMP_NEED_REBOOT);
        if (reason.length > 0) {
            intent.putExtra("reason", reason[0]);
        }
        emitter.emit(intent);
    }

    public static void sendRebootWpp(String... reason) {
        emitter.emit(Events.ACTION_WA_REVAMP_REBOOT);
    }

    public static void sendCheckWpp() {
        emitter.emit(Events.ACTION_WA_REVAMP_CHECK);
    }

    public static void sendCleanDatabase(String database) {
        var intent = new Intent(Events.ACTION_WA_REVAMP_CLEAN_DATABASE);
        intent.putExtra("dbName", database);
        emitter.emit(intent);
    }
}
