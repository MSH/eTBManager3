package org.msh.etbm.services.admin.cmdhisotryrep;

import org.msh.etbm.commons.JsonParser;
import org.msh.etbm.commons.commands.details.CommandLogDetail;
import org.msh.etbm.commons.commands.details.CommandLogDiff;
import org.msh.etbm.commons.commands.details.CommandLogItem;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.CommandHistory;
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

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    public QueryResult getResult(CmdHistoryRepQueryParams query) {
        if (query.getIniDate() == null) {
            //TODOMSF: EntityValidationException
        }

        QueryResult<CmdHistoryRepData> ret = new QueryResult();

        QueryBuilder qry = queryBuilderFactory.createQueryBuilder(CommandHistory.class, "a");
        qry.addRestriction("a.workspace.id = :wId", userRequestService.getUserSession().getWorkspaceId());
        qry.addRestriction("a.execDate >= :iniDate", DateUtils.getDatePart(query.getIniDate()));
        qry.addRestriction("a.execDate < :endDate", query.getEndDate() != null ? DateUtils.getDatePart(DateUtils.incDays(query.getEndDate(), 1)) : null);
        qry.addRestriction("a.action = :action", query.getAction());
        qry.addRestriction("a.user.id = :userId", query.getUserId());
        qry.addRestriction("a.type like :type", query.getType());
        //qry.addRestriction("a.type = :type", query.getAdminUnitId()); TODOMSF
        //qry.addRestriction("(a.type like :type or a.type like :type)", query.getSearchKey()); TODOMSF

        List<CommandHistory> list = qry.getResultList();
        ret.setList(new ArrayList<>());
        ret.setCount(list.size());

        for (CommandHistory c : list) {
            String userName = c.getUser() != null ? c.getUser().getName() : null;
            String unitName = c.getUnit() != null ? c.getUnit().getName() : null;
            String adminUnitName = c.getUnit() != null ? c.getUnit().getAddress().getAdminUnit().getFullDisplayName() : null;

            ret.getList().add(new CmdHistoryRepData(c.getType(), c.getAction(), c.getExecDate(), c.getEntityName(), userName, unitName, adminUnitName, processJsonData(c.getData())));
        }

        return ret;
    }

    private CommandLogDetail processJsonData(String data){
        CommandLogDetail c = JsonParser.parseString(data, CommandLogDetail.class);
        if (c == null) {
            return null;
        }

        if (c.getItems() != null ) {
            for (CommandLogItem i : c.getItems()) {
                i.setTitle(processStringToDisplay(i.getTitle()));
                i.setValue(processStringToDisplay(i.getValue()));
            }
        }

        if (c.getDiffs() != null) {
            for (CommandLogDiff i : c.getDiffs()) {
                i.setTitle(processStringToDisplay(i.getTitle()));
                i.setNewValue(processStringToDisplay(i.getNewValue()));
                i.setPrevValue(processStringToDisplay(i.getPrevValue()));
            }
        }

        c.setText("Hey oh lets go! Hey oh lets go! Hey oh lets go! Hey oh lets go! Hey oh lets go! Hey oh lets go! Hey oh lets go!Hey oh lets go! Hey oh lets go! Hey oh lets go! Hey oh lets go! Hey oh lets go! Hey oh lets go! Hey oh lets go!");

        return c;
    }

    private String processStringToDisplay(String s){
        return s;
    }
}