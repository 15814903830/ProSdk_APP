package com.zero.ci.network.http.cache.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * General field class.
 */
public abstract class BasicSQLHelper extends SQLiteOpenHelper {

    public static final String ID = "_id";

    public static final String ALL = "*";

    public BasicSQLHelper(Context context, String dbName, SQLiteDatabase.CursorFactory cursorFactory, int dbVersion) {
        super(context, dbName, cursorFactory, dbVersion);
    }

}
