package com.example.assistant.FileManager;

public class QuestionAnswerObject {
    String question = "";
    String answer = "";

    public QuestionAnswerObject(String question, String answer){
        setAnswer(answer);
        setQuestion(question);
    }
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
