package com.example.assistant.FileManager;

import static com.example.assistant.ContantsDefine.bBKeyId;
import static com.example.assistant.ContantsDefine.bBKeyKey;
import static com.example.assistant.ContantsDefine.bBKeyTableName;
import static com.example.assistant.ContantsDefine.bBKeyValue;
import static com.example.assistant.ContantsDefine.chatBotDB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;


class SQLiteDBKeyHelper extends SQLiteOpenHelper {

    private final int DB_Version = 1;
    //private static final String CREATE_TABLE = "Create Table If Not Exists " + bBKeyTableName + " (" + bBKeyId + " Text PRIMARY KEY, " + bBKeyKey + " Text, " + bBKeyValue + " Text);";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + bBKeyTableName + "("
            + bBKeyId + " INTEGER PRIMARY KEY AUTOINCREMENT, " + bBKeyKey  + " TEXT, "
            + bBKeyValue + " TEXT)";
    public SQLiteDBKeyHelper(@Nullable Context context, @Nullable String dBName, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dBName, factory, version);
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
    public SQLiteDBKeyManager openForWirteDB() throws SQLException {
        this.dbHelper = new SQLiteDBKeyHelper(this.context, chatBotDB, null, 1);
        this.database = this.dbHelper.getWritableDatabase();
        return this;
    }
    public SQLiteDBKeyManager openForReadDB() throws SQLException {
        this.dbHelper = new SQLiteDBKeyHelper(this.context, chatBotDB, null, 1);
        this.database = this.dbHelper.getReadableDatabase();
        return this;
    }
    public void close() {
        this.dbHelper.close();
    }
    public void delete(long _id) {
        openForWirteDB();
        this.database.delete(bBKeyTableName, bBKeyId+"=" + _id, null);
    }

    public void addKey(DBKeyObject dbKeyObject){
        openForWirteDB();
        ContentValues cv=new ContentValues();
        //cv.put(bBKeyId,dbKeyObject.getId());
        cv.put(bBKeyKey,dbKeyObject.getKey());
        cv.put(bBKeyValue,dbKeyObject.getValue());

        this.database.insert(bBKeyTableName, null, cv);
        close();
        String result = GetValueByKey(dbKeyObject.getKey());
    }

    @SuppressLint("Range")
    public String GetValueByKey(String objectKey){
        openForReadDB();
        String result = "";
        String[] projection = {
                bBKeyId,
                bBKeyKey,
                bBKeyValue
        };
        String selection = bBKeyKey + " = ?";
        String[] selectionArgs = { objectKey };
        // How you want the results sorted in the resulting Cursor
        String sortOrder = bBKeyValue + " DESC";

        Cursor cursor = this.database.query(
                bBKeyTableName,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        //@SuppressLint("Recycle") Cursor cursor = this.database.rawQuery(query,null);
        //Cursor cursor = this.database.query(bBKeyTableName, new String[]{bBKeyId, bBKeyKey, bBKeyValue}, whereclause, whereargs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            result =  cursor.getString(cursor.getColumnIndex(bBKeyValue));
        }
        close();
        return  result;
    }
    public void updateKey(DBKeyObject dbKeyObject){
        openForWirteDB();

        // New value for one column
        ContentValues values=new ContentValues();
        //cv.put(bBKeyId,dbKeyObject.getId());
        values.put(bBKeyKey,dbKeyObject.getKey());
        values.put(bBKeyValue,dbKeyObject.getValue());
// Which row to update, based on the title
        String selection = bBKeyKey + " LIKE ?";

        String[] selectionArgs = { dbKeyObject.getKey() };

        int count = this.database.update(
                bBKeyTableName,
                values,
                selection,
                selectionArgs);
        close();
    }

    public void addKeys(ArrayList<DBKeyObject> dbKeyObjects){
        openForWirteDB();
        ContentValues cv=new ContentValues();
        for (DBKeyObject dbKeyObject: dbKeyObjects)
        {
            cv.put(bBKeyKey,dbKeyObject.getKey());
            cv.put(bBKeyValue,dbKeyObject.getValue());
        }
        this.database.insert(bBKeyTableName, null, cv);
        close();
    }

    public void updateKeys(ArrayList<DBKeyObject> dbKeyObjects){
        openForWirteDB();
        ArrayList<String>  mStringList= new ArrayList<String>();

        ContentValues values=new ContentValues();
        for (DBKeyObject dbKeyObject: dbKeyObjects)
        {
            values.put(bBKeyKey,dbKeyObject.getKey());
            values.put(bBKeyValue,dbKeyObject.getValue());
            mStringList.add(dbKeyObject.getKey());
        }
        // Which row to update, based on the title
        String selection = bBKeyKey + " LIKE ?";
        String[] selectionArgs = mStringList.toArray(new String[mStringList.size()]);


        int count = this.database.update(
                bBKeyTableName,
                values,
                selection,
                selectionArgs);
        close();
    }
}