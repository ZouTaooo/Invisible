package com.example.invisible.Bean;

public class Basebean<T> {

    /**
     * body : {"token":"5c315260ee080d960e90c88862361a0722c87f07"}
     * msg : 成功
     * status : 1
     */

    private T body;
    private String msg;
    private int status;

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
