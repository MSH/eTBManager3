package org.msh.etbm.services.cases.workspace;

import org.msh.etbm.commons.entities.EntityUtils;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.services.admin.tags.CasesTagsReportItem;
import org.msh.etbm.services.admin.tags.CasesTagsReportService;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service to generate a view of the workspace about the case management
 * <p>
 * Created by rmemoria on 17/6/16.
 */
@Service
public class WorkspaceViewService {

    @Autowired
    CasesTagsReportService casesTagsReportService;

    @Autowired
    UserRequestService userRequestService;

    @PersistenceContext
    EntityManager entityManager;


    /**
     * Generate a report of the workspace about the consolidated cases by administrative units
     * and the quantity of tags by case
     *
     * @return instance of {@link WorkspaceViewResponse} containing the report view
     */
    @Transactional
    public WorkspaceViewResponse generateView() {
        WorkspaceViewResponse resp = new WorkspaceViewResponse();

        // load information about the tags
        List<CasesTagsReportItem> tags = casesTagsReportService.generate();
        resp.setTags(tags);

        // load information about the places
        List<PlaceData> places = loadPlaces(null);

        resp.setPlaces(places);

        return resp;
    }

    /**
     * Generate a report of the quantity of cases by administrative unit and units, using
     * as reference a given administrative unit
     *
     * @param adminUnitId the ID of the administrative unit to generate the report from
     * @return
     */
    @Transactional
    public List<PlaceData> generateAdminUnitView(UUID adminUnitId) {
        List<PlaceData> places = loadPlaces(adminUnitId);

        return places;
    }


    private List<PlaceData> loadPlaces(UUID adminUnitId) {
        List<PlaceData> places = new ArrayList<>();

        // just load the units because it is level 0
        loadAdminUnits(places, adminUnitId);

        if (adminUnitId != null) {
            loadUnits(places, adminUnitId);
        }

        // sort items
        places.sort((it1, it2) -> it1.getName().compareToIgnoreCase(it2.getName()));

        return places;
    }


    private void loadAdminUnits(List<PlaceData> data, UUID admunitId) {
        String sql = "select a.id, a.name, a.unitsCount, k.diagnosisType, k.classification, sum(k.count)\n" +
                "from administrativeunit a\n" +
                "left join (select a2.code, c.diagnosisType, c.classification, count(*) as count\n" +
                "from tbcase c \n" +
                "inner join unit b on b.id=c.owner_unit_id\n" +
                "inner join administrativeunit a2 on a2.id = b.adminunit_id\n" +
                "where a2.workspace_id=:wsid and c.state < 3 " +
                "and c.diagnosisType is not null and c.classification is not null\n" +
                "group by a2.code, c.diagnosisType, c.classification) as k\n" +
                "  on k.code like concat(a.code, '%')\n" +
                "where " + (admunitId == null ? "a.parent_id is null\n" : "a.parent_id = :id\n") +
                "and a.workspace_id=:wsid\n" +
                "group by a.id, a.name, k.diagnosisType, k.classification\n" +
                "order by a.name";

        Query qry = entityManager
                .createNativeQuery(sql)
                .setParameter("wsid", userRequestService.getUserSession().getWorkspaceId());

        if (admunitId != null) {
            qry.setParameter("id", admunitId);
        }

        List<Object[]> lst = qry.getResultList();

        mountList(lst, data, PlaceData.PlaceType.ADMINUNIT);
    }


    /**
     * Load case information about the units
     *
     * @param data      the data to include information into
     * @param admunitId the administrative unit to get information from
     */
    private void loadUnits(List<PlaceData> data, UUID admunitId) {
        String sql = "select a.id, a.name, 0, res.diagnosisType, res.classification, count(*) " +
                "from unit a\n" +
                "inner join (select owner_unit_id, diagnosistype, classification\n" +
                "from tbcase c\n" +
                "inner join patient p on p.id=c.patient_id where p.workspace_id=:wsid\n" +
                "and c.state < 3 and c.diagnosisType is not null) res on a.id=res.owner_unit_id\n" +
                "where a.adminunit_id=:auId\n" +
                "group by a.id, a.name, res.diagnosisType, res.classification\n" +
                "order by a.name";

        List<Object[]> lst = entityManager
                .createNativeQuery(sql)
                .setParameter("wsid", userRequestService.getUserSession().getWorkspaceId())
                .setParameter("auId", admunitId)
                .getResultList();

        mountList(lst, data, PlaceData.PlaceType.UNIT);
    }

    /**
     * Mount the list of items from information retrieved from the database
     *
     * @param lst  list of values (source of data)
     * @param dest destination list to include information of place report
     * @param type the type of list (admin unit or TB unit)
     */
    protected void mountList(List<Object[]> lst, List<PlaceData> dest, PlaceData.PlaceType type) {
        for (Object[] vals : lst) {
            // check if there is any value
            long count = vals[5] != null ? ((Number) vals[5]).longValue() : 0;
            if (count == 0) {
                continue;
            }

            UUID id = EntityUtils.bytesToUUID((byte[]) vals[0]);
            String name = (String) vals[1];

            PlaceData item = findPlaceById(dest, id, type);

            if (item == null) {
                item = new PlaceData();
                item.setId(id);
                item.setName(name);
                item.setType(type);

                int children = ((Number) vals[2]).intValue();
                item.setHasChildren(children > 0);

                dest.add(item);
            }

            if (vals[3] != null && count > 0) {
                DiagnosisType dtype = DiagnosisType.values()[(Integer) vals[3]];
                CaseClassification cla = CaseClassification.values()[(Integer) vals[4]];

                // force item as a node if there are cases in an admin unit node
                if (count > 0 && type == PlaceData.PlaceType.ADMINUNIT) {
                    item.setHasChildren(true);
                }

                if (dtype == DiagnosisType.SUSPECT) {
                    item.setSuspectCount(item.getSuspectCount() + count);
                } else {
                    switch (cla) {
                        case TB:
                            item.setTbCount(count);
                            break;
                        case DRTB:
                            item.setDrtbCount(count);
                            break;
                        case NTM:
                            item.setNtmCount(count);
                            break;
                        default:
                            throw new RuntimeException("Value not supported");
                    }
                }
            }
        }
    }


    private PlaceData findPlaceById(List<PlaceData> items, UUID id, PlaceData.PlaceType type) {
        for (PlaceData it : items) {
            if ((it.getId().equals(id)) && (it.getType() == type)) {
                return it;
            }
        }

        return null;
    }
}
