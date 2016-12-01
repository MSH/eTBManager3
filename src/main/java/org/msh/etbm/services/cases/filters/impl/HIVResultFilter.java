package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.filters.FilterException;
import org.msh.etbm.commons.filters.FilterTypes;
import org.msh.etbm.commons.indicators.variables.VariableOptions;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.msh.etbm.db.enums.HIVResult;
import org.msh.etbm.services.cases.filters.CaseFilters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Standard filter for HIV results
 *
 * Created by rmemoria on 23/10/16.
 */
public class HIVResultFilter extends AbstractFilter {

    public HIVResultFilter() {
        super(CaseFilters.HIV_RESULT, "${cases.hivresult}");
    }

    @Override
    public void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params) {
        iterateValues(value, val -> def.restrict(hivRestriction(val)));
    }

    @Override
    public void prepareVariableQuery(QueryDefs def, int iteration) {
        HIVResult res;
        switch (iteration) {
            case 0: res = HIVResult.POSITIVE;
                break;

            case 1: res = HIVResult.NEGATIVE;
                break;

            case 2: res = HIVResult.NOTDONE;
                break;

            default:
                throw new IllegalArgumentException("Unexpected iteration " + iteration);
        }

        def.select("'" + Integer.toString(res.ordinal() + 1) + "'");

        String restriction = hivRestriction(res.toString());
        def.restrict(restriction);
    }

    private String hivRestriction(Object val) {
        HIVResult res = keyToHivResult(val);

        switch (res) {
            case POSITIVE: // POSITIVE
                return "exists (select * from examhiv where examhiv.result=0 and examhiv.case_id = tbcase.id)";

            case NEGATIVE: // NEGATIVE
                return "exists (select * from examhiv where examhiv.result=1 and examhiv.case_id = tbcase.id) and " +
                        "not exists (select * from examhiv where examhiv.result=0 and examhiv.case_id = tbcase.id)";

            case NOTDONE: // NOT DONE
                return "not exists (select * from examhiv where examhiv.result in (0, 1) and examhiv.case_id = tbcase.id)";

            default:
                throw new FilterException("Value not supported: " + val);
        }
    }

    @Override
    public String createKey(Object value) {
        if (value == null) {
            return super.createKey(value);
        }
        return value.toString();
    }

    @Override
    public List<Item> getOptions() {
        List<Item> options = new ArrayList<>();
        Messages msgs = getMessages();

        HIVResult[] vals = {
                HIVResult.NOTDONE,
                HIVResult.NEGATIVE,
                HIVResult.POSITIVE
        };

        for (HIVResult val: vals) {
            options.add(new Item(val.toString(), msgs.get(val.getMessageKey())));
        }

        return options;
    }

    private HIVResult keyToHivResult(Object key) {
        return ObjectUtils.stringToEnum((String)key, HIVResult.class);
    }

    @Override
    public String getFilterType() {
        return FilterTypes.SELECT;
    }

    @Override
    public VariableOptions getVariableOptions() {
        VariableOptions opt = super.getVariableOptions();
        return new VariableOptions(opt.isGrouped(), opt.isTotalEnabled(), 3, opt.getCountingUnit());
    }

    @Override
    public String getKeyDisplay(String key) {
        if (key == null) {
            return getMessages().get(Messages.UNDEFINED);
        }

        int index = Integer.parseInt(key.toString());
        HIVResult res = HIVResult.values()[index - 1];

        return getMessages().get(res.getMessageKey());
    }
}
