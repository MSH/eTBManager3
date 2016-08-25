package org.msh.etbm.test.commons.forms;

import org.junit.Test;
import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormRequestService;
import org.msh.etbm.test.AuthenticatedTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rmemoria on 6/2/16.
 */
public class FormRequestServiceTest extends AuthenticatedTest {

    private static final String ID = "sub";

    @Autowired
    FormRequestService formRequestService;

    @Test
    public void testSimpleRequest() {
        List<FormRequest> reqs = new ArrayList<>();

        FormRequest req = new FormRequest();
        req.setCmd("substances");
        req.setId(ID);
        reqs.add(req);

        Map<String, Object> res = formRequestService.processFormRequests(reqs);

        assertNotNull(res);
        assertEquals(res.size(), 1);
        assertNotNull(res.get(ID));
        assert (res.get(ID) instanceof List);

        List<Item> lst = (List<Item>) res.get(ID);
        for (Item item : lst) {
            System.out.println(item);
        }
    }
}
