package com.example.assistant.DBManager.Objects;

import androidx.annotation.Nullable;

public class DBKeyObject {
    private String id;
    private String key;
    private String value;

    public DBKeyObject(@Nullable String id, String key, String value){
        setId(id);
        setKey(key);
        setValue(value);
    }
    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
