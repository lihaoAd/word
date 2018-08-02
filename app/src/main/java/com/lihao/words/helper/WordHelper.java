package com.lihao.words.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.lihao.words.db.WordSQL;
import com.lihao.words.entry.Word;
import com.lihao.words.provider.WordContentProvider;

import java.util.ArrayList;
import java.util.List;

public class WordHelper {

    public static void saveWordToDB(Context context, String word,String meaning){
        Cursor cursor = context.getContentResolver().query(WordContentProvider.CONNTENT_URL,
                null,WordSQL.COLUMN_WORD +" =?",new String[]{word},WordSQL.WORD_SORTORDER);
        if(cursor != null&&cursor.getCount()>0){
            ContentValues contentValues = new ContentValues();
            contentValues.put(WordSQL.COLUMN_WORD_UPPERCASE,word.toUpperCase());
            contentValues.put(WordSQL.COLUMN_MEANING,meaning);
            contentValues.put(WordSQL.COLUMN_MODIFYTIME,System.currentTimeMillis());
            context.getContentResolver().update(WordContentProvider.CONNTENT_URL,contentValues,WordSQL.COLUMN_WORD +" =?",new String[]{word});
            cursor.close();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(WordSQL.COLUMN_FIRSTLETTER,word.substring(0,1).toUpperCase());
        contentValues.put(WordSQL.COLUMN_WORD,word);
        contentValues.put(WordSQL.COLUMN_WORD_UPPERCASE,word.toUpperCase());
        contentValues.put(WordSQL.COLUMN_MEANING,meaning);
        contentValues.put(WordSQL.COLUMN_STATUS,Word.WORD_STATUS_NOT_REMEMBER);
        contentValues.put(WordSQL.COLUMN_CREATTIME,System.currentTimeMillis());
        contentValues.put(WordSQL.COLUMN_MODIFYTIME,System.currentTimeMillis());
        context.getContentResolver().insert(WordContentProvider.CONNTENT_URL,contentValues);
    }

    public static List<Word> queryAllWord(Context context){
        List<Word> words;
        Cursor cursor = context.getContentResolver().query(WordContentProvider.CONNTENT_URL,null,null,null,WordSQL.WORD_SORTORDER);
        if(cursor == null)return null;
        words = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()){
            words.add(new Word(cursor.getString(cursor.getColumnIndex(WordSQL.COLUMN_FIRSTLETTER)),
                    cursor.getString(cursor.getColumnIndex(WordSQL.COLUMN_WORD)),
                    cursor.getString(cursor.getColumnIndex(WordSQL.COLUMN_MEANING)),
                    cursor.getInt(cursor.getColumnIndex(WordSQL.COLUMN_STATUS)),
                    cursor.getLong(cursor.getColumnIndex(WordSQL.COLUMN_CREATTIME)),
                    cursor.getLong(cursor.getColumnIndex(WordSQL.COLUMN_MODIFYTIME))));
        }
        cursor.close();
        return words;
    }

    public static List<Word> queryWord(Context context,String filter){
        List<Word> words;
        Cursor cursor = context.getContentResolver().query(WordContentProvider.CONNTENT_URL,
                null,WordSQL.COLUMN_WORD +" like '%"+filter+"%'",null,WordSQL.WORD_SORTORDER);
        if(cursor == null)return null;
        words = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()){
            words.add(new Word(cursor.getString(cursor.getColumnIndex(WordSQL.COLUMN_FIRSTLETTER)),
                    cursor.getString(cursor.getColumnIndex(WordSQL.COLUMN_WORD)),
                    cursor.getString(cursor.getColumnIndex(WordSQL.COLUMN_MEANING)),
                    cursor.getInt(cursor.getColumnIndex(WordSQL.COLUMN_STATUS)),
                    cursor.getLong(cursor.getColumnIndex(WordSQL.COLUMN_CREATTIME)),
                    cursor.getLong(cursor.getColumnIndex(WordSQL.COLUMN_MODIFYTIME))));
        }
        cursor.close();
        return words;
    }


    public static void deleteWord(Context context,String word){
        context.getContentResolver().delete(WordContentProvider.CONNTENT_URL,WordSQL.COLUMN_WORD +" =?",new String[]{word});
    }

    public static void updateWord(Context context,Word wordEntry){
        ContentValues contentValues = new ContentValues();
        contentValues.put(WordSQL.COLUMN_FIRSTLETTER,wordEntry.word.substring(0,1).toUpperCase());
        contentValues.put(WordSQL.COLUMN_WORD,wordEntry.word);
        contentValues.put(WordSQL.COLUMN_WORD_UPPERCASE,wordEntry.word.toUpperCase());
        contentValues.put(WordSQL.COLUMN_MEANING,wordEntry.meaning);
        contentValues.put(WordSQL.COLUMN_STATUS,Word.WORD_STATUS_NOT_REMEMBER);
        contentValues.put(WordSQL.COLUMN_CREATTIME,System.currentTimeMillis());
        contentValues.put(WordSQL.COLUMN_MODIFYTIME,System.currentTimeMillis());
        context.getContentResolver().update(WordContentProvider.CONNTENT_URL,contentValues,WordSQL.COLUMN_WORD +" =?",new String[]{wordEntry.word});
    }
}
