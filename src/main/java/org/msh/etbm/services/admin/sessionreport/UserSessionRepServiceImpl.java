package org.msh.etbm.services.admin.sessionreport;

import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.UserLogin;
import org.msh.etbm.services.admin.onlinereport.OnlineUsersRepData;
import org.msh.etbm.services.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by msantos on 9/3/16.
 */
@Service
public class UserSessionRepServiceImpl implements UserSessionRepService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRequestService userRequestService;

    public QueryResult getResultByDay(UserSessionRepQueryParams query) {
        if (query.getIniDate() == null) {
            //TODO: retornar erro e validação
        }

        List<UserLogin> result = entityManager.createQuery("from UserLogin where loginDate >= :iniDate and loginDate < :endDate and workspace.id = :wId ")
                .setParameter("iniDate", DateUtils.getDatePart(query.getIniDate()))
                .setParameter("endDate", DateUtils.getDatePart(DateUtils.incDays(query.getIniDate(), 1)))
                .setParameter("wId", userRequestService.getUserSession().getWorkspaceId())
                .getResultList();

        return createQueryResult(result);
    }

    public QueryResult getResult(UserSessionRepQueryParams query) {
        if (query.getIniDate() == null) {
            //TODO: retornar erro e validação
        }

        if (query.getEndDate() == null) {
            //TODO: retornar erro e validação
        }

        String qStr = "from UserLogin where loginDate >= :iniDate and loginDate < :endDate and workspace.id = :wId ";
        if (query.getUserId() != null) {
            qStr = qStr + "and user.id = :userId";
        }

        Query q = entityManager.createQuery(qStr);
        q.setParameter("iniDate", DateUtils.getDatePart(query.getIniDate()));
        q.setParameter("endDate", DateUtils.getDatePart(DateUtils.incDays(query.getEndDate(), 1)));
        q.setParameter("wId", userRequestService.getUserSession().getWorkspaceId());
        if (qStr.contains("userId")) {
            q.setParameter("userId", query.getUserId());
        }

        List<UserLogin> result = q.getResultList();

        return createQueryResult(result);
    }

    private QueryResult createQueryResult(List<UserLogin> result) {
        QueryResult ret = new QueryResult<OnlineUsersRepData>();
        ret.setList(new ArrayList<UserSessionRepData>());
        ret.setCount( (result == null ? 0 : result.size()) );

        for (UserLogin u : result) {
            ret.getList().add(new UserSessionRepData(u.getUser().getLogin(), u.getUser().getName(), u.getLoginDate(), u.getLogoutDate(), u.getIpAddress(), u.getApplication()));
        }

        return ret;
    }
}