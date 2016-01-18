package org.msh.etbm.commons.forms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
     * @param fields
     * @return
     */
    public List<FieldInitResponse> initFormFields(List<FieldInitRequest> fields) {
        List<FieldInitResponse> lst = new ArrayList<>();

        for (FieldInitRequest fieldReq: fields) {
            TypeHandler handler = typesManager.get(fieldReq.getType());

            if (handler == null) {
                throw new RuntimeException("Invalid type handler " + fieldReq.getType());
            }

            // initialize the field with the proper response
            Object res = handler.initField(fieldReq);

            lst.add( new FieldInitResponse(fieldReq.getId(), res) );
        }

        return lst;
    }
}
