package org.msh.etbm.services.admin.substances;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.options.OptionsManagerService;
import org.msh.etbm.commons.forms.options.OptionsResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Created by rmemoria on 1/2/16.
 */
@Component
public class SubstanceOptionsResolver implements OptionsResolver {

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
        qry.setProfile(SubstanceQueryParams.PROFILE_ITEM);
        QueryResult res = substanceService.findMany(qry);

        return res.getList();
    }
}
