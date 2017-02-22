package org.msh.etbm.test.services.cases;

import org.junit.Test;
import org.msh.etbm.commons.filters.FilterGroupData;
import org.msh.etbm.services.cases.search.CaseSearchInitResponse;
import org.msh.etbm.services.cases.search.CaseSearchService;
import org.msh.etbm.test.AuthenticatedTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by rmemoria on 17/9/16.
 */
public class CaseSearchTest extends AuthenticatedTest {

    @Autowired
    CaseSearchService caseSearchService;

    @Test
    public void initFilters() {
        CaseSearchInitResponse resp = caseSearchService.init();

        assertNotNull(resp);
        assertNotNull(resp.getFilters());
        assertTrue(resp.getFilters().size() > 0);

        for (FilterGroupData grp: resp.getFilters()) {
            assertNotNull(grp.getLabel());
        }
    }
}
