package its.madruga.warevamp.core.broadcast.senders;

import android.app.Application;
import android.content.Intent;

public class EventEmitter {
    private final Application application;
    public static EventEmitter emitter;

    public EventEmitter(Application application) {
        this.application = application;
        emitter = this;
    }

    public void emit(Intent intent) {
        application.sendBroadcast(intent);
    }
    public void emit(String event) {
        var intent = new Intent(event);
        emit(intent);
    }
}
