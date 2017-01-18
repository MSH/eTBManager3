package org.msh.etbm.commons.dbcache;

import org.springframework.stereotype.Service;

/**
 * Created by rmemoria on 18/1/17.
 */
@Service
public class DbCacheManager {

    private DbCacheUtils utils;

    public DbCacheManager(DbCacheUtils utils) {
        this.utils = utils;
    }

    public void updateIn(String entry, String time) {

    }
}
