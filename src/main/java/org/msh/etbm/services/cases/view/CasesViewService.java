package org.msh.etbm.services.cases.view;

import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.*;

/**
 * Service to generate a view of the workspace about the case management
 * <p>
 * Created by rmemoria on 17/6/16.
 */
@Service
public class CasesViewService {

    @Autowired
    UserRequestService userRequestService;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    DataSource dataSource;


    /**
     * Generate a report of the workspace about the consolidated cases by administrative units
     * and the quantity of tags by case
     *
     * @return instance of {@link CasesViewResponse} containing the report view
     */
    @Transactional
    public CasesViewResponse generateView(UUID adminUnitId) {
        CasesViewResponse resp = new CasesViewResponse();

        // load information about the places
        List<PlaceData> places = loadPlaces(adminUnitId);

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

        AdministrativeUnit adminUnit =
                adminUnitId != null ?
                entityManager.find(AdministrativeUnit.class, adminUnitId) :
                null;

        // just load the units because it is level 0
        loadAdminUnits(places, adminUnit);

        if (adminUnitId != null) {
            loadUnits(places, adminUnit);
        }

        // sort items
        places.sort((it1, it2) -> it1.getName().compareToIgnoreCase(it2.getName()));

        return places;
    }


    private void loadAdminUnits(List<PlaceData> data, AdministrativeUnit adminUnit) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder s = new StringBuilder();

        s.append("select a.id, a.name, a.unitsCount, d.diagnosisType, d.classification, count(*) as total\n")
                .append("from administrativeunit a\n");

        if (adminUnit != null) {
            int level = adminUnit.getLevel();
            params.put("id", ObjectUtils.uuidAsBytes(adminUnit.getId()));
            String fname = "pid" + (level + 1);
            String fparent = "pid" + level;

            s.append("join administrativeunit b on b.id = a.id or b.").append(fname).append(" = a.id\n")
                    .append("join unit c on c.adminunit_id = b.id\n")
                    .append("join tbcase d on d.owner_unit_id = c.id\n")
                    .append("where a.").append(fname).append(" is null and a.").append(fparent).append(" = :id\n");
        } else {
            s.append("join administrativeunit b on b.pid0 = a.id\n")
                   .append("join unit c on c.adminunit_id = b.id\n")
                    .append("join tbcase d on d.owner_unit_id = c.id\n")
                    .append("where a.pid0 is null\n");
        }

        s.append("and a.workspace_id = :wsid and d.state < 2 and d.diagnosisType is not null\n")
                .append("group by a.id, a.name, d.diagnosisType, d.classification\n")
                .append("order by a.name");

        params.put("wsid", ObjectUtils.uuidAsBytes(userRequestService.getUserSession().getWorkspaceId()));

        NamedParameterJdbcTemplate templ = new NamedParameterJdbcTemplate(dataSource);
        List<Map<String, Object>> lst = templ.queryForList(s.toString(), params);

        mountList(lst, data, PlaceData.PlaceType.ADMINUNIT);
    }


    /**
     * Load case information about the units
     *
     * @param data      the data to include information into
     * @param adminUnit the administrative unit to get information from
     */
    private void loadUnits(List<PlaceData> data, AdministrativeUnit adminUnit) {
        String sql = "select a.id, a.name, 0 as unitsCount, b.diagnosisType, b.classification, count(*) as total " +
                "from unit a\n" +
                "inner join tbcase b on b.owner_unit_id = a.id\n" +
                "where b.state < 2 and b.diagnosisType is not null\n" +
                "and a.adminunit_id = :auId\n" +
                "group by a.id, a.name, b.diagnosisType, b.classification\n" +
                "order by a.name";

        NamedParameterJdbcTemplate templ = new NamedParameterJdbcTemplate(dataSource);
        Map<String, Object> p = new HashMap<>();
        p.put("auId", ObjectUtils.uuidAsBytes(adminUnit.getId()));
        List<Map<String, Object>> lst = templ.queryForList(sql, p);

        mountList(lst, data, PlaceData.PlaceType.UNIT);
    }

    /**
     * Mount the list of items from information retrieved from the database
     *
     * @param lst  list of values (source of data)
     * @param dest destination list to include information of place report
     * @param type the type of list (admin unit or TB unit)
     */
    protected void mountList(List<Map<String, Object>> lst, List<PlaceData> dest, PlaceData.PlaceType type) {
        for (Map<String, Object> vals : lst) {
            // check if there is any value
            long count = vals.get("total") != null ? ((Number) vals.get("total")).longValue() : 0;
            if (count == 0) {
                continue;
            }

            UUID id = ObjectUtils.bytesToUUID((byte[]) vals.get("id"));
            String name = (String) vals.get("name");

            PlaceData item = findPlaceById(dest, id, type);

            if (item == null) {
                item = new PlaceData();
                item.setId(id);
                item.setName(name);
                item.setType(type);

                int children = ((Number) vals.get("unitsCount")).intValue();
                item.setHasChildren(children > 0);

                dest.add(item);
            }

            if (vals.get("diagnosisType") != null && count > 0) {
                DiagnosisType dtype = DiagnosisType.values()[(Integer) vals.get("diagnosisType")];
                CaseClassification cla = CaseClassification.values()[(Integer) vals.get("classification")];

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
