package org.msh.etbm.services.admin.sessionreport;

import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.UserLogin;
import org.msh.etbm.services.admin.onlinereport.OnlineReportData;
import org.msh.etbm.services.admin.onlinereport.OnlineReportService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by msantos on 9/3/16.
 */
@Service
public class SessionReportServiceImpl implements SessionReportService {

    @PersistenceContext
    EntityManager entityManager;

    public QueryResult getResultByDay(Date day) {
        QueryResult ret = new QueryResult<OnlineReportData>();

        List<UserLogin> results = entityManager.createQuery("from UserLogin where loginDate >= :iniDay and loginDate <= :endDay ")
                .setParameter("iniDay", DateUtils.getDatePart(day))
                .setParameter("endDay", DateUtils.getLastMinute(day))
                .getResultList();

        ret.setList(new ArrayList<SessionReportData>());
        ret.setCount( (results == null ? 0 : results.size()) );

        for (UserLogin u : results) {
            ret.getList().add(new SessionReportData(u.getUser().getLogin(), u.getUser().getName(), u.getLoginDate(), u.getLogoutDate(), u.getIpAddress(), u.getApplication()));
        }

        return ret;
    }

    public QueryResult getResult(Date iniDate, Date endDate, UUID userId) {
        /*QueryResult ret = new QueryResult<OnlineReportData>();

        List<UserLogin> results = entityManager.createQuery("from UserLogin where logoutDate is null ")
                .getResultList();

        ret.setList(new ArrayList<OnlineReportData>());
        ret.setCount( (results == null ? 0 : results.size()) );

        for (UserLogin u : results) {
            ret.getList().add(new OnlineReportData(u.getUser().getLogin(), u.getUser().getName(), u.getLoginDate(), u.getLastAccess()));
        }

        return ret;*/

        return null;
    }
}