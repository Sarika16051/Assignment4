package com.example.sarika.todolist;

/**
 * Created by sarika on 11/3/2016.
 */
public class ToDo {

    private String title, detail, time;

    public ToDo() {
    }

    public ToDo(String title, String detail, String time) {
        this.title = title;
        this.detail = detail;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


}
