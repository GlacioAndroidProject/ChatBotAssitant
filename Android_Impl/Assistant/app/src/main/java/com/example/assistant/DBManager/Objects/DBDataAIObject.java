package com.example.assistant.DBManager.Objects;

import androidx.annotation.Nullable;

public class DBDataAIObject {
    private String id;
    private String question;
    private String answer;

    public DBDataAIObject(@Nullable String id, String question, String answer){
        setId(id);
        setQuestion(question);
        setAnswer(answer);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
