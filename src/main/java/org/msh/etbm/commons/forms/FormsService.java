package org.msh.etbm.commons.forms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Services to handle form operations. Initialize field contents that need resources
 * from the server side, for example, list of units needed in a drop down
 * Created by rmemoria on 17/1/16.
 */
@Component
public class FormsService {

    @Autowired
    TypesManagerService typesManager;

    /**
     * Initialize fields content
     * @param fields list of fields and its data to be initialized with the content to be displayed
     * @return Map with id (given in the argument) as the key and the result to initialize the fields
     */
    public Map<String, Object> initFormFields(List<FieldInitRequest> fields) {
        Map<String, Object> map = new HashMap<>();

        for (FieldInitRequest fieldReq: fields) {
            TypeHandler handler = typesManager.get(fieldReq.getType());

            if (handler == null) {
                throw new RuntimeException("Invalid type handler " + fieldReq.getType());
            }

            // initialize the field with the proper response
            Object res = handler.initField(fieldReq);

            map.put(fieldReq.getId(), res);
        }

        return map;
    }
}
