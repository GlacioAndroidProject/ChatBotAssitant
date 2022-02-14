package com.example.assistant;
import android.content.Context;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.util.*;
import java.text.*;

import java.util.Locale;
/*
import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
*/
public class ChatBot
{
    private TreeMap<String,String> quesResponseMap=null;
    private TreeMap<String,String> localQuesResponseMap=null;
    private Set<String> questions=null;
    private Collection<String> answers=null;
    private ArrayList<MessageObject> messageObjects = null;
    private int chatID = 0;
    private int messageIDCount = 0;
    private String botAnswer="";
    private String chatHistory="";
    private String username="";

    //static variables
    public static Boolean readyToLearnSomethingNew = false;
    public static String question = "";
    public Context context= null;

    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }


    public static String getBotIntro() {
        return ContantsDefine.BOT_INTRO;
    }

    public static String getSorryResponse() {
        return ContantsDefine.SORRY_RESPONSE;
    }

    public static String getSystemError() {
        return ContantsDefine.SYSTEM_ERROR;
    }

    public static String getLearnRequest() {
        return ContantsDefine.LEARN_REQUEST;
    }

    public static String getEXIT() {
        return ContantsDefine.EXIT;
    }

    public static String getDATABASE() {
        return ContantsDefine.DATABASE;
    }

    public static String getBotName() {
        return ContantsDefine.BOT_NAME;
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public ChatBot(@Nullable Context context) throws Exception
    {
        this.context = context;
        this.messageObjects = new ArrayList<>();
        this.quesResponseMap=KnowledgeBase.getKnowledgeBase(this.context);

        System.out.println("========="+this.quesResponseMap);
        this.localQuesResponseMap = new TreeMap<String,String>();
        if(this.quesResponseMap == null)
            this.botAnswer=ContantsDefine.SORRY_RESPONSE ;
        else
        {
            questions = quesResponseMap.keySet();
            answers = quesResponseMap.values();
        }
    }

    public TreeMap<String, String> getQuesResponseMap() {
        return quesResponseMap;
    }

    public void setQuesResponseMap(TreeMap<String, String> quesResponseMap) {
        this.quesResponseMap = quesResponseMap;
        questions = quesResponseMap.keySet();
        answers = quesResponseMap.values();
    }

    public String saveQuestionAndResponse(String question , String response)
    {
        if(!question.trim().isEmpty() && !response.trim().isEmpty()) {
            this.quesResponseMap.put(question,response);
            this.localQuesResponseMap.put(question,response);
        }
        this.botAnswer="Vâng. Chúng ta bắt đầu lại nhé!";
        return this.botAnswer;
    }

    public String getResponse(String question)
    {
        double globalRank=0.400;
        String localQuestion="";
        try{
            for(String ques:questions)
            {
                double localRank=StringSimilarity.similarity(ques,question);

                if(globalRank <localRank)
                {
                    globalRank = localRank;
                    localQuestion=ques;
                }
            }

            if(localQuestion.trim()!=""){
                this.botAnswer=quesResponseMap.get(localQuestion);
                if(this.botAnswer.trim().toLowerCase().contains("timedate")) {this.botAnswer = new Date().toString();}
            }
            else {
                this.botAnswer = ContantsDefine.SORRY_RESPONSE + " " + ContantsDefine.LEARN_REQUEST;
                readyToLearnSomethingNew = true;
                ChatBot.question = question;
            }
        }catch(Exception e)
        {
            this.botAnswer=ContantsDefine.SYSTEM_ERROR;
        }
        return this.botAnswer;
    }


    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public void checkExit(String exitStmt) throws Exception
    {
        if(exitStmt.toLowerCase().contains("exit"))
        {
            this.chatHistory += ContantsDefine.BOT_NAME+ContantsDefine.EXIT;
            log(ContantsDefine.BOT_NAME+ContantsDefine.EXIT );
            speak(ContantsDefine.EXIT);
            KnowledgeBase.saveNewKnowledge(this.localQuesResponseMap, this.context);
            KnowledgeBase.saveLogs(this.chatHistory,this.username);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public static void main(String arg[]) throws Exception{
        Scanner scanner= new Scanner(System.in);
        ChatBot chatBot= new ChatBot(null);
        log(ContantsDefine.BOT_INTRO);
        speak(ContantsDefine.BOT_INTRO);
        chatBot.username=scanner.nextLine();
        chatBot.chatHistory += "\n\n"+ContantsDefine.BOT_NAME+"Hi "+chatBot.username+". How can I help you?\r\n";
        log("\n\n"+ContantsDefine.BOT_NAME+"Hi "+chatBot.username+". How can I help you?");
        speak("Hi "+chatBot.username+". How can I help you?");
        while(true)
        {
            System.out.print(chatBot.username+" : ");
            String question = scanner.nextLine();
            chatBot.chatHistory += chatBot.username+" : "+question+"\r\n";
            chatBot.checkExit(question.trim());
            String response = chatBot.getResponse(question);
            chatBot.chatHistory += ContantsDefine.BOT_NAME+response+"\r\n";
            log(ContantsDefine.BOT_NAME+response);
            speak(response.trim());
            if(response.toLowerCase().contains("sorry"))
            {
                log(chatBot.username+" : ");
                response = scanner.nextLine();
                chatBot.chatHistory += chatBot.username+" : "+response;
                chatBot.checkExit(response.trim());
                chatBot.chatHistory += ContantsDefine.BOT_NAME+chatBot.saveQuestionAndResponse(question,response)+"\r\n";
                String res = chatBot.saveQuestionAndResponse(question,response);
                log(ContantsDefine.BOT_NAME+res);
                speak(res);
            }

        }
    }

    private static void log(String content){
        System.out.println(content);
    }

    public static void speak(String content)
    {
        try{
       //     textSpeech.speak(content);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


}