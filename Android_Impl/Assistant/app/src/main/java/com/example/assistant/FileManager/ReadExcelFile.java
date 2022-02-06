package com.example.assistant.FileManager;
import java.io.InputStream;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import android.content.res.AssetManager;

public class ReadExcelFile {
    public static ArrayList<String> ReadExcelFile(String excelFilePath){
        ArrayList<String>datas = new ArrayList<>();
        try
        {
            AssetManager am= getAssets();
            InputStream is=am.open("book.xls");
            Workbook wb=Workbook.getWorkbook(is);
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
}
