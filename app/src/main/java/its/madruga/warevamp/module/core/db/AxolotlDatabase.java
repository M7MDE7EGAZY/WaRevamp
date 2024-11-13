package its.madruga.warevamp.module.core.db;

import static its.madruga.warevamp.module.hooks.core.HooksLoader.mApp;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;

public class AxolotlDatabase {

    private static AxolotlDatabase mInstance;

    private SQLiteDatabase sqLiteDatabase;

    private AxolotlDatabase() {
        var dataDir = mApp.getFilesDir().getParentFile();
        var dbFile = new File(dataDir, "/databases/axolotl.db");
        if (!dbFile.exists()) return;
        sqLiteDatabase = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
    }

    public static AxolotlDatabase getInstance() {
        synchronized (MessageStore.class) {
            if (mInstance == null || mInstance.sqLiteDatabase == null || !mInstance.sqLiteDatabase.isOpen()) {
                mInstance = new AxolotlDatabase();
            }
        }
        return mInstance;
    }

    public SQLiteDatabase getDatabase() {
        return sqLiteDatabase;
    }
}
