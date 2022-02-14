package com.example.assistant;

public class MessageObject {
    private int messageID = 0 ;
    private int chatOrder= 0;
    private int chatID = 0;
    private  int UserID =0;
    String messageContent = "";
    Boolean IsSeen = false;
    Boolean IsReceived = false;
    String ChatTime = "";
    public MessageObject( int messageID, int chatOrder, int chatID, int UserID, String messageContent ,
              Boolean IsSeen, Boolean IsReceived, String ChatTime){
    }
    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setChatOrder(int chatOrder) {
        this.chatOrder = chatOrder;
    }

    public int getChatOrder() {
        return chatOrder;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }

    public int getChatID() {
        return chatID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setSeen(Boolean seen) {
        IsSeen = seen;
    }

    public Boolean getSeen() {
        return IsSeen;
    }

    public void setReceived(Boolean received) {
        IsReceived = received;
    }

    public Boolean getReceived() {
        return IsReceived;
    }

    public void setChatTime(String chatTime) {
        ChatTime = chatTime;
    }

    public String getChatTime() {
        return ChatTime;
    }
}
