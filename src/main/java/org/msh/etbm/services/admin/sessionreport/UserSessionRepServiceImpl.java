package org.msh.etbm.services.admin.sessionreport;

import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.UserLogin;
import org.msh.etbm.services.admin.onlinereport.OnlineUsersRepData;
import org.msh.etbm.services.session.usersession.UserRequestService;
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
public class UserSessionRepServiceImpl implements UserSessionRepService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    public QueryResult getResultByDay(UserSessionRepQueryParams query) {
        if (query.getIniDate() == null) {
            throw new EntityValidationException(query, "iniDate", "javax.validation.constraints.NotNull.message", null);
        }

        QueryBuilder qry = queryBuilderFactory.createQueryBuilder(UserLogin.class, "a");
        qry.addRestriction("a.loginDate >= :iniDate", DateUtils.getDatePart(query.getIniDate()));
        qry.addRestriction("a.loginDate < :endDate", DateUtils.getDatePart(DateUtils.incDays(query.getIniDate(), 1)));
        qry.addRestriction("a.workspace.id = :wId", userRequestService.getUserSession().getWorkspaceId());

        return createQueryResult(qry.getResultList());
    }

    public QueryResult getResult(UserSessionRepQueryParams query) {

        if (query.getIniDate() == null) {
            throw new EntityValidationException(query, "iniDate", "javax.validation.constraints.NotNull.message", null);
        }

        QueryBuilder qry = queryBuilderFactory.createQueryBuilder(UserLogin.class, "a");
        qry.addRestriction("a.loginDate >= :iniDate", DateUtils.getDatePart(query.getIniDate()));
        qry.addRestriction("a.loginDate < :endDate", query.getEndDate() != null ? DateUtils.getDatePart(DateUtils.incDays(query.getEndDate(), 1)) : null);
        qry.addRestriction("a.workspace.id = :wId", userRequestService.getUserSession().getWorkspaceId());
        qry.addRestriction("a.user.id = :userId", query.getUserId());

        return createQueryResult(qry.getResultList());
    }

    private QueryResult createQueryResult(List<UserLogin> result) {
        QueryResult ret = new QueryResult<OnlineUsersRepData>();
        ret.setList(new ArrayList<UserSessionRepData>());
        ret.setCount((result == null ? 0L : result.size()));

        for (UserLogin u : result) {
            ret.getList().add(new UserSessionRepData(u.getUser().getLogin(), u.getUser().getName(), u.getLoginDate(), u.getLogoutDate(), u.getIpAddress(), u.getApplication()));
        }

        return ret;
    }
}