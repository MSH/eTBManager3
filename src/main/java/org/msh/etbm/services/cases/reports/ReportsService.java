package org.msh.etbm.services.cases.reports;

import org.msh.etbm.commons.Item;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Basic service to handle reports of the case management module
 *
 * Created by rmemoria on 23/9/16.
 */
@Service
public class ReportsService {

    /**
     * Return the list of available reports for the given scope
     * @param req
     * @return
     */
    public List<Item> getReports(ReportRequest req) {
        String[] lst = {
            "Case Notifications",
            "Outcomes",
            "Drug Resistance Profile",
            "Drug Resistance Survey",
            "Adverse Reactions to Medicine",
            "HIV Test",
            "Interim outcome assessment of confirmed MDR-TB cases"
        };

        List<Item> res = new ArrayList<>();

        for (String s: lst) {
            res.add(new Item(UUID.randomUUID(), s));
        }

        return res;
    }
}
