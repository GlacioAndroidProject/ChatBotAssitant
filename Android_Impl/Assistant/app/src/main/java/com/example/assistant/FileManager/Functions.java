package com.example.assistant.FileManager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.assistant.ContantsDefine;

import java.util.ArrayList;
import java.util.List;

public class Functions {
    public static final int WRITE_EXTENAL_STORAGE = 121;
    int REQUEST_GET_NEW_SNAPSHORT_FOLDER = 100;
    public static String[] permissions = new String[]{
//            Manifest.permission.INTERNET,
//            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
//            Manifest.permission.VIBRATE,
//            Manifest.permission.RECORD_AUDIO,
    };
    public static boolean checkPermission(Activity mContext){

        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(mContext, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
            } else {
                //request for the permission
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                intent.setData(uri);
                mContext.startActivityForResult(intent, ContantsDefine.REQUEST_GET_EXCEL_FILE_PATH);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(mContext, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), Functions.WRITE_EXTENAL_STORAGE);
            return false;
        }
        return true;
    }
    public static boolean RequestReadFilePermission(Activity mContext){

        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        //for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (result != PackageManager.PERMISSION_GRANTED) {
                requestPermission(mContext);
                return false;
            }
        //}
        return true;
    }
    public static void requestPermission(Activity context){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                context, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(context, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        }else{
            ActivityCompat.requestPermissions(context, permissions, Functions.WRITE_EXTENAL_STORAGE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void ScanFolder(Activity context) {

//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        // it would be "*/*".
//        //intent.setType("image/*");
//        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        context.startActivityForResult(intent, ContantsDefine.REQUEST_GET_EXCEL_FILE_PATH);

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                intent.addCategory(Intent.CATEGORY_OPENABLE);
                // it would be "*/*".
                intent.setType("text/*");
                //intent.setType("text/csv");
                context.startActivityForResult(intent, ContantsDefine.REQUEST_GET_EXCEL_FILE_PATH);
            } else {
                checkPermission(context);
            }
        //}
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
