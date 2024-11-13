package its.madruga.warevamp.module.references;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReferencesUtils {

    public static Field getFieldByType(Class<?> cls, Class<?> type) {
        return Arrays.stream(cls.getFields()).filter(f -> type == f.getType()).findFirst().orElse(null);
    }

    public static Field getFieldByExtendType(Class<?> cls, Class<?> type) {
        return Arrays.stream(cls.getFields()).filter(f -> type.isAssignableFrom(f.getType())).findFirst().orElse(null);
    }

    public synchronized static boolean isCalledFromMethod(Method method) {
        var trace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : trace) {
            if (stackTraceElement.getClassName().equals(method.getDeclaringClass().getName()) && stackTraceElement.getMethodName().equals(method.getName()))
                return true;
        }
        return false;
    }
}
