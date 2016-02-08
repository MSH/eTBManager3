package org.msh.etbm.web.api.sys;

import org.msh.etbm.Messages;
import org.msh.etbm.db.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Provide global list options used throughout the system. The main reason
 * of this service is to expose all standard lists to the client side
 *
 * Created by rmemoria on 10/12/15.
 */
@Service
public class GlobalListsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalListsService.class);

    @Autowired
    Messages messages;


    // available lists to be sent to the client
    private static final Class[] lists = {
            MedicineLine.class,
            MedicineCategory.class,
            CaseClassification.class,
            CaseState.class,
            UserState.class
    };

    /**
     * Return the options of all lists supported by the system
     * @return
     */
    public Map<String, Map<String, String> > getLists() {
        Map<String, Map<String, String>> res = new HashMap<>();

        for (Class clazz: lists) {
            String name = clazz.getSimpleName();
            Map<String, String> options = getOptions(clazz);

            res.put(name, options);
        }

        return res;
    }

    /**
     * Return the options from the given class (actually, just enums are supported)
     * @param clazz the enum class to get options from
     * @return the list of options from the enumeration
     */
    private Map<String, String> getOptions(Class clazz) {
        if (!clazz.isEnum()) {
            throw new RuntimeException("Type not supported: " + clazz);
        }

        Object[] vals = clazz.getEnumConstants();

        Map<String, String> opts = new HashMap<>();

        for (Object val: vals) {
            String messageKey = clazz.getSimpleName() + '.' + val.toString();
            String message = messages.get(messageKey);

            opts.put(val.toString(), message);
        }

        return opts;
    }

}
