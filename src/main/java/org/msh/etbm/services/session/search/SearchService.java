package org.msh.etbm.services.session.search;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.db.entities.Searchable;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Search service used to look up searchable data in the system, like patients, TB units, laboratories, etc.
 * Created by rmemoria on 9/6/16.
 */
@Service
public class SearchService {

    private static final int DEFAULT_MAX_RESULTS = 100;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    DozerBeanMapper mapper;

    /**
     * Search for data using a key
     * @param req Data requested to the search key
     * @return list of items found
     */
    @Transactional
    public List<SearchResponse> searchByKey(SearchRequest req) {
        // query the database based on the request
        List<Searchable> res = loadResult(req);

        // mount response to the client
        List<SearchResponse> lst = new ArrayList<>();
        for (Searchable searchable: res) {
            SearchResponse it = mapper.map(searchable, SearchResponse.class);
            lst.add(it);
        }

        return lst;
    }

    /**
     * Create the query based on the request parameters
     * @param req the client request
     * @return Result list of searchable items
     */
    private List<Searchable> loadResult(SearchRequest req) {
        String hql = "from Searchable where upper(title) like :key and workspace.id = :wsid ";

        if (req.getType() != null) {
            hql += " and type = :type ";
        }
        hql += "order by type, title";

        Query qry = entityManager.createQuery(hql)
                .setParameter("wsid", userRequestService.getUserSession().getWorkspaceId())
                .setParameter("key", req.getKey().toUpperCase() + "%");

        if (req.getType() != null) {
            qry.setParameter("type", req.getType());
        }

        qry.setMaxResults( req.getMaxResults() != null ? req.getMaxResults() : DEFAULT_MAX_RESULTS );

        return qry.getResultList();
    }
}
