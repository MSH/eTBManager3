package org.msh.etbm.db;

import org.hibernate.annotations.GenericGenerator;
import org.msh.etbm.db.entities.TbCase;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * A supper entity class that is under a TB case
 * <p>
 * Created by rmemoria on 11/10/15.
 */
@MappedSuperclass
public class CaseEntity extends Synchronizable {

    /**
     * The case related to this data
     */
    @ManyToOne
    @JoinColumn(name = "CASE_ID")
    @NotNull
    private TbCase tbcase;

    public TbCase getTbcase() {
        return tbcase;
    }

    public void setTbcase(TbCase tbcase) {
        this.tbcase = tbcase;
    }
}
