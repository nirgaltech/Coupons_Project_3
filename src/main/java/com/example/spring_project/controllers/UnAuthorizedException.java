package com.example.spring_project.controllers;

public class UnAuthorizedException extends Exception {
    public UnAuthorizedException(String s) {
        super(s);
    }
}
