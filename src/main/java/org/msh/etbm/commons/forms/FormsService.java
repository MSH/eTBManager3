package org.msh.etbm.commons.forms;

import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public FormResponse initForm(FormRequest req, EntityService serv, Class formDataClass) {
        FormResponse resp = new FormResponse();

        if (req.getId() != null) {
            // load the entity from the service
            Object data = serv.findOne(req.getId(), formDataClass);
            resp.setDoc(data);
        }
        else {
            resp.setDoc(ObjectUtils.newInstance(formDataClass));
        }

        if (req.getFields() != null) {
            // initialize the fields
            Map<String, Object> flds = initFormFields(req.getFields());
            resp.setResources(flds);
        }

        return resp;
    }

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
                throw new FormException("Invalid type handler " + fieldReq.getType());
            }

            // initialize the field with the proper response
            Object res = handler.initField(fieldReq);

            map.put(fieldReq.getId(), res);
        }

        return map;
    }
}
