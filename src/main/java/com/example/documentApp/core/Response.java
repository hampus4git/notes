package com.example.documentApp.core;

public class Response<T> {

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String INVALID_INPUT = "invalid input";
    public static final String USERNAME_ALREADY_PRESENT = "username already present";
    public static final String UNAUTHORIZED = "unauthorized";

    private String status;
    private T data;

    public Response(){

    }

    public Response(String status, T data){
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
