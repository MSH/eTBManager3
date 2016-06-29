package org.msh.etbm.services.session.search;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.db.entities.Searchable;
import org.msh.etbm.db.enums.SearchableType;
import org.msh.etbm.db.enums.UserView;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.services.session.usersession.UserSession;
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
     *
     * @param req Data requested to the search key
     * @return list of items found
     */
    @Transactional
    public List<SearchResponse> searchByKey(SearchRequest req) {
        // query the database based on the request
        List<Searchable> res1 = loadResult(req, false);
        List<Searchable> res2 = loadResult(req, true);

        List<Searchable> res = mixLists(res1, res2, req);

        // mount response to the client
        List<SearchResponse> lst = new ArrayList<>();
        for (Searchable searchable : res) {
            SearchResponse it = mapper.map(searchable, SearchResponse.class);
            lst.add(it);
        }

        return lst;
    }

    private List<Searchable> mixLists(List<Searchable> res1, List<Searchable> res2, SearchRequest req) {
        int max = req.getMaxResults() != null ? req.getMaxResults() : DEFAULT_MAX_RESULTS;

        int count = res1.size() + res2.size();

        List<Searchable> result;
        int end1 = res1.size();
        int end2 = res2.size();

        // check if lists must be clipped
        if (count > max) {
            float perc = (float) max / (float) count;
            end1 = (int) (perc * res1.size());
            end2 = max - end1;
        }

        // mix both lists
        result = new ArrayList<>(res1.subList(0, end1));
        result.addAll(res2.subList(0, end2));

        return result;
    }

    /**
     * Create the query based on the request parameters
     *
     * @param req the client request
     * @return Result list of searchable items
     */
    private List<Searchable> loadResult(SearchRequest req, boolean casesOnly) {
        UserSession session = userRequestService.getUserSession();

        String hql = "from Searchable where workspace.id = :wsid ";

        // include query restriction
        switch (session.getView()) {
            case UNIT:
                hql += "and unit.id = " + session.getUnitId();
                break;
            case ADMINUNIT:
                hql += "and unit.adminUnit.code like (select concat(code, '%') from AdministrativeUnit where a.id=:auid) ";
        }

        hql += casesOnly ? " and type in (:c1, :c2) and (upper(title) like :key or upper(subtitle) like :key) order by title" :
                " and upper(title) like :key and type < :c3 order by type, title";

        Query qry = entityManager.createQuery(hql)
                .setParameter("wsid", userRequestService.getUserSession().getWorkspaceId())
                .setParameter("key", req.getKey().toUpperCase() + "%");

        // set parameter if view is per admin unit
        if (session.getView() == UserView.ADMINUNIT) {
            qry.setParameter("auid", session.getAdminUnit().getSelected().getId());
        }

        // just load cases ?
        if (casesOnly) {
            qry.setParameter("c1", SearchableType.CASE_MAN);
            qry.setParameter("c2", SearchableType.CASE_WOMAN);
        } else {
            qry.setParameter("c3", SearchableType.CASE_MAN);
        }

        qry.setMaxResults(req.getMaxResults() != null ? req.getMaxResults() : DEFAULT_MAX_RESULTS);

        return qry.getResultList();
    }
}
