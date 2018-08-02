package com.lihao.words.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lihao.words.db.WordDBHelper;
import com.lihao.words.db.WordSQL;

public class WordContentProvider extends ContentProvider {

    private static UriMatcher sUriMatcher;
    public static final String HOST = "com.lihao.words.WordContentProvider";
    public static final String PATH = WordSQL.DATABASE_TABLE_WORD;
    public static final Uri CONNTENT_URL = Uri.parse("content://"+HOST+"/"+PATH);


    private WordDBHelper wordDBHelper;


    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(HOST,PATH,1);
    }

    @Override
    public boolean onCreate() {
        wordDBHelper = new WordDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
      if(sUriMatcher.match(uri) == 1){
          return "vnd.android.cursor.item/vndcom.lihao.words.word";
      }
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase readDatabase = null;
        Cursor cursor = null;
        switch (sUriMatcher.match(uri)){
            case 1:
                readDatabase = wordDBHelper.getReadableDatabase();
                try {
                    readDatabase.beginTransaction();
                    cursor = readDatabase.query(WordSQL.DATABASE_TABLE_WORD,projection,selection,selectionArgs,null,null,sortOrder);
                    readDatabase.setTransactionSuccessful();
                }finally {
                    readDatabase.endTransaction();
                }

                break;
        }
        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase writeDatabase = null;
        Cursor cursor = null;
        switch (sUriMatcher.match(uri)){
            case 1:
                writeDatabase = wordDBHelper.getWritableDatabase();
                try {
                    writeDatabase.beginTransaction();
                    long id =  writeDatabase.insert(WordSQL.DATABASE_TABLE_WORD,null,values);
                    writeDatabase.setTransactionSuccessful();


                    if(id > 0){
                        Uri uriChange = Uri.parse(CONNTENT_URL+"/"+id);
                        ContentResolver contentResolver = getContext().getContentResolver();
                        if(contentResolver!=null){
                            contentResolver.notifyChange(uriChange,null);
                        }
                    }

                }finally {
                    writeDatabase.endTransaction();
                }

                break;
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase writeDatabase = null;
        int rows = 0;
        switch (sUriMatcher.match(uri)){
            case 1:
                writeDatabase = wordDBHelper.getWritableDatabase();
                try {
                    writeDatabase.beginTransaction();
                    rows =  writeDatabase.delete(WordSQL.DATABASE_TABLE_WORD,selection,selectionArgs);
                    writeDatabase.setTransactionSuccessful();
                    if(rows > 0){
                        ContentResolver contentResolver = getContext().getContentResolver();
                        if(contentResolver!=null){
                            contentResolver.notifyChange(uri,null);
                        }
                    }

                }finally {
                    writeDatabase.endTransaction();
                }
                break;
        }
        return rows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase writeDatabase = null;
        int rows = 0;
        switch (sUriMatcher.match(uri)){
            case 1:
                writeDatabase = wordDBHelper.getWritableDatabase();
                try {
                    writeDatabase.beginTransaction();
                    rows =  writeDatabase.update(WordSQL.DATABASE_TABLE_WORD,values,selection,selectionArgs);
                    writeDatabase.setTransactionSuccessful();
                    if(rows > 0){
                        ContentResolver contentResolver = getContext().getContentResolver();
                        if(contentResolver!=null){
                            contentResolver.notifyChange(uri,null);
                        }
                    }

                }finally {
                    writeDatabase.endTransaction();
                }
                break;
        }
        return rows;
    }
}
