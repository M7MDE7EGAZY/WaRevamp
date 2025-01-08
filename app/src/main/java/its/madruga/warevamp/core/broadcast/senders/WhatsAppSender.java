package its.madruga.warevamp.core.broadcast.senders;

import android.app.Application;

import its.madruga.warevamp.core.broadcast.Events;

public class WhatsAppSender extends EventEmitter {
    public WhatsAppSender(Application application) {
        super(application);
    }

   public void enabled() {
       emit(Events.ACTION_WA_REVAMP_ENABLED);
   }
}
