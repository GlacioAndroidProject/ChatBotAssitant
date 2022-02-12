package com.example.assistant;
import android.content.Context;

import androidx.annotation.Nullable;

import com.example.assistant.DBManager.Objects.DBDataAIObject;
import com.example.assistant.DBManager.Manager.SQLiteDBDataAIManager;

import java.util.*;

public class KnowledgeBase
{
    public static TreeMap<String,String> quesResponseMap=null;
    private static void createData(@Nullable Context context){
        ArrayList<DBDataAIObject> dbDataAIObjectsInitial = new ArrayList<>();
        dbDataAIObjectsInitial.add(new DBDataAIObject("0","Bạn có khỏe không?","Vâng, tôi rất khỏe, còn bạn ?"));
        dbDataAIObjectsInitial.add(new DBDataAIObject("0","Tên bạn là gì?","Tôi là Trợ lý bác sĩ phiên bản 1.0"));
        dbDataAIObjectsInitial.add(new DBDataAIObject("0","Bạn sống ở đâu","Tôi ở trên điện thoại của bạn"));
        dbDataAIObjectsInitial.add(new DBDataAIObject("0","Tôi khỏe","Tốt rồi."));
        SQLiteDBDataAIManager.GetInstance(context).addKeys(dbDataAIObjectsInitial);
    }

    public static TreeMap<String,String> getKnowledgeBase(@Nullable Context context) throws Exception {
        createData(context);
        quesResponseMap = SQLiteDBDataAIManager.GetInstance(context).GetAllValueAsTreeMap();
//        Cursor resultSet = null;
//        try{
//        resultSet = mydatabase.rawQuery("Select * from " + ChatBot.DATABASE_NAME, null);
//        }catch(Exception e){
//            System.out.println("================= No Table found =======");
//            createData(mydatabase);
//            resultSet = mydatabase.rawQuery("Select * from " + ChatBot.DATABASE_NAME, null);
//        }
//        System.out.println(" resultSet : "+resultSet);
//        while(resultSet.moveToNext() != resultSet.isLast()){
//            quesResponseMap.put(resultSet.getString(0),resultSet.getString(1));
//        }
//        quesResponseMap.put(resultSet.getString(0),resultSet.getString(1));
//        System.out.println(quesResponseMap);
//        resultSet.close();
        return quesResponseMap;
    }

    public static void saveNewKnowledge(TreeMap<String,String> newKnowledge,@Nullable Context context) throws Exception
    {
        for(String key:  newKnowledge.keySet()){
            DBDataAIObject dbDataAIObject = new DBDataAIObject("0", key, newKnowledge.get(key));
            SQLiteDBDataAIManager.GetInstance(context).addKey(dbDataAIObject);
//            ContentValues values = new ContentValues();
//            values.put("Question",key);
//            values.put("Answer",newKnowledge.get(key));
//            mydatabase.insert(ChatBot.DATABASE_NAME,null,values);
        }
        System.out.println("============ saved ");
    }

    public static void saveLogs(String chat,String username) throws Exception
    {

    }

}