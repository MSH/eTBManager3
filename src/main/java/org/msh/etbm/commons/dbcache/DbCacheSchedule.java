package org.msh.etbm.commons.dbcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Component to check for expired data that must be updated again
 * Created by rmemoria on 12/1/17.
 */
@Service
public class DbCacheSchedule {

    private final static Logger log = LoggerFactory.getLogger(DbCacheSchedule.class);

    @Autowired
    DbCacheUpdate dbCacheUpdate;


    /**
     * Execute the scheduled checking of expired cached data
     */
    @Scheduled(fixedDelay = 60000, initialDelay = 1000)
    public void execute() {
        log.info("Executing scheduled DB cache update...");
        dbCacheUpdate.execute();
    }

}
