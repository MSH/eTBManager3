package org.msh.etbm.services.admin.substances;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.options.OptionsManagerService;
import org.msh.etbm.commons.forms.options.OptionsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provide a list of options for substances
 * Created by rmemoria on 1/2/16.
 */
@Component
public class SubstanceOptionsProvider implements OptionsProvider {

    public static final String OPTIONS_NAME = "substances";

    @Autowired
    OptionsManagerService optionsManagerService;

    @Autowired
    SubstanceService substanceService;

    /**
     * Initialize the component when it is created
     */
    @PostConstruct
    public void init() {
        // register it as a list of options
        optionsManagerService.register(OPTIONS_NAME, this);
    }


    @Override
    public List<Item> getOptions(Map<String, Object> params) {
        SubstanceQueryParams qry = new SubstanceQueryParams();
        qry.setProfile(SubstanceQueryParams.PROFILE_DEFAULT);
        qry.setOrderBy(SubstanceQueryParams.ORDERBY_NAME);
        QueryResult<SubstanceData> res = substanceService.findMany(qry);

        // mount list to include the short name in the label
        List<Item> lst = new ArrayList<>();
        for (SubstanceData sub: res.getList()) {
            lst.add(new SynchronizableItem(sub.getId(), "(" + sub.getShortName() + ") " + sub.getName()));
        }

        return lst;
    }
}
