package com.example.assistant.DBManager.Manager;

import static com.example.assistant.ContantsDefine.bBDatAIId;
import static com.example.assistant.ContantsDefine.bBDatAIKey;
import static com.example.assistant.ContantsDefine.bBDatAITableName;
import static com.example.assistant.ContantsDefine.bBDatAIValue;
import static com.example.assistant.ContantsDefine.chatBotDB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.assistant.DBManager.Objects.DBDataAIObject;

import java.util.ArrayList;
import java.util.TreeMap;

public class SQLiteDBDataAIManager {

    private static SQLiteDBDataAIManager single_instance = null;
    private Context context;
    private SQLiteDatabase database;
    private SQLiteDBHelper dbHelper;

    public static SQLiteDBDataAIManager GetInstance(Context c) {
        if (single_instance == null)
            single_instance = new SQLiteDBDataAIManager(c);
        return single_instance;
    }
    public SQLiteDBDataAIManager(Context c) {
        this.context = c;
    }
    public SQLiteDBDataAIManager openForWirteDB() throws SQLException {
        this.dbHelper = new SQLiteDBHelper(this.context, chatBotDB, null, 1);
        this.database = this.dbHelper.getWritableDatabase();
        return this;
    }
    public SQLiteDBDataAIManager openForReadDB() throws SQLException {
        this.dbHelper = new SQLiteDBHelper(this.context, chatBotDB, null, 1);
        this.database = this.dbHelper.getReadableDatabase();
        return this;
    }
    public void close() {
        this.dbHelper.close();
    }
    public void delete(long _id) {
        openForWirteDB();
        this.database.delete(bBDatAITableName, bBDatAIId+"=" + _id, null);
        close();
    }

    public void addKey(DBDataAIObject dBDataAIObject){

        openForWirteDB();
        ContentValues cv=new ContentValues();
        cv.put(bBDatAIKey,dBDataAIObject.getQuestion());
        cv.put(bBDatAIValue,dBDataAIObject.getAnswer());
        this.database.replace(bBDatAITableName, null, cv);

        close();
    }

    @SuppressLint("Range")
    public TreeMap<String,String> GetValueByKeyAsTreeMap(String objectKey){
        TreeMap<String,String> quesResponseMap = new TreeMap<String, String>();
        openForReadDB();
        String result = "";
        String[] projection = {
                bBDatAIId,
                bBDatAIKey,
                bBDatAIValue
        };
        String selection = bBDatAIKey + " = ?";
        String[] selectionArgs = { objectKey };
        // How you want the results sorted in the resulting Cursor
        String sortOrder = bBDatAIValue + " DESC";

        Cursor cursor = this.database.query(
                bBDatAITableName,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        //@SuppressLint("Recycle") Cursor cursor = this.database.rawQuery(query,null);
        //Cursor cursor = this.database.query(bBKeyTableName, new String[]{bBKeyId, bBKeyKey, bBKeyValue}, whereclause, whereargs, null, null, null);
        while(cursor.moveToNext() != cursor.isLast()){
            quesResponseMap.put(cursor.getString(1),cursor.getString(2));  // only get questioin and answer
        }
        close();
        return  quesResponseMap;
    }
    @SuppressLint("Range")
    public ArrayList<DBDataAIObject> GetValueByKeyAsObject(String objectKey){
        ArrayList<DBDataAIObject> dBDataAIObjects = new ArrayList<>();
        openForReadDB();
        String result = "";
        String[] projection = {
                bBDatAIId,
                bBDatAIKey,
                bBDatAIValue
        };
        String selection = bBDatAIKey + " = ?";
        String[] selectionArgs = { objectKey };
        // How you want the results sorted in the resulting Cursor
        String sortOrder = bBDatAIValue + " DESC";

        Cursor cursor = this.database.query(
                bBDatAITableName,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while(cursor.moveToNext() != cursor.isLast()){
            while(cursor.moveToNext() != cursor.isLast()){
                dBDataAIObjects.add(new DBDataAIObject("0",cursor.getString(1),cursor.getString(2)));  // only get questioin and answer
            }
        }
        close();
        return  dBDataAIObjects;
    }
    @SuppressLint("Range")
    public TreeMap<String,String> GetAllValueAsTreeMap(){
        openForReadDB();
        String result = "";
        TreeMap<String,String> quesResponseMap = new TreeMap<String, String>();
        String[] projection = {
                bBDatAIId,
                bBDatAIKey,
                bBDatAIValue
        };
        // How you want the results sorted in the resulting Cursor
        String sortOrder = bBDatAIValue + " DESC";
        Cursor cursor = this.database.query(
                bBDatAITableName,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        while(cursor.moveToNext() != cursor.isLast()){
            quesResponseMap.put(cursor.getString(1),cursor.getString(2));  // only get questioin and answer
        }
        close();
        return  quesResponseMap;
    }
    public ArrayList<DBDataAIObject> GetAllValueAsObjects(){
        openForReadDB();
        String result = "";
        ArrayList<DBDataAIObject> dBDataAIObjects = new ArrayList<>();
        String[] projection = {
                bBDatAIId,
                bBDatAIKey,
                bBDatAIValue
        };
        // How you want the results sorted in the resulting Cursor
        String sortOrder = bBDatAIValue + " DESC";
        Cursor cursor = this.database.query(
                bBDatAITableName,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        while(cursor.moveToNext() != cursor.isLast()){
            dBDataAIObjects.add(new DBDataAIObject("0",cursor.getString(1),cursor.getString(2)));  // only get questioin and answer
        }
        close();
        return  dBDataAIObjects;
    }

    public void updateKey(DBDataAIObject dBDataAIObject){
        openForWirteDB();
        // New value for one column
        ContentValues values=new ContentValues();
        //cv.put(bBKeyId,dbKeyObject.getId());
        values.put(bBDatAIKey,dBDataAIObject.getQuestion());
        values.put(bBDatAIValue,dBDataAIObject.getAnswer());
// Which row to update, based on the title
        String selection = bBDatAIKey + " LIKE ?";

        String[] selectionArgs = { dBDataAIObject.getQuestion() };

        int count = this.database.update(
                bBDatAITableName,
                values,
                selection,
                selectionArgs);
        close();
    }

    public void addKeys(ArrayList<DBDataAIObject> dBDataAIObjects){
        openForWirteDB();
        for (DBDataAIObject dBDataAIObject: dBDataAIObjects)
        {   ContentValues cv=new ContentValues();
            cv.put(bBDatAIKey,dBDataAIObject.getQuestion());
            cv.put(bBDatAIValue,dBDataAIObject.getAnswer());
            this.database.replace(bBDatAITableName, null, cv);
        }
        close();
    }

    public void updateKeys(ArrayList<DBDataAIObject> dBDataAIObjects){
        openForWirteDB();
        ArrayList<String>  mStringList= new ArrayList<String>();

        ContentValues values=new ContentValues();
        for (DBDataAIObject dBDataAIObject: dBDataAIObjects)
        {
            values.put(bBDatAIKey,dBDataAIObject.getQuestion());
            values.put(bBDatAIValue,dBDataAIObject.getAnswer());
            mStringList.add(dBDataAIObject.getQuestion());
        }
        // Which row to update, based on the title
        String selection = bBDatAIKey + " LIKE ?";
        String[] selectionArgs = mStringList.toArray(new String[mStringList.size()]);


        int count = this.database.update(
                bBDatAITableName,
                values,
                selection,
                selectionArgs);
        close();
    }
}
