package org.msh.etbm.test.services.admin;

import org.junit.Test;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.db.entities.Medicine;
import org.msh.etbm.db.enums.MedicineCategory;
import org.msh.etbm.db.enums.MedicineLine;
import org.msh.etbm.services.admin.products.ProductDetailedData;
import org.msh.etbm.services.admin.products.ProductFormData;
import org.msh.etbm.services.admin.products.ProductService;
import org.msh.etbm.services.admin.products.ProductType;
import org.msh.etbm.services.admin.substances.SubstanceFormData;
import org.msh.etbm.services.admin.substances.SubstanceService;
import org.msh.etbm.test.services.CommonEntityServiceTests;
import org.msh.etbm.test.services.TestResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Test of CRUD operations in {@link org.msh.etbm.services.admin.products.ProductService}
 *
 * Created by rmemoria on 9/3/16.
 */
public class MedicineTest extends CommonEntityServiceTests {

    @Autowired
    ProductService productService;

    @Autowired
    SubstanceService substanceService;

    private List<UUID> substances;

    /**
     * Single constructor passing the classes to be used during tests
     *
     */
    public MedicineTest() {
        super(Medicine.class, ProductFormData.class, ProductDetailedData.class);
    }

    @Test
    public void testMedicineCRUD() {
        setEntityService(productService);

        createSubstances();
        UUID toAdd = substances.get(0);
        substances.remove(0);

        // test create and find one
        Map<String, Object> props = new HashMap<>();
        props.put("type", ProductType.MEDICINE);
        props.put("name", "My medicine");
        props.put("shortName", "MS");
        props.put("substances", substances);
        props.put("active", true);
        props.put("line", MedicineLine.FIRST_LINE);
        props.put("category", MedicineCategory.INJECTABLE);

        // unique properties
        List<String> lst = new ArrayList<>();
        lst.add("name");
        lst.add("shortName");

        TestResult res = testCreateAndFindOne(props, lst, null);

        // test update
        props.clear();
        props.put("name", "My medicine 2");
        props.put("shortName", "MED 2");

        List<UUID> subs = new ArrayList<>(substances);
        subs.remove(0);
        subs.add(toAdd);
        props.put("substances", subs);

        testUpdate(res.getId(), props);

        // test delete
        testDelete(res.getId());
    }


    /**
     * Create a list of substances
     */
    protected void createSubstances() {
        substances = new ArrayList<>();

        for (int i = 1; i < 5; i++) {
            SubstanceFormData s = new SubstanceFormData();
            s.setName(Optional.of("Substance " + i));
            s.setShortName(Optional.of("Sub " + i));
            s.setDisplayOrder(Optional.of(1));
            s.setLine(Optional.of(MedicineLine.FIRST_LINE));

            ServiceResult res = substanceService.create(s);

            substances.add(res.getId());
        }
    }

}
