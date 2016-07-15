package org.msh.etbm.db.entities;


import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "errorlog")
public class ErrorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date errorDate;

    @Column(length = 100)
    private String exceptionClass;

    @Column(length = 500)
    private String exceptionMessage;

    @Column(length = 150)
    private String url;

    @Column(length = 100)
    private String userName;

    private UUID userId;

    @Lob
    private String stackTrace;

    @Column(length = 100)
    private String workspace;

    @Lob
    private String request;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the exceptionClass
     */
    public String getExceptionClass() {
        return exceptionClass;
    }

    /**
     * @param exceptionClass the exceptionClass to set
     */
    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the user
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param user the user to set
     */
    public void setUserName(String user) {
        this.userName = userName;
    }

    /**
     * @return the userId
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    /**
     * @return the stackTrace
     */
    public String getStackTrace() {
        return stackTrace;
    }

    /**
     * @param stackTrace the stackTrace to set
     */
    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    /**
     * @return the exceptionMessage
     */
    public String getExceptionMessage() {
        return exceptionMessage;
    }

    /**
     * @param exceptionMessage the exceptionMessage to set
     */
    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    /**
     * @return the workspace
     */
    public String getWorkspace() {
        return workspace;
    }

    /**
     * @param workspace the workspace to set
     */
    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    /**
     * @return the errorDate
     */
    public Date getErrorDate() {
        return errorDate;
    }

    /**
     * @param errorDate the errorDate to set
     */
    public void setErrorDate(Date errorDate) {
        this.errorDate = errorDate;
    }


    /**
     * @return the request
     */
    public String getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(String request) {
        this.request = request;
    }
}
