package com.example.assistant.FileManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import android.app.Activity;
import android.net.Uri;
import android.widget.Toast;

import com.example.assistant.DBManager.Objects.DBDataAIObject;
import com.example.assistant.R;


public class ReadExcelFile {
    public static ArrayList<DBDataAIObject> ReadExcelFile(Activity context, String excelFilePath){
        ArrayList<DBDataAIObject>datas = new ArrayList<>();
        try
        {
            Functions.RequestReadFilePermission(context);
            Uri uri = Uri.parse(excelFilePath);//fromFile(new File(excelFilePath));
            InputStream inputStream =context.getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split("_____");

                String question = "";
                String answer = "";
                if(row.length>=2) {
                    question = row[0];
                    question = question.replace("+++","\n");
                    answer = row[1];
                    answer = answer.replace("+++","\n");
                    if(!question.isEmpty() && !answer.isEmpty())
                        datas.add(new DBDataAIObject("0",question, answer));
                }

            }
            inputStream.close();
        }

        catch (Exception e){
            System.out.println(e.toString());
            Toast.makeText(context, R.string.openFileError,Toast.LENGTH_LONG);
        }
        return  datas;
    }
}
