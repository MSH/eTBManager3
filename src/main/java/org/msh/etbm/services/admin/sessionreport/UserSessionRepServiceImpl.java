package org.msh.etbm.services.admin.sessionreport;

import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.UserLogin;
import org.msh.etbm.services.admin.onlinereport.OnlineUsersRepData;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    public QueryResult getResultByDay(UserSessionRepQueryParams query) {
        if( query.getIniDate() == null) {
            //TODO: retornar erro e validação
        }

        List<UserLogin> result = entityManager.createQuery("from UserLogin where loginDate >= :iniDay and loginDate < :nextDay ")
                .setParameter("iniDay", DateUtils.getDatePart(query.getIniDate()))
                .setParameter("nextDay", DateUtils.getDatePart(DateUtils.incDays(query.getIniDate(), 1)))
                .getResultList();

        return createQueryResult(result);
    }

    public QueryResult getResult(UserSessionRepQueryParams query) {
        return null;
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