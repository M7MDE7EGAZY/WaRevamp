package its.madruga.warevamp.core.broadcast;

import its.madruga.warevamp.core.broadcast.receivers.ModuleReceiver;
import its.madruga.warevamp.core.broadcast.receivers.WhatsAppReceiver;
import its.madruga.warevamp.core.broadcast.senders.WhatsAppSender;

public class Receivers {
    public static void registerReceivers() {
        new ModuleReceiver().registerAllReceivers();
        new WhatsAppReceiver().registerAllReceivers();
    }
}
