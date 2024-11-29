package its.madruga.warevamp;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

public class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DynamicColors.applyToActivitiesIfAvailable(this);
    }

    public static App getInstance() {
        return instance;
    }
}
