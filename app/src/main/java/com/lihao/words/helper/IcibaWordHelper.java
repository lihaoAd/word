package com.lihao.words.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lihao.words.db.IcibaeWordSQL;
import com.lihao.words.entry.IcibaPart;
import com.lihao.words.entry.IcibaWord;
import com.lihao.words.provider.IcibaWordContentProvider;
import java.util.List;

public class IcibaWordHelper {

    public static void saveIcibaWordToDB(Context context, IcibaWord word){
        ContentValues contentValues = new ContentValues();
        contentValues.put(IcibaeWordSQL.COLUMN_WORDNAME,word.wordName);
        contentValues.put(IcibaeWordSQL.COLUMN_PHAM,word.phAm);
        contentValues.put(IcibaeWordSQL.COLUMN_PHEN,word.phEn);
        contentValues.put(IcibaeWordSQL.COLUMN_PHAMMP3,word.phAmMp3);
        contentValues.put(IcibaeWordSQL.COLUMN_PHENMP3,word.phEnMp3);
        contentValues.put(IcibaeWordSQL.COLUMN_PARTS,new Gson().toJson(word.parts));
        Cursor cursor = context.getContentResolver().query(IcibaWordContentProvider.CONNTENT_URL,
                null, IcibaeWordSQL.COLUMN_WORDNAME +" =?",new String[]{word.wordName},null);
        if(cursor != null&&cursor.getCount()>0){
            context.getContentResolver().update(IcibaWordContentProvider.CONNTENT_URL,contentValues,IcibaeWordSQL.COLUMN_WORDNAME +" =?",new String[]{word.wordName});
            cursor.close();
            return;
        }
        context.getContentResolver().insert(IcibaWordContentProvider.CONNTENT_URL,contentValues);
    }


    public static IcibaWord queryIcibaWord(Context context,String word){
        IcibaWord icibaWord;
        Cursor cursor = context.getContentResolver().query(IcibaWordContentProvider.CONNTENT_URL,
                null,IcibaeWordSQL.COLUMN_WORDNAME +" =?",new String[]{word},null);
        if(cursor == null || cursor.getCount() == 0)return null;
        icibaWord = new IcibaWord();
        cursor.moveToNext();
        icibaWord.wordName = cursor.getString(cursor.getColumnIndex(IcibaeWordSQL.COLUMN_WORDNAME));
        icibaWord.phAm     = cursor.getString(cursor.getColumnIndex(IcibaeWordSQL.COLUMN_PHAM));
        icibaWord.phEn     = cursor.getString(cursor.getColumnIndex(IcibaeWordSQL.COLUMN_PHEN));
        icibaWord.phAmMp3  = cursor.getString(cursor.getColumnIndex(IcibaeWordSQL.COLUMN_PHAMMP3));
        icibaWord.phEnMp3  = cursor.getString(cursor.getColumnIndex(IcibaeWordSQL.COLUMN_PHENMP3));
        icibaWord.parts    = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(IcibaeWordSQL.COLUMN_PARTS)),new TypeToken<List<IcibaPart>>(){}.getType());
        cursor.close();
        return icibaWord;
    }
}
