package org.msh.etbm.services.admin.cmdhisotryrep;

import org.msh.etbm.db.entities.UserLogin;
import org.msh.etbm.services.admin.onlinereport.OnlineUsersRepService;
import org.msh.etbm.services.admin.onlinereport.OnlineUsersRepData;
import org.msh.etbm.services.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by msantos on 9/3/16.
 */
@Service
public class CmdHistoryRepServiceImpl implements CmdHistoryRepService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRequestService userRequestService;

    public List<OnlineUsersRepData> getResult(CmdHistoryRepQueryParams query) {
        List<OnlineUsersRepData> ret = new ArrayList<>();

        List<UserLogin> results = entityManager.createQuery("from UserLogin where logoutDate is null and workspace.id = :wId")
                                    .setParameter("wId", userRequestService.getUserSession().getWorkspaceId())
                                    .getResultList();

        for (UserLogin u : results) {
            ret.add(new OnlineUsersRepData(u.getUser().getLogin(), u.getUser().getName(), u.getLoginDate(), u.getLastAccess()));
        }

        return ret;
    }
}