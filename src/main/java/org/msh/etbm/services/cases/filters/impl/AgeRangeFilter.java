package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.filters.FilterTypes;
import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.msh.etbm.services.admin.ageranges.AgeRangeData;
import org.msh.etbm.services.admin.ageranges.AgeRangeService;
import org.msh.etbm.services.admin.ageranges.AgeRangesQueryParams;
import org.msh.etbm.services.cases.filters.CaseFilters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rmemoria on 23/10/16.
 */
public class AgeRangeFilter extends AbstractFilter {

    private List<AgeRangeData> ageRanges;


    public AgeRangeFilter() {
        super(CaseFilters.AGE_RANGE, "${AgeRange}");
    }

    @Override
    public void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params) {
        StringBuilder restrict = new StringBuilder();
        List vals = new ArrayList();

        iterateValues(value, val -> {
            AgeRangeData ar = ageRangeById(val.toString());
            vals.add(ar.getIniAge());
            vals.add(ar.getEndAge());

            String sep = restrict.length() > 0 ? " or " : "";
            restrict.append(sep).append("(tbcase.age between ? and ?)");
        });

        def.restrict("(" + restrict.toString() + ")", vals.toArray());
    }


    @Override
    public String getFilterType() {
        return FilterTypes.MULTI_SELECT;
    }


    /**
     * Search for an age range by a given age
     * @param age
     * @return
     */
    protected AgeRangeData ageRangeByAge(int age) {
        for (AgeRangeData ar: getAgeRanges()) {
            if (age >= ar.getIniAge() && age <= ar.getEndAge()) {
                return ar;
            }
        }
        return null;
    }


    protected AgeRangeData ageRangeById(String id) {
        for (AgeRangeData ar: getAgeRanges()) {
            if (ar.getId().toString().equals(id)) {
                return ar;
            }
        }
        return null;
    }

    /**
     * Return the list of age ranges
     * @return
     */
    protected List<AgeRangeData> getAgeRanges() {
        if (ageRanges == null) {
            AgeRangeService srv = getApplicationContext().getBean(AgeRangeService.class);
            QueryResult<AgeRangeData> res = srv.findMany(new AgeRangesQueryParams());
            ageRanges = res.getList();
        }

        return ageRanges;
    }

    @Override
    public void prepareVariableQuery(QueryDefs def, int iteration) {
        def.select("tbcase.age");
    }

    @Override
    public String createKey(Object values) {
        if (values == null) {
            return AbstractFilter.KEY_NULL;
        }

        // get the age
        int age = (Integer)values;

        // find the range
        AgeRangeData ageRange = ageRangeByAge(age);

        return ageRange != null ? ageRange.getId().toString() : AbstractFilter.KEY_NULL;
    }

    @Override
    public String getKeyDisplay(String key) {
        String s = super.getKeyDisplay(key);
        if (s != null) {
            return s;
        }

        AgeRangeData ageRange = ageRangeById(key.toString());
        return ageRange != null ? ageRange.getName() : super.getKeyDisplay(null);
    }

    @Override
    public List<Item> getOptions() {
        List<Item> options = new ArrayList<>();

        for (AgeRangeData ageRange: getAgeRanges()) {
            options.add(new Item(ageRange.getId().toString(), ageRange.getName()));
        }

        return options;
    }
}
