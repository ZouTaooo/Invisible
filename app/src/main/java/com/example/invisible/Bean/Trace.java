package com.example.invisible.Bean;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;

public class Trace extends RealmObject{
    /**
     * date : 201809141912
     * type : 日迹
     * content : content1
     */
    public String date;
    public String type;
    public String content;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
