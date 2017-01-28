package org.msh.etbm.test.commons.dbcache;

import org.junit.Test;
import org.msh.etbm.test.AuthenticatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * The {@link org.msh.etbm.commons.dbcache.DbCache} annotation store complex and long method result
 * in the database, working as a cache. The method execution is intercepted and if data is not in the
 * cache or cache is expired, the method is executed and its result recorded in the cache.
 *
 * But what if the method is executed simultaneously by several request at the same time?
 *
 * That is what this test case does - launches 5 threads to execute a service with DbCache annotation
 * and it is expected that the service will be called just once.
 *
 * Created by rmemoria on 27/1/17.
 */
public class DbCacheConcurrencyTest extends AuthenticatedTest implements Runnable {

    @Autowired
    TestService testService;

    private RequestAttributes requestAttributes;

    @Test
    public void testConcurrency() throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();

        requestAttributes = RequestContextHolder.getRequestAttributes();

        for (int i = 1; i <= 5; i++) {
            exec.execute(this);
        }
        exec.awaitTermination(1000, TimeUnit.MILLISECONDS);

        assertEquals(1, testService.getCounter());
    }

    @Override
    public void run() {
        RequestContextHolder.setRequestAttributes(requestAttributes);
        testService.execute();
    }
}
