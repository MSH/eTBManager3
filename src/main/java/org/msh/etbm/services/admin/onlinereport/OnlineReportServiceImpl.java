package org.msh.etbm.services.admin.onlinereport;

import org.msh.etbm.db.entities.UserLogin;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by msantos on 9/3/16.
 */
@Service
public class OnlineReportServiceImpl implements OnlineReportService {

    @PersistenceContext
    EntityManager entityManager;

    public List<OnlineReportData> getResult() {
        List<OnlineReportData> ret = new ArrayList<>();

        List<UserLogin> results = entityManager.createQuery("from UserLogin where logoutDate is null ")
                                    .getResultList();

        for (UserLogin u : results) {
            ret.add(new OnlineReportData(u.getUser().getLogin(), u.getUser().getName(), u.getLoginDate(), u.getLastAccess()));
        }

        return ret;
    }
}