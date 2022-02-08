package com.example.assistant.FileManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SQLiteDBKeyManager extends SQLiteOpenHelper {
    public String tableName="DBKeyManager";
    public String id="id";
    public String key="key_name";
    public String value="key_value";
    private final int DB_Version=1;
    private static SQLiteDBKeyManager single_instance = null;

    public static SQLiteDBKeyManager GetInstance(){
        if (single_instance == null)
            single_instance = new SQLiteDBKeyManager(null,null,null, GetInstance().DB_Version);

        return single_instance;
    }
    private SQLiteDBKeyManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table " + tableName + "(" + id + " INTEGER PRIMARY KEY, " + key + " Text, " + value + " Text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addKey(DBKeyObject dbKeyObject){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(id,dbKeyObject.getId());
        cv.put(key,dbKeyObject.getKey());
        cv.put(value,dbKeyObject.getValue());

        db.insert(tableName, null, cv);
        db.close();
    }
    public void updateKey(DBKeyObject dbKeyObject){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(id,dbKeyObject.getId());
        cv.put(key,dbKeyObject.getKey());
        cv.put(value,dbKeyObject.getValue());

        db.replace(tableName, null,cv);
        db.close();
    }
    public void addKeys(ArrayList<DBKeyObject> dbKeyObjects){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        for (DBKeyObject dbKeyObject: dbKeyObjects)
        {
            cv.put(id,dbKeyObject.getId());
            cv.put(key,dbKeyObject.getKey());
            cv.put(value,dbKeyObject.getValue());
        }
        db.insert(tableName, null, cv);
        db.close();
    }
    public void updateKeys(ArrayList<DBKeyObject> dbKeyObjects){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        for (DBKeyObject dbKeyObject: dbKeyObjects)
        {
            cv.put(id,dbKeyObject.getId());
            cv.put(key,dbKeyObject.getKey());
            cv.put(value,dbKeyObject.getValue());
        }
        db.replace(tableName, null,cv);
        db.close();
    }
}