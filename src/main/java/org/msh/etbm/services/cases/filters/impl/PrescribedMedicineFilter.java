package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.filters.FilterTypes;
import org.msh.etbm.commons.indicators.keys.Key;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.msh.etbm.services.admin.products.ProductQueryParams;
import org.msh.etbm.services.admin.products.ProductService;
import org.msh.etbm.services.admin.products.ProductType;
import org.msh.etbm.services.cases.filters.CaseFilters;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Filter to restrict the cases from a set of prescribed medicine, and a variable
 * to display the number of cases by prescribed medicine
 *
 * Created by rmemoria on 23/12/16.
 */
public class PrescribedMedicineFilter extends AbstractFilter {

    private List<Item> medicines;

    public PrescribedMedicineFilter() {
        super(CaseFilters.PRESC_MEDICINE, "${PrescribedMedicine}");
    }

    /**
     * Return the list of medicines available in the workspace
     * @return list of {@link Item} objects
     */
    public List<Item> getMedicines() {
        if (medicines == null) {
            ProductService service = getApplicationContext().getBean(ProductService.class);

            ProductQueryParams p = new ProductQueryParams();
            p.setType(ProductType.MEDICINE);
            QueryResult<Item> res = service.findMany(p);
            medicines = res.getList();
        }
        return medicines;
    }

    @Override
    public void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params) {
        def.join("prescribedmedicine");
        addValuesRestriction(def, "prescribedmedicine.medicine_id", value, it -> it);
    }

    @Override
    public String getFilterType() {
        return FilterTypes.MULTI_SELECT;
    }

    @Override
    public List<Item> getOptions() {
        return getMedicines();
    }

    @Override
    public void prepareVariableQuery(QueryDefs def, int iteration) {
        def.join("prescribedmedicine").select("product_id");

        def.restrict("prescribedmedicine.id = (select min(pm1.id) from prescribedmedicine pm1 " +
                "where pm1.product_id = prescribedmedicine.product_id " +
                "and pm1.case_id = tbcase.id)");
    }

    @Override
    public Key createKey(Object[] values, int iteration) {
        if (values[0] == null) {
            return Key.asNull();
        }

        UUID id = ObjectUtils.bytesToUUID((byte[])values[0]);
        return Key.of(id);
    }

    @Override
    public boolean isTotalEnabled() {
        return false;
    }

    @Override
    public String getKeyDisplay(Key key) {
        // if it's null, return undefined
        if (key.isNull()) {
            return super.getKeyDisplay(key);
        }

        for (Item med: getMedicines()) {
            if (med.getId().equals(key.getValue()))
                return med.getName();
        }

        return super.getKeyDisplay(key);
    }
}
