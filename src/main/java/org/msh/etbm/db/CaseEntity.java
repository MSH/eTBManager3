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
public class CaseEntity {

    /**
     * The table primary key
     */
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(name = "uuid2", strategy = "uuid2", parameters = {@org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
    private UUID id;


    /**
     * The case related to this data
     */
    @ManyToOne
    @JoinColumn(name = "CASE_ID")
    @NotNull
    private TbCase tbcase;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TbCase getTbcase() {
        return tbcase;
    }

    public void setTbcase(TbCase tbcase) {
        this.tbcase = tbcase;
    }
}
