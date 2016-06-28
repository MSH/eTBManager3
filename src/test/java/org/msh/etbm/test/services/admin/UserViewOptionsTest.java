package org.msh.etbm.test.services.admin;

import org.junit.Test;
import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.db.enums.UserView;
import org.msh.etbm.services.admin.admunits.parents.AdminUnitSeries;
import org.msh.etbm.services.admin.units.UnitQueryParams;
import org.msh.etbm.services.admin.units.UnitService;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.services.admin.usersws.UserViewOptions;
import org.msh.etbm.services.admin.usersws.data.UserViewData;
import org.msh.etbm.test.AuthenticatedTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * Test the {@link UserViewOptions} component (indirectly instantiated by FormServices)
 *
 * Created by rmemoria on 10/2/16.
 */
public class UserViewOptionsTest extends AuthenticatedTest {

    private static final String KEY = "id";

    @Autowired
    FormService formService;

    @Autowired
    UnitService unitService;

    /**
     * Test list of options with no unit selected. It is supposed to return just
     * one item
     */
    @Test
    public void testNoParameters() {
        FormRequest req = new FormRequest(UserViewOptions.CMD_NAME, KEY);

        List<Item> items = process(req);

        assertEquals(items.size(), 1);
        Item item = items.get(0);
        assert(item.getId() instanceof UserViewData);

        UserViewData uv = (UserViewData)item.getId();
        assertEquals(uv.getView().get(), UserView.COUNTRY);
        assertNull(uv.getAdminUnitId());
    }

    @Test
    public void testSelelectedUnit() {
        // get one unit for testing
        UnitQueryParams p = new UnitQueryParams();
        p.setPage(0);
        p.setPageSize(1);
        p.setProfile(UnitQueryParams.PROFILE_DEFAULT);

        QueryResult<UnitData> units = unitService.findMany(p);

        // assert there is at least one single unit
        assert(units.getList().size() > 0);

        UnitData data = units.getList().get(0);

        assert(data instanceof UnitData);
        assert(data.getAdminUnit() instanceof AdminUnitSeries);

        Map<String, Object> params = new HashMap<>();
        params.put(UserViewOptions.PARAM_UNITID, data.getId().toString());

        // create form request
        FormRequest req = new FormRequest(UserViewOptions.CMD_NAME, KEY, params);

        List<Item> items = process(req);

        // at least the country option, admin unit, the unit and option to select other units
        assert(items.size() >= 4);

        // check country option
        Item<UserViewData> item = items.get(0);
        UserViewData id = item.getId();
        assertEquals(id.getView().get(), UserView.COUNTRY);
        assertNull(id.getAdminUnitId());

        // check select unit option
        item = items.get(items.size() - 1);
        id = item.getId();
        assertEquals(id.getView().get(), UserView.SELECTEDUNITS);
        assertNull(id.getAdminUnitId());

        // check unit
        item = items.get(items.size() - 2);
        id = item.getId();
        assertEquals(id.getView().get(), UserView.UNIT);
        assertNull(id.getAdminUnitId());


        // check select unit option
        item = items.get(items.size() - 3);
        id = item.getId();
        assertEquals(id.getView().get(), UserView.ADMINUNIT);
        assertNotNull(id.getAdminUnitId());
    }

    /**
     * Execute the form request
     * @param req the request to be performed
     * @return the expected list of items
     */
    protected List<Item> process(FormRequest req) {
        List<FormRequest> reqs = new ArrayList<>();
        reqs.add(req);

        Map<String, Object> res = formService.processFormRequests(reqs);

        // assert values
        assertNotNull(res);
        assert(res.keySet().size() > 0);
        assertNotNull(res.get(KEY));
        assert(res.get(KEY) instanceof List);

        List<Item> items = (List)res.get(KEY);

        for (Item item: items) {
            assert(item.getId() instanceof UserViewData);
        }

        return (List)res.get(KEY);
    }
}
