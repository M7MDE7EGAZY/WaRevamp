package its.madruga.warevamp.module.core.db;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;

import static its.madruga.warevamp.module.hooks.core.HooksLoader.mApp;

public class MessageStore {


    private static MessageStore mInstance;

    private SQLiteDatabase sqLiteDatabase;

    private MessageStore() {
        var dataDir = mApp.getFilesDir().getParentFile();
        var dbFile = new File(dataDir, "/databases/msgstore.db");
        if (!dbFile.exists()) return;
        sqLiteDatabase = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
    }

    public static MessageStore getInstance() {
        synchronized (MessageStore.class) {
            if (mInstance == null || mInstance.sqLiteDatabase == null || !mInstance.sqLiteDatabase.isOpen()) {
                mInstance = new MessageStore();
            }
        }
        return mInstance;
    }

    public SQLiteDatabase getDatabase() {
        return sqLiteDatabase;
    }
}