package org.msh.etbm.services.admin.cmdhisotryrep;

import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.onlinereport.OnlineUsersRepData;

import java.util.List;

/**
 * Created by msantos on 15/3/16.
 */
public interface CmdHistoryRepService {
    QueryResult getResult(CmdHistoryRepQueryParams query);
}
