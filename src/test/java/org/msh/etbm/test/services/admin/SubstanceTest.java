package org.msh.etbm.test.services.admin;

import org.junit.Test;
import org.msh.etbm.db.entities.Substance;
import org.msh.etbm.db.enums.MedicineLine;
import org.msh.etbm.services.admin.substances.SubstanceData;
import org.msh.etbm.services.admin.substances.SubstanceFormData;
import org.msh.etbm.services.admin.substances.SubstanceService;
import org.msh.etbm.test.services.CommonEntityServiceTests;
import org.msh.etbm.test.services.TestResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rmemoria on 8/2/16.
 */
public class SubstanceTest extends CommonEntityServiceTests {

    @Autowired
    SubstanceService substanceService;

    /**
     * Single constructor passing the classes to be used during tests
     *
     */
    public SubstanceTest() {
        super(Substance.class, SubstanceFormData.class, SubstanceData.class);
    }

    @Test
    public void executeCrudTest() {
        setEntityService(substanceService);

        // test create and find one
        Map<String, Object> props = new HashMap<>();
        props.put("name", "Rifampicin");
        props.put("shortName", "R");
        props.put("line", MedicineLine.FIRST_LINE);
        props.put("prevTreatmentForm", true);
        props.put("dstResultForm", true);
        props.put("displayOrder", 1);

        // unique properties
        List<String> lst = new ArrayList<>();
        lst.add("name");
        lst.add("shortName");

        TestResult result = testCreateAndFindOne(props, lst, null);

        // test update
        props.clear();
        props.put("name", "Rifampicin 2");

        testUpdate(result.getId(), props);

        // test delete
        testDelete(result.getId());
    }
}
