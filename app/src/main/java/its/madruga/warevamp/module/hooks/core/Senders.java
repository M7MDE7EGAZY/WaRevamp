package its.madruga.warevamp.module.hooks.core;

import its.madruga.warevamp.core.broadcast.senders.ModuleSender;
import its.madruga.warevamp.core.broadcast.senders.WhatsAppSender;

public class Senders {
    public static void initialize() {
        new WhatsAppSender();
        new ModuleSender();
    }
}
