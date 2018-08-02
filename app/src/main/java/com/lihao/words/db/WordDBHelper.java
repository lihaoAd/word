package com.lihao.words.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WordDBHelper extends SQLiteOpenHelper {

    public WordDBHelper(Context context) {
        super(context, WordSQL.DATABASE_NAME, null, WordSQL.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WordSQL.CREATE_TABLE_WORD);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
