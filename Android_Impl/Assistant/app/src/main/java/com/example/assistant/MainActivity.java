package com.example.assistant;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alan.alansdk.AlanConfig;
import com.alan.alansdk.button.AlanButton;
import com.example.assistant.FileManager.DBDataAIObject;
import com.example.assistant.FileManager.DBKeyObject;
import com.example.assistant.FileManager.Functions;
import com.example.assistant.FileManager.QuestionAnswerObject;
import com.example.assistant.FileManager.ReadExcelFile;
import com.example.assistant.FileManager.SQLiteDBDataAIManager;
import com.example.assistant.FileManager.SQLiteDBKeyManager;

import java.io.File;
import java.util.ArrayList;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

public class MainActivity extends AppCompatActivity {
    ChatView chatView;
    ChatBot chatBot;
    File dbpath;
    EditText newFolder;
    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chatView = (ChatView) findViewById(R.id.chat_view);
        //Context ctx = this;// for Activity, or Service. Otherwise simply get the context.
        //String dbname = ContantsDefine.DATABASE_NAME;
        //dbpath = ctx.getDatabasePath(dbname);
        //ChatBot.DATABASE = dbpath.getAbsolutePath();
        init();
        ImageView imageButton;
        AlanButton alanButton = (AlanButton)findViewById(R.id.alan_button);

        AlanConfig alanConfig = AlanConfig.builder()
                .setProjectId("314203787ccd9370974f1bf6b6929c1b2e956eca572e1d8b807a3e2338fdd0dc/prod")
                .build();
        alanButton.initWithConfig(alanConfig);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chatbot_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.importFile:
                OpenSettingFolderPath(this);
                return true;
            case R.id.deleteDataBase:
                return true;
            case R.id.help:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Fix no activity available
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        switch (requestCode) {
            case ContantsDefine.REQUEST_GET_EXCEL_FILE_PATH:
                if (resultCode == RESULT_OK) {
                    String FilePath = data.getData().toString();
                    String path = FilePath;
                    newFolder.setText(FilePath);
                    //FilePath is your file as a string
                }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    private void init() {
        chatView = (ChatView) findViewById(R.id.chat_view);
        try{
            chatBot = new ChatBot(this.getApplicationContext());
            chatView.addMessage(new ChatMessage(ChatBot.getBotIntro(), System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
            addListeners();
        }catch (Exception e){
            chatView.addMessage(new ChatMessage(ChatBot.getSystemError()+"\n\n"+e.getMessage(), System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
        }
    }

    private void addListeners(){
        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener(){
            @Override
            public boolean sendMessage(ChatMessage chatMessage){
                System.out.println(chatMessage.getMessage());
                MessageProcessor messageProcessor = new MessageProcessor(chatView,chatBot);
                if(!ChatBot.readyToLearnSomethingNew) {
                    messageProcessor.execute(chatMessage.getMessage());
                }else{
                    messageProcessor.execute(ChatBot.question,chatMessage.getMessage());
                }
                return true;
            }
        });

        chatView.setTypingListener(new ChatView.TypingListener(){
            @Override
            public void userStartedTyping(){
                // will be called when the user starts typing

            }

            @Override
            public void userStoppedTyping(){
                // will be called when the user stops typing

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            chatBot.checkExit("exit");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void OpenSettingFolderPath(Activity context){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.setting_foder_path);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        ImageView OpenSettingFolder = (ImageView) dialog.findViewById(R.id.OpenSettingFolder);


        newFolder = dialog.findViewById(R.id.newFolderConfirm);
        String excePath = SQLiteDBKeyManager.GetInstance(context).GetValueByKey(ContantsDefine.EXCEL_FOLDER_PATH);
        newFolder.setText(excePath);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newFolderValue = newFolder.getText().toString();
                DBKeyObject dbKeyObject = new DBKeyObject(ContantsDefine.EXCEL_FOLDER_PATH,ContantsDefine.EXCEL_FOLDER_PATH, newFolderValue );
                SQLiteDBKeyManager.GetInstance(context).addKey(dbKeyObject);
                ReadDataFromExcelFile(context);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        OpenSettingFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.ScanFolder(context);
            }
        });
        dialog.show();
    }
    private void ReadDataFromExcelFile(Activity context){
        String excelFilePath = SQLiteDBKeyManager.GetInstance(context).GetValueByKey(ContantsDefine.EXCEL_FOLDER_PATH);
        ArrayList<DBDataAIObject> datasFromExcelFile = ReadExcelFile.ReadExcelFile(context, excelFilePath);
        SQLiteDBDataAIManager.GetInstance(context).addKeys(datasFromExcelFile);
    }
}
