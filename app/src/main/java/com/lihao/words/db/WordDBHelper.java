package com.lihao.words.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WordDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION_1            = 1;
    public static final int DATABASE_VERSION_3            = 3;
    public static final String DATABASE_NAME              = "words.db";


    public WordDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION_3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WordSQL.CREATE_TABLE);
        db.execSQL(IcibaeWordSQL.CREATE_TABLE);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < DATABASE_VERSION_3){
            db.execSQL(IcibaeWordSQL.CREATE_TABLE);
        }
    }
}
