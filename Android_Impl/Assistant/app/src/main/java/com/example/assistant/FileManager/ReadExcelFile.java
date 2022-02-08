package com.example.assistant.FileManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Environment;

public class ReadExcelFile {
    public static ArrayList<String> ReadExcelFile(String excelFilePath){
        ArrayList<String>datas = new ArrayList<>();
        try
        {
            //FileInputStream am= new FileInputStream(excelFilePath);
            InputStream excelData=new FileInputStream(excelFilePath);
            Workbook wb=Workbook.getWorkbook(excelData);
            Sheet s=wb.getSheet(0);
            int row=s.getRows();
            int col=s.getColumns();

            String xx="";
            for (int i=0;i<row;i++)
            {

                for(int c=0;i<col;c++)
                {
                    Cell z=s.getCell(c,i);
                    datas.add(z.getContents());
                    xx=xx+z.getContents();

                }

                xx=xx+"\n";
            }
        }

        catch (Exception e){}
        return  datas;
    }
    public static String GetDefaultSnapshotFolderPath(Activity mContext){
        if(!Functions.checkPermission(mContext)){
            Functions.requestPermission(mContext);
        }
        String folderPath ;
        ApplicationInfo applicationInfo = mContext.getApplicationInfo();
        int appNameID = applicationInfo.labelRes;
        String AppName = appNameID == 0 ? applicationInfo.nonLocalizedLabel.toString() : mContext.getString(appNameID);

        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.R){
            folderPath = Environment.getExternalStorageDirectory() +"/"+ Environment.DIRECTORY_PICTURES+ "/"+ AppName+"/";
        } else
            folderPath =mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();

        return  folderPath;
    }
}
