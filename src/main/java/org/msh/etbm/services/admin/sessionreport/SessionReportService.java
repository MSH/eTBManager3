package org.msh.etbm.services.admin.sessionreport;

import org.msh.etbm.commons.entities.query.QueryResult;

import java.util.Date;
import java.util.UUID;

/**
 * Created by msantos on 11/3/16.
 */
public interface SessionReportService {
    QueryResult getResultByDay(Date day);

    QueryResult getResult(Date iniDate, Date endDate, UUID userId);
}
