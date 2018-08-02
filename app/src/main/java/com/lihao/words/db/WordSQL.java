package com.lihao.words.db;

public class WordSQL {

    public static final int DATABASE_VERSION            = 1;
    public static final String DATABASE_NAME            = "words.db";
    public static final String DATABASE_TABLE_WORD      = "word";

    public static final String CREATE_TABLE_WORD        = "create table if not exists word (_id integer primary key autoincrement,firstLetter text,word text,wordUpperCase text,meaning text,status integer,creatTime integer,modifyTime integer)";

    public static final String COLUMN_FIRSTLETTER       = "firstLetter";
    public static final String COLUMN_WORD              = "word";
    public static final String COLUMN_WORD_UPPERCASE    = "wordUpperCase";
    public static final String COLUMN_MEANING           = "meaning";
    public static final String COLUMN_STATUS            = "status";
    public static final String COLUMN_CREATTIME         = "creatTime";
    public static final String COLUMN_MODIFYTIME        = "modifyTime";

    public static final String WORD_SORTORDER           = COLUMN_WORD_UPPERCASE + " ASC";



}
