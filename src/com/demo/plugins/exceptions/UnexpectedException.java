package com.demo.plugins.exceptions;

public class UnexpectedException
        extends Exception {
    private String m_exceptionInfo = null;

    public UnexpectedException(String string) {
        this.m_exceptionInfo = string;
    }

    public String getMessage() {
        return this.m_exceptionInfo;
    }
}

