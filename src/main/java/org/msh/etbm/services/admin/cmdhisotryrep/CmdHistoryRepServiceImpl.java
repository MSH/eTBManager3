package org.msh.etbm.services.admin.cmdhisotryrep;

import org.msh.etbm.Messages;
import org.msh.etbm.commons.JsonParser;
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
import org.msh.etbm.db.repositories.AdminUnitRepository;
import org.msh.etbm.services.admin.admunits.AdminUnitData;
import org.msh.etbm.services.admin.admunits.AdminUnitService;
import org.msh.etbm.services.admin.units.TypedUnit;
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

    @Autowired
    AdminUnitRepository adminUnitRepository;

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    @Autowired
    Messages messages;

    public QueryResult getResult(CmdHistoryRepQueryParams query) {
        if (query.getIniDate() == null) {
            throw new EntityValidationException(query, "iniDate", "javax.validation.constraints.NotNull.message", null);
        }

        AdministrativeUnit adminUnit = null;

        if (query.getAdminUnitId() != null ){
            adminUnit = adminUnitRepository.findOne(query.getAdminUnitId());
        }

        QueryResult<CmdHistoryRepData> ret = new QueryResult();

        QueryBuilder qry = queryBuilderFactory.createQueryBuilder(CommandHistory.class, "a");
        qry.addRestriction("a.workspace.id = :wId", userRequestService.getUserSession().getWorkspaceId());
        qry.addRestriction("a.execDate >= :iniDate", DateUtils.getDatePart(query.getIniDate()));
        qry.addRestriction("a.execDate < :endDate", query.getEndDate() != null ? DateUtils.getDatePart(DateUtils.incDays(query.getEndDate(), 1)) : null);
        qry.addRestriction("a.action = :action", query.getAction());
        qry.addRestriction("a.user.id = :userId", query.getUserId());
        qry.addLikeRestriction("a.type", query.getType());
        if (adminUnit != null) {
            qry.addRestriction("a.unit.address.adminUnit.code like :code", adminUnit.getCode() + "%");
        }
        if (query.getSearchKey() != null && !query.getSearchKey().isEmpty()) {
            qry.addRestriction("(a.data like :searchKey or a.entityName like :searchKey)", "%" + query.getSearchKey() + "%", "%" + query.getSearchKey() + "%");
        }
        
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

    private CommandLogDetail processJsonData(String data) {
        CommandLogDetail c = JsonParser.parseString(data, CommandLogDetail.class);
        if (c == null) {
            return null;
        }

        if (c.getItems() != null ) {
            for (CommandLogItem i : c.getItems()) {
                i.setTitle(processTitleToDisplay(i.getTitle()));
                i.setValue(processValueToDisplay(i.getValue()));
            }
        }

        if (c.getDiffs() != null) {
            for (CommandLogDiff i : c.getDiffs()) {
                i.setTitle(processTitleToDisplay(i.getTitle()));
                i.setNewValue(processValueToDisplay(i.getNewValue()));
                i.setPrevValue(processValueToDisplay(i.getPrevValue()));
            }
        }

        return c;
    }

    private String processTitleToDisplay(String s) {
        if (s == null) {
            return null;
        }

        String ret;

        if (s.contains("action.added")) {
            ret = "+";
            s = s.replace(" ($action.added)", "");
        } else if (s.contains("action.removed")) {
            ret = "-";
            s = s.replace(" ($action.removed)", "");
        } else {
            ret = "";
        }

        String[] msgs = s.replace("(","").replace(")", "").split(" ");
        ret = ret + s;

        for (String msg : msgs) {
            ret = ret.replace(msg, messages.get(msg.substring(1, msg.length())));
        }

        return ret;
    }

    private String processValueToDisplay(String s) {
        if (s == null) {
            return null;
        }

        String type = s.substring(0,1);

        switch (type) {
            case CommandLogDetail.TYPE_STRING :  s = s.substring(1, s.length());
                break;
            case CommandLogDetail.TYPE_BOOLEAN :  s = processBooleanValue(s);
                break;
            case CommandLogDetail.TYPE_DATETIME :  s = processDateValue(s);
                break;
            case CommandLogDetail.TYPE_NUMBER :  s = processNumberValue(s);
                break;
            case CommandLogDetail.TYPE_TEMPLATE :  s = processTemplateValue(s);
                break;
        }

        return s;
    }

    private String processBooleanValue(String s){
        if (s.equals("B0")) {
            return messages.get("global.no");
        } else if (s.equals("B1")){
            return messages.get("global.yes");
        }

        return s;
    }

    private String processDateValue(String s){
        return s;
    }

    private String processNumberValue(String s){
        return s;
    }

    private String processTemplateValue(String s){
        return s;
    }

}