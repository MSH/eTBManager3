package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.PropertyLog;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * Created by rmemoria on 9/1/17.
 */
@Entity
@Table(name = "cacheddata")
public class CachedData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 250)
    @NotNull
    private String entryId;

    @Column(length = 250)
    @NotNull
    private String method;

    @Column(length = 40)
    @NotNull
    private String hash;

    @Lob
    @NotNull
    private String args;

    @NotNull
    private String argsClass;

    @NotNull
    private Date entryDate;

    private Date expiryDate;

    @Lob
    @NotNull
    private String data;

    @NotNull
    @Column(length = 250)
    private String dataClass;

    /**
     * The workspace of this entity
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKSPACE_ID")
    private Workspace workspace;


    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataClass() {
        return dataClass;
    }

    public void setDataClass(String dataClass) {
        this.dataClass = dataClass;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getArgsClass() {
        return argsClass;
    }

    public void setArgsClass(String argsClass) {
        this.argsClass = argsClass;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }
}
