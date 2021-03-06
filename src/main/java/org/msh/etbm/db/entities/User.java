/*
 * User.java
 *
 * Created on 29 de Janeiro de 2007, 13:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.msh.etbm.db.entities;

import org.hibernate.validator.constraints.Email;
import org.msh.etbm.commons.Displayable;
import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.Synchronizable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Store information about a user of the system
 *
 * @author Ricardo Memoria
 */
@Entity
@Table(name = "sys_user")
public class User extends Synchronizable implements Displayable {

    @Column(length = 30)
    @NotNull
    @PropertyLog(operations = {Operation.NEW})
    private String login;

    @Column(length = 80)
    @NotNull
    @PropertyLog(operations = {Operation.NEW})
    private String name;

    @Column(length = 32)
    @PropertyLog(ignore = true)
    private String password;

    @Column(nullable = false, length = 80)
    @PropertyLog(operations = {Operation.NEW})
    @Email
    private String email;

    @PropertyLog(messageKey = "EntityState.ACTIVE")
    private boolean active;

    private boolean passwordExpired;

    private boolean emailConfirmed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEFAULTWORKSPACE_ID")
    @PropertyLog(operations = {Operation.NEW})
    private UserWorkspace defaultWorkspace;

    @Column(length = 50)
    private String timeZone;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    @PropertyLog(ignore = true)
    private List<UserWorkspace> workspaces = new ArrayList<UserWorkspace>();

    @Column(length = 200)
    @PropertyLog(messageKey = "global.comments")
    private String comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENTUSER_ID")
    private User parentUser;

    @Temporal(TemporalType.TIMESTAMP)
    @PropertyLog(ignore = true)
    private Date registrationDate;

    @Column(length = 30)
    @PropertyLog(messageKey = "form.customId")
    private String customId;

    /**
     * If true, user will receive system messages to his e-mail address
     */
    private boolean sendSystemMessages;

    /**
     * Indicate if the ULA was accepted or not
     */
    private boolean ulaAccepted;

    /**
     * The request token used by the user to change its password when using the forgot password process
     */
    @PropertyLog(ignore = true)
    private String passwordResetToken;

    /**
     * The user mobile number
     */
    @PropertyLog(messageKey = "global.mobile")
    @Pattern(regexp = "(^$|[0-9]{6,14})")
    private String mobile;

    /**
     * The preferred language, used to remember whitch
     */
    @Column(length = 10)
    private String language;

    /**
     * Search the user workspace by the workspace
     *
     * @param workspace instance of the workspace to search for the user workspace
     * @return instance of {@link UserWorkspace}
     */
    public UserWorkspace getUserWorkspaceByWorkspace(Workspace workspace) {
        for (UserWorkspace ws : getWorkspaces()) {
            if (ws.getWorkspace().equals(workspace)) {
                return ws;
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return login + " - " + name;
    }

    /**
     * Return the user login. The login is used to enter in the system
     *
     * @return the user login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Set a new user login
     *
     * @param login new user login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Return the user name
     *
     * @return name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Change the user name
     *
     * @param name new name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the user password hashed using the MD5 algorithm
     *
     * @return the user password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the user password hashed using the MD5 algorithm
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPasswordExpired() {
        return passwordExpired;
    }

    public void setPasswordExpired(boolean passwordExpired) {
        this.passwordExpired = passwordExpired;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public UserWorkspace getDefaultWorkspace() {
        return defaultWorkspace;
    }

    public void setDefaultWorkspace(UserWorkspace defaultWorkspace) {
        this.defaultWorkspace = defaultWorkspace;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public List<UserWorkspace> getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(List<UserWorkspace> workspaces) {
        this.workspaces = workspaces;
    }

    /**
     * @param comment the comment to set
     */
    public void setComments(String comment) {
        this.comments = comment;
    }

    /**
     * @return the comment
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param parentUser the parentUser to set
     */
    public void setParentUser(User parentUser) {
        this.parentUser = parentUser;
    }

    /**
     * @return the parentUser
     */
    public User getParentUser() {
        return parentUser;
    }

    /**
     * @param registrationDate the registrationDate to set
     */
    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * @return the registrationDate
     */
    public Date getRegistrationDate() {
        return registrationDate;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    /**
     * @return the sendSystemMessages
     */
    public boolean isSendSystemMessages() {
        return sendSystemMessages;
    }


    /**
     * @param sendSystemMessages the sendSystemMessages to set
     */
    public void setSendSystemMessages(boolean sendSystemMessages) {
        this.sendSystemMessages = sendSystemMessages;
    }

    public boolean isUlaAccepted() {
        return ulaAccepted;
    }

    public void setUlaAccepted(boolean ulaAccepted) {
        this.ulaAccepted = ulaAccepted;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    @Override
    public String getDisplayString() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
