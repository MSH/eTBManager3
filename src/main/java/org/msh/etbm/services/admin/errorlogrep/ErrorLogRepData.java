package org.msh.etbm.services.admin.errorlogrep;

import java.util.Date;

/**
 * Created by msantos on 05/7/16.
 */
public class ErrorLogRepData {

    private Date errorDate;
    private String exceptionClass;
    private String exceptionMessage;
    private String url;
    private String userName;
    private String stackTrace;
    private String workspace;
    private String request;

    public ErrorLogRepData (Date errorDate, String exceptionClass, String exceptionMessage, String url, String userName, String stackTrace, String workspace, String request) {
        this.errorDate = errorDate;
        this.exceptionClass = exceptionClass;
        this.exceptionMessage = exceptionMessage;
        this.url = url;
        this.userName = userName;
        this.stackTrace = stackTrace;
        this.workspace = workspace;
        this.request = request;
    }

    public Date getErrorDate() {
        return errorDate;
    }

    public void setErrorDate(Date errorDate) {
        this.errorDate = errorDate;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
