package org.msh.etbm.services.admin.errorlogrep;

import org.msh.etbm.Messages;
import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.JsonParser;
import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.details.CommandLogDetail;
import org.msh.etbm.commons.commands.details.CommandLogDiff;
import org.msh.etbm.commons.commands.details.CommandLogItem;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.entities.CommandHistory;
import org.msh.etbm.db.entities.ErrorLog;
import org.msh.etbm.db.entities.UserLogin;
import org.msh.etbm.services.admin.cmdhisotryrep.CmdHistoryRepData;
import org.msh.etbm.services.admin.cmdhisotryrep.CmdHistoryRepQueryParams;
import org.msh.etbm.services.admin.cmdhisotryrep.CmdHistoryRepService;
import org.msh.etbm.services.admin.onlinereport.OnlineUsersRepData;
import org.msh.etbm.services.admin.sessionreport.UserSessionRepData;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by msantos on 05/7/16.
 */
@Service
public class ErrorLogRepServiceImpl implements ErrorLogRepService {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    @Autowired
    Messages messages;

    public QueryResult getResult(ErrorLogRepQueryParams query) {
        if (query.getIniDate() == null) {
            throw new EntityValidationException(query, "iniDate", "javax.validation.constraints.NotNull.message", null);
        }

        QueryBuilder qry = queryBuilderFactory.createQueryBuilder(ErrorLog.class, "a");
        qry.addRestriction("a.errorDate >= :iniDate", DateUtils.getDatePart(query.getIniDate()));
        qry.addRestriction("a.errorDate < :endDate", query.getEndDate() != null ? DateUtils.getDatePart(DateUtils.incDays(query.getEndDate(), 1)) : null);

        if(query.getSearchKey() != null && !query.getSearchKey().isEmpty()) {
            String searchKeyRestriction = "(a.exceptionClass like :searchKey or a.exceptionMessage like :searchKey or a.stackTrace like :searchKey or a.userName like :searchKey or a.url like :searchKey or a.workspace like :searchKey or a.request like :searchKey)";
            searchKeyRestriction = searchKeyRestriction.replace(":searchKey", "'%" + query.getSearchKey() + "%'");
            qry.addRestriction(searchKeyRestriction);
        }

        return createQueryResult(qry.getResultList());
    }

    private QueryResult createQueryResult(List<ErrorLog> result) {
        QueryResult ret = new QueryResult<OnlineUsersRepData>();
        ret.setList(new ArrayList<UserSessionRepData>());
        ret.setCount((result == null ? 0 : result.size()));

        for (ErrorLog e : result) {
            ret.getList().add(new ErrorLogRepData(e.getErrorDate(), e.getExceptionClass(), e.getExceptionMessage(), e.getUrl(), e.getUserName(), e.getStackTrace(), e.getWorkspace(), e.getRequest()));
        }

        return ret;
    }
}