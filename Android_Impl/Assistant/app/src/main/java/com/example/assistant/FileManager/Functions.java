package com.example.assistant.FileManager;

import static com.example.assistant.ContantsDefine.REQUEST_GET_NEW_SNAPSHORT_FOLDER;
import static com.example.assistant.FileManager.ReadExcelFile.GetDefaultSnapshotFolderPath;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.assistant.ContantsDefine;
import com.example.assistant.R;

import java.io.File;

public class Functions {
    public static final int WRITE_EXTENAL_STORAGE = 121;
    int REQUEST_GET_NEW_SNAPSHORT_FOLDER = 100;

    public static boolean checkPermission(Activity mContext){
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED)
            return true;
        else return false;
    }
    public static void requestPermission(Activity context){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                context, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(context, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        }else{
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Functions.WRITE_EXTENAL_STORAGE);
        }
    }

    public static void ScanFolder(Activity context) {

//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, GetDefaultSnapshotFolderPath(context));
//        context.startActivityForResult(intent, ContantsDefine.REQUEST_GET_NEW_SNAPSHORT_FOLDER);

//        Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
//        fileintent.setType("xlsx");
//        try {
//            context.startActivityForResult(fileintent, ContantsDefine.REQUEST_GET_NEW_SNAPSHORT_FOLDER);
//        } catch (ActivityNotFoundException e) {
//
//        }
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        //intent.setType("image/*");
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //intent.setType("gagt/sdf");
        context.startActivityForResult(intent, ContantsDefine.REQUEST_GET_NEW_SNAPSHORT_FOLDER);
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
            folderPath = String.valueOf(Environment.DIRECTORY_DOWNLOADS);//Environment.getExternalStorageDirectory() +"/"+ Environment.DIRECTORY_PICTURES+ "/"+ AppName+"/";
        } else
            folderPath =mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        return  folderPath;
    }
}
