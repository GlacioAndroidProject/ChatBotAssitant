package com.example.assistant.FileManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

public class ReadExcelFile {
    public static ArrayList<QuestionAnswerObject> ReadExcelFile(Activity context, String excelFilePath){
        ArrayList<QuestionAnswerObject>datas = new ArrayList<>();
        try
        {
            //FileInputStream am= new FileInputStream(excelFilePath);
            if(!Functions.checkPermission(context)){
                //Functions.requestPermission(context);
            }
            Uri uri = Uri.parse(excelFilePath);
            File excelFile = new File(uri.getPath());
            InputStream excelData= new FileInputStream(uri.getPath());
            Workbook wb=Workbook.getWorkbook(excelData);
            Sheet s=wb.getSheet(0);
            int row=s.getRows();
            int col=1;  // 2 column


            String xx="";
            for (int i=0;i<row;i++)
            {

                String question = "";
                String answer = "";
                for(int c=0;i<col;c++)
                {
                    Cell z=s.getCell(c,i);
                    if(c==0)
                        question = z.getContents();
                    else if (c==1)
                    {
                        answer = z.getContents();
                    }
                }
                datas.add(new QuestionAnswerObject(question, answer));
            }
        }

        catch (Exception e){
            System.out.println(e.toString());
        }
        return  datas;
    }
}
