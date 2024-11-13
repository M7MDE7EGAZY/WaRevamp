package its.madruga.warevamp.module.core;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static its.madruga.warevamp.module.references.References.*;

public class FMessageInfo {
    private final Object messageObject;
    private static Method messageMethod;
    private static Method messageWithMediaMethod;
    private static Field keyMessage;
    private static boolean initialized;

    public FMessageInfo(Object message) {
        if (message == null) throw new RuntimeException("Message is null");
        this.messageObject = message;
        try {
            init(message.getClass().getClassLoader());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void init(ClassLoader loader) throws Exception {
        if (initialized) return;
        initialized = true;
        keyMessage = keyMessageField(loader);
        messageMethod = newMessageMethod(loader);
        messageWithMediaMethod = newMessageWithMediaMethod(loader);
    }

    public Key getKey() {
        try {
            return new Key(keyMessage.get(messageObject));
        } catch (Exception e) {
            XposedBridge.log(e);
            return null;
        }
    }

    public String getMessageStr() {
        try {
            String message = (String) messageMethod.invoke(messageObject);
            if (message != null) return message;
            return (String) messageWithMediaMethod.invoke(messageObject);
        } catch (Exception e) {
            XposedBridge.log(e);
            return null;
        }
    }

    public Object getObject() {
        return messageObject;
    }

    public static class Key {

        public final Object thisObject;
        public final String messageID;
        public final boolean isFromMe;
        public final Object remoteJid;

        public Key(Object key) {
            this.thisObject = key;
            this.messageID = (String) XposedHelpers.getObjectField(key, "A01");
            this.isFromMe = XposedHelpers.getBooleanField(key, "A02");
            this.remoteJid = XposedHelpers.getObjectField(key, "A00");
        }

    }
}
