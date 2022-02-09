package com.example.assistant.FileManager;

import static com.example.assistant.ContantsDefine.bBKeyId;
import static com.example.assistant.ContantsDefine.bBKeyKey;
import static com.example.assistant.ContantsDefine.bBKeyTableName;
import static com.example.assistant.ContantsDefine.bBKeyValue;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.assistant.ContantsDefine;

import java.util.ArrayList;


class SQLiteDBKeyHelper extends SQLiteOpenHelper {

    private final int DB_Version = 1;
    private static final String CREATE_TABLE = "Create Table If Not Exists " + bBKeyTableName + "(" + bBKeyId + " Text PRIMARY KEY, " + bBKeyKey + " Text, " + bBKeyValue + " Text);";

    public SQLiteDBKeyHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS DBKeyManager");
        onCreate(db);
    }

}

public class SQLiteDBKeyManager {

    private static SQLiteDBKeyManager single_instance = null;
    private Context context;
    private SQLiteDatabase database;
    private SQLiteDBKeyHelper dbHelper;

    public static SQLiteDBKeyManager GetInstance(Context c) {
        if (single_instance == null)
            single_instance = new SQLiteDBKeyManager(c);
        return single_instance;
    }
    public SQLiteDBKeyManager(Context c) {
        this.context = c;
    }
    public SQLiteDBKeyManager open() throws SQLException {
        this.dbHelper = new SQLiteDBKeyHelper(this.context, null, null, 1);
        this.database = this.dbHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        this.dbHelper.close();
    }
    public void delete(long _id) {
        open();
        this.database.delete(bBKeyTableName, bBKeyId+"=" + _id, null);
    }

    public void addKey(DBKeyObject dbKeyObject){
        open();
        ContentValues cv=new ContentValues();
        cv.put(bBKeyId,dbKeyObject.getId());
        cv.put(bBKeyKey,dbKeyObject.getKey());
        cv.put(bBKeyValue,dbKeyObject.getValue());

        this.database.insert(bBKeyTableName, null, cv);
        close();
        String value = GetValueByKey(dbKeyObject.getId());

    }
    @SuppressLint("Range")
    public String GetValueByKey(String objectKey){
        open();
        String result = "";
        ContentValues cv=new ContentValues();
        String query = "SELECT * FROM "+bBKeyTableName+" WHERE " +  bBKeyId + "="+objectKey;
        String whereclause = bBKeyId + "=?";
        String[] whereargs = new String[]{String.valueOf(objectKey)};

        @SuppressLint("Recycle") Cursor cursor = this.database.rawQuery(query,null);
        //Cursor cursor = this.database.query(bBKeyTableName, new String[]{bBKeyId, bBKeyKey, bBKeyValue}, whereclause, whereargs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            result =  cursor.getString(cursor.getColumnIndex(bBKeyValue));
        }
        close();
        return  result;
    }
    public void updateKey(DBKeyObject dbKeyObject){
        open();

        ContentValues cv=new ContentValues();
        cv.put(bBKeyId,dbKeyObject.getId());
        cv.put(bBKeyKey,dbKeyObject.getKey());
        cv.put(bBKeyValue,dbKeyObject.getValue());

        //this.database.replace(bBKeyTableName, null,cv);
        this.database.update(bBKeyTableName, cv, bBKeyId + "= " + dbKeyObject.getId(), null);
        close();
        String value = GetValueByKey(dbKeyObject.getId());
    }
    public void addKeys(ArrayList<DBKeyObject> dbKeyObjects){
        open();
        ContentValues cv=new ContentValues();
        for (DBKeyObject dbKeyObject: dbKeyObjects)
        {
            cv.put(bBKeyId,dbKeyObject.getId());
            cv.put(bBKeyKey,dbKeyObject.getKey());
            cv.put(bBKeyValue,dbKeyObject.getValue());
        }
        this.database.insert(bBKeyTableName, null, cv);
        close();
    }
    public void updateKeys(ArrayList<DBKeyObject> dbKeyObjects){
        open();

        ContentValues cv=new ContentValues();
        for (DBKeyObject dbKeyObject: dbKeyObjects)
        {
            cv.put(bBKeyId,dbKeyObject.getId());
            cv.put(bBKeyKey,dbKeyObject.getKey());
            cv.put(bBKeyValue,dbKeyObject.getValue());
        }
        this.database.replace(bBKeyTableName, null,cv);
        close();
    }
}