package org.msh.etbm.test.commons.dbcache;

import org.msh.etbm.commons.dbcache.DbCache;
import org.springframework.stereotype.Service;

/**
 * Created by rmemoria on 27/1/17.
 */
@Service
public class TestService {

    private int counter;

    @DbCache
    public Integer execute() {
        counter++;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return counter;
    }

    public int getCounter() {
        return counter;
    }
}
