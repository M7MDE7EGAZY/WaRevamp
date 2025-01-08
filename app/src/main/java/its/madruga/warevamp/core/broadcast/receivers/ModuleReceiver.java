package its.madruga.warevamp.core.broadcast.receivers;

import its.madruga.warevamp.App;

public class ModuleReceiver extends EventReceiver {
    public ModuleReceiver() {
        super(App.getInstance());
    }

    @Override
    public void registerAllReceivers() {

    }
}
