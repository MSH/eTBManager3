package org.msh.etbm.services.session.search;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Search service used to look up searchable data in the system, like patients, TB units, laboratories, etc.
 * Created by rmemoria on 9/6/16.
 */
public class SearchService {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Search for data using a key
     * @param key the key containing data to search for
     * @return list of items found
     */
    public List<SearchResponse> searchByKey(String key, Integer maxResult) {
        List<SearchResponse> lst = new ArrayList<>();

        return lst;
    }
}
