package org.msh.etbm.commons;

import org.msh.etbm.db.PersonName;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.db.enums.NameComposition;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Mauricio on 08/10/2016.
 */
@Component
public class PersonNameUtils {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRequestService userRequestService;

    public String displayPersonName (PersonName personName) {
        UUID workspaceId = userRequestService.getUserSession().getWorkspaceId();
        return displayPersonName(personName, workspaceId);
    }

    public String displayPersonName (PersonName personName, UUID workspaceId) {
        Workspace workspace = entityManager.find(Workspace.class, workspaceId);
        ArrayList names = new ArrayList();

        if (personName == null) {
            return null;
        }

        switch (workspace.getPatientNameComposition()) {
            case FULLNAME:
                return personName.getName();
            case FIRSTSURNAME:
                names.add(personName.getName());
                names.add(personName.getMiddleName());
                break;
            case SURNAME_FIRSTNAME:
                names.add(personName.getMiddleName());
                names.add(personName.getName());
                break;
            case FIRST_MIDDLE_LASTNAME:
                names.add(personName.getName());
                names.add(personName.getMiddleName());
                names.add(personName.getLastName());
                break;
            case LAST_FIRST_MIDDLENAME:
                names.add(personName.getLastName());
                names.add(personName.getName());
                names.add(personName.getMiddleName());
                break;
            case LAST_FIRST_MIDDLENAME_WITHOUT_COMMAS:
                names.add(personName.getName());
                names.add(personName.getMiddleName());
                names.add(personName.getLastName());
                break;
        }

        return names.size() > 0 ? mountName(names, workspace.getPatientNameComposition().equals(NameComposition.LAST_FIRST_MIDDLENAME)) : null;
    }

    private String mountName(ArrayList<String> names, boolean putCommas) {
        String spacer = putCommas ? ", " : " ";
        int i = putCommas ? 2 : 1;
        String ret = "";

        for (String name : names) {
            if (name != null) {
                ret = ret.concat(name).concat(spacer);
            }
        }

        return ret.substring(0, ret.length() - i);
    }
}
