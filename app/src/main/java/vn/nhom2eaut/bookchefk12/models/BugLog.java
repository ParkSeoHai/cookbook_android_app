package vn.nhom2eaut.bookchefk12.models;

import java.util.Date;

public class BugLog {
    private Date timestamp;
    private String message;
    private String stackTrace;
    private String contextInfo;

    public BugLog() {}

    public BugLog(Date timestamp, String message, String stackTrace, String contextInfo) {
        this.timestamp = timestamp;
        this.message = message;
        this.stackTrace = stackTrace;
        this.contextInfo = contextInfo;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getContextInfo() {
        return contextInfo;
    }

    public void setContextInfo(String contextInfo) {
        this.contextInfo = contextInfo;
    }
}
