package com.example.demo.common;

import java.util.HashMap;

public class Result {
    private boolean status;
    private String message;
    private Object data;

    public Result() {}

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static <T> Result success(T data) {
        Result r = new Result();
        r.status = true;
        r.message = "success";
        r.data = data;
        return r;
    }

    public static Result success() {
        return success(new HashMap<>());
    }

    public static Result error(String message) {
        Result r = new Result();
        r.status = false;
        r.message = message;
        r.data = new HashMap<>();
        return r;
    }

    public static Result error() {
        return error("error");
    }
}