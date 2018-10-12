package com.example.invisible.Bean;

public class Message {
    //layout_flag = 0 =>left layout_flag = 1 =>right
    private int layout_flag;
    private String content;
    private String time;

    public int getLayout_flag() {
        return layout_flag;
    }

    public void setLayout_flag(int layout_flag) {
        this.layout_flag = layout_flag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
