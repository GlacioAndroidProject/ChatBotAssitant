package com.example.assistant.FileManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.example.assistant.R;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcelFile {
    public static ArrayList<DBDataAIObject> ReadExcelFile(Activity context, String excelFilePath){
        ArrayList<DBDataAIObject>datas = new ArrayList<>();
        try
        {
            //FileInputStream am= new FileInputStream(excelFilePath);
            Functions.RequestReadFilePermission(context);
            Uri uri = Uri.parse(excelFilePath);//fromFile(new File(excelFilePath));
            //context.grantUriPermission(context.getPackageName(), uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            InputStream inputStream =context.getContentResolver().openInputStream(uri);
            //XSSFWorkbook workbook = new XSSFWorkbook(stream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");

                String question = "";
                String answer = "";
                if(row.length>=2) {
                    question = row[0];
                    answer = row[1];
                    if(!question.isEmpty() && !answer.isEmpty())
                        datas.add(new DBDataAIObject("0",question, answer));
                }

            }
//            XSSFWorkbook wb= new XSSFWorkbook(inputStream);
//            //Workbook wb=Workbook.getWorkbook(excelData);
//            XSSFSheet mySheet=wb.getSheetAt(0);
//            Iterator<Row> rowIterator = mySheet.iterator();
//            while (rowIterator.hasNext()) {
//
//                Row row = rowIterator.next();
//                Iterator<Cell> cellIterator = row.cellIterator();
//
//                int colno =0;
//                String question = "";
//                String answer = "";
//                while (cellIterator.hasNext()) {
//                    Cell cell = cellIterator.next();
//                    if(colno==0)
//                        question = cell.toString();
//                    else if (colno ==1)
//                        answer = cell.toString();
//                }
//                if(!question.isEmpty() && !answer.isEmpty())
//                    datas.add(new QuestionAnswerObject(question, answer));
//            }
            inputStream.close();
        }

        catch (Exception e){
            System.out.println(e.toString());
            Toast.makeText(context, R.string.openFileError,Toast.LENGTH_LONG);
        }
        return  datas;
    }
}
