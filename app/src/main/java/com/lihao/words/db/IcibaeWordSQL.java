package com.lihao.words.db;

public class IcibaeWordSQL {

    public static final String DATABASE_TABLE           = "icibaWord";

    public static final String CREATE_TABLE             = "create table if not exists icibaWord (_id integer primary key autoincrement,wordName text,phEn text,phAm text,phEnMp3 text,phAmMp3 text,parts text)";

    public static final String COLUMN_WORDNAME          = "wordName";
    public static final String COLUMN_PHEN              = "phEn";
    public static final String COLUMN_PHAM              = "phAm";
    public static final String COLUMN_PHENMP3           = "phEnMp3";
    public static final String COLUMN_PHAMMP3           = "phAmMp3";
    public static final String COLUMN_PARTS             = "parts";



}
