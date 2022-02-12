package com.example.assistant.DBManager.Manager;

import static com.example.assistant.ContantsDefine.bBDatAIId;
import static com.example.assistant.ContantsDefine.bBDatAIKey;
import static com.example.assistant.ContantsDefine.bBDatAITableName;
import static com.example.assistant.ContantsDefine.bBDatAIValue;
import static com.example.assistant.ContantsDefine.bBKeyId;
import static com.example.assistant.ContantsDefine.bBKeyKey;
import static com.example.assistant.ContantsDefine.bBKeyTableName;
import static com.example.assistant.ContantsDefine.bBKeyValue;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

class SQLiteDBHelper extends SQLiteOpenHelper {

    private final int DB_Version = 1;
    //private static final String CREATE_TABLE = "Create Table If Not Exists " + bBKeyTableName + " (" + bBKeyId + " Text PRIMARY KEY, " + bBKeyKey + " Text, " + bBKeyValue + " Text);";
    private static final String CREATE_TABLE_KEY_MANAGER = "CREATE TABLE IF NOT EXISTS " + bBKeyTableName + "("
            + bBKeyId + " INTEGER PRIMARY KEY AUTOINCREMENT, " + bBKeyKey  + " TEXT, "
            + bBKeyValue + " TEXT)";

    private static final String CREATE_TABLE_DATA_AI = "CREATE TABLE IF NOT EXISTS " + bBDatAITableName + "("
            + bBDatAIId + " INTEGER PRIMARY KEY AUTOINCREMENT, " + bBDatAIKey  + " TEXT, "
            + bBDatAIValue + " TEXT)";
    public SQLiteDBHelper(@Nullable Context context, @Nullable String dBName, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dBName, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_KEY_MANAGER);
        db.execSQL(CREATE_TABLE_DATA_AI);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ bBKeyTableName);
        db.execSQL("DROP TABLE IF EXISTS "+ bBDatAITableName);
        onCreate(db);
    }

}