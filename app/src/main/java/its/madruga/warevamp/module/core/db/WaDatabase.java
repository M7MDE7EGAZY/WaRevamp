package its.madruga.warevamp.module.core.db;

import static its.madruga.warevamp.module.hooks.core.HooksLoader.mApp;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;

public class WaDatabase {

    private static WaDatabase mInstance;

    private SQLiteDatabase sqLiteDatabase;

    private WaDatabase() {
        var dataDir = mApp.getFilesDir().getParentFile();
        var dbFile = new File(dataDir, "/databases/wa.db");
        if (!dbFile.exists()) return;
        sqLiteDatabase = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
    }

    public static WaDatabase getInstance() {
        synchronized (MessageStore.class) {
            if (mInstance == null || mInstance.sqLiteDatabase == null || !mInstance.sqLiteDatabase.isOpen()) {
                mInstance = new WaDatabase();
            }
        }
        return mInstance;
    }

    public SQLiteDatabase getDatabase() {
        return sqLiteDatabase;
    }
}
