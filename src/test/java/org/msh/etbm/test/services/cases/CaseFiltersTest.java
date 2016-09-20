package org.msh.etbm.test.services.cases;

import org.junit.Test;
import org.msh.etbm.commons.filters.FilterData;
import org.msh.etbm.commons.filters.FilterGroupData;
import org.msh.etbm.services.cases.filters.CaseFilters;
import org.msh.etbm.test.AuthenticatedTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;


/**
 * Created by rmemoria on 19/9/16.
 */
public class CaseFiltersTest extends AuthenticatedTest {

    @Autowired
    CaseFilters caseFilters;

    /**
     * Check the consistency of the filters returned to the client
     */
    @Test
    public void checkFilterConsistency() {
        List<FilterGroupData> lst = caseFilters.getFiltersData();

        assertNotNull(lst);
        assertTrue(lst.size() > 0);

        for (FilterGroupData grp: lst) {
            assertNotNull(grp.getLabel());

            // check if it is being translated
            assertFalse(grp.getLabel().contains("${"));

            assertNotNull(grp.getFilters());
            assertTrue(grp.getFilters().size() > 0);

            for (FilterData filter: grp.getFilters()) {
                assertNotNull(filter.getLabel());
                assertFalse(filter.getLabel().contains("${"));
                assertNotNull(filter.getId());
                assertNotNull(filter.getType());
            }
        }
    }
}
