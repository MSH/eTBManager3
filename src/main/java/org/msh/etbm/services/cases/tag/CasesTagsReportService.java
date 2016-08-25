package org.msh.etbm.services.cases.tag;

import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.entities.Tag;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service to generate a report about the quantity of cases per tag
 * Created by rmemoria on 17/6/16.
 */
@Service
public class CasesTagsReportService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRequestService userRequestService;

    /**
     * Generate a report of quantity of cases per tag of the whole workspace
     *
     * @return list of tags and its quantity of cases
     */
    @Transactional
    public List<CasesTagsReportItem> generate() {
        String sql = "select t.id, t.name, t.sqlCondition is null, t.consistencyCheck, count(*) " +
                "from tags_case tc " +
                "inner join tag t on t.id = tc.tag_id " +
                " where t.WORKSPACE_ID = :id and t.active = true " +
                " group by t.id, t.name order by t.name";

        UUID wsid = userRequestService.getUserSession().getWorkspaceId();

        Query qry = entityManager
                .createNativeQuery(sql)
                .setParameter("id", wsid);

        return loadData(qry);
    }

    /**
     * Generate a report of quantity of cases per tag of the whole workspace
     *
     * @return list of tags and its quantity of cases
     */
    @Transactional
    public List<CasesTagsReportItem> generateByAdminUnit(@NotNull UUID adminUnitId) {
        AdministrativeUnit adminUnit = entityManager.find(AdministrativeUnit.class, adminUnitId);
        String pidlevel = "pid" + (adminUnit.getCountryStructure().getLevel() - 1);

        String sql = "select t.id, t.name, t.sqlCondition is null, t.consistencyCheck, count(*) " +
                "from tags_case tc " +
                "inner join tag t on t.id = tc.tag_id " +
                "inner join tbcase c on c.id = tc.case_id " +
                "inner join unit u on u.id = c.owner_unit_id " +
                "inner join administrativeunit au on au.id = u.ADMINUNIT_ID " +
                " where (au.id = :id or au." + pidlevel + " = :id) and t.active = true " +
                " group by t.id, t.name order by t.name";

        Query qry = entityManager.createNativeQuery(sql)
                .setParameter("id", adminUnitId);

        return loadData(qry);
    }

    /**
     * Generate e report of quantity of cases per tag for a unit
     *
     * @param unitId the ID of the unit to generate the report from
     * @return list of tags and its quantity of cases
     */
    @Transactional
    public List<CasesTagsReportItem> generateByUnit(@NotNull UUID unitId) {
        String sql = "select t.id, t.name, t.sqlCondition is null, t.consistencyCheck, count(*) " +
                "from tags_case tc " +
                "inner join tag t on t.id = tc.tag_id " +
                "inner join tbcase c on c.id = tc.case_id " +
                " where c.owner_unit_id = :id and t.active = true " +
                " group by t.id, t.name order by t.name";

        Query qry = entityManager.createNativeQuery(sql)
                .setParameter("id", unitId);

        return loadData(qry);
    }


    private List<CasesTagsReportItem> loadData(Query qry) {
        List<CasesTagsReportItem> tags = new ArrayList<>();

        List<Object[]> lst = qry.getResultList();

        for (Object[] vals : lst) {
            Tag.TagType type = null;
            if ((Integer) vals[2] == 1) {
                type = Tag.TagType.MANUAL;
            } else {
                type = (Boolean) vals[3] == Boolean.TRUE ? Tag.TagType.AUTODANGER : Tag.TagType.AUTO;
            }

            int count = ((Number) vals[4]).intValue();
            UUID id = ObjectUtils.bytesToUUID((byte[]) vals[0]);

            CasesTagsReportItem tag = new CasesTagsReportItem();
            tag.setId(id);
            tag.setName(vals[1].toString());
            tag.setType(type);
            tag.setCount(count);
            tags.add(tag);
        }

        if (tags.size() > 0) {
            tags.sort((it1, it2) -> -it1.getType().compareTo(it2.getType()));
        }

        return tags;
    }
}
