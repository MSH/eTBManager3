package org.msh.etbm.db.entities;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Store information about an exam request registered in the laboratory
 *
 * Created by ricardo on 13/08/14.
 */
@Entity
@Table(name = "examrequest")
public class ExamRequest {

    @Id
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    private TbCase tbcase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private Tbunit tbunit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laboratory_id")
    @NotNull
    private Laboratory laboratory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @OneToMany(mappedBy = "request")
    private List<ExamMicroscopy> examsMicroscopy = new ArrayList<ExamMicroscopy>();

    @OneToMany(mappedBy = "request")
    private List<ExamCulture> examsCulture = new ArrayList<ExamCulture>();

    @OneToMany(mappedBy = "request")
    private List<ExamDST> examsDST = new ArrayList<ExamDST>();

    @OneToMany(mappedBy = "request")
    private List<ExamXpert> examsXpert = new ArrayList<ExamXpert>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public TbCase getTbcase() {
        return tbcase;
    }

    public void setTbcase(TbCase tbcase) {
        this.tbcase = tbcase;
    }

    public Tbunit getTbunit() {
        return tbunit;
    }

    public void setTbunit(Tbunit tbunit) {
        this.tbunit = tbunit;
    }

    public List<ExamMicroscopy> getExamsMicroscopy() {
        return examsMicroscopy;
    }

    public void setExamsMicroscopy(List<ExamMicroscopy> examsMicroscopy) {
        this.examsMicroscopy = examsMicroscopy;
    }

    public List<ExamCulture> getExamsCulture() {
        return examsCulture;
    }

    public void setExamsCulture(List<ExamCulture> examsCulture) {
        this.examsCulture = examsCulture;
    }

    public List<ExamDST> getExamsDST() {
        return examsDST;
    }

    public void setExamsDST(List<ExamDST> examsDST) {
        this.examsDST = examsDST;
    }

    public List<ExamXpert> getExamsXpert() {
        return examsXpert;
    }

    public void setExamsXpert(List<ExamXpert> examsXpert) {
        this.examsXpert = examsXpert;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Laboratory getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
    }
}
