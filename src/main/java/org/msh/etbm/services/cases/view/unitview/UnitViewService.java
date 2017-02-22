package org.msh.etbm.services.cases.view.unitview;

import org.apache.commons.collections.map.HashedMap;
import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.entities.ExamMicroscopy;
import org.msh.etbm.db.entities.ExamXpert;
import org.msh.etbm.db.entities.Patient;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.db.enums.MicroscopyResult;
import org.msh.etbm.db.enums.XpertResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service that returns the data to create the unit view of the case management module
 * Created by rmemoria on 3/6/16.
 */
@Service
public class UnitViewService {

    private EntityManager entityManager;

    private Messages messages;

    private DataSource dataSource;


    /**
     * Default constructor
     * @param entityManager injected by the container manager
     * @param messages  injected by the container manager
     * @param dataSource  injected by the container manager
     */
    public UnitViewService(EntityManager entityManager, Messages messages, DataSource dataSource) {
        this.entityManager = entityManager;
        this.messages = messages;
        this.dataSource = dataSource;
    }


    /**
     * Get data related to the unit view of the cases module
     *
     * @param unitId the unit ID to get information from
     * @return instance of {@link UnitViewData} containing the unit data
     */
    @Transactional
    public UnitViewData getUnitView(UUID unitId) {
        UnitViewData data = new UnitViewData();

        loadCases(unitId, data);

        return data;
    }

    /**
     * Load the cases to load into unit view
     *
     * @param unitId the unit ID to get the cases from
     * @param data   the data to receive the cases
     */
    private void loadCases(UUID unitId, UnitViewData data) {
        List<TbCase> lst = entityManager.createQuery("from TbCase c " +
                "left join fetch c.comorbidities " +
                "join fetch c.patient where (c.ownerUnit.id = :unitId or c.transferOutUnit = :unitId) " +
                "and c.state in (:st1, :st2) " +
                "order by c.patient.name.name")
                .setParameter("unitId", unitId)
                .setParameter("st1", CaseState.ONTREATMENT)
                .setParameter("st2", CaseState.NOT_ONTREATMENT)
                .getResultList();

        data.setPresumptives(new ArrayList<>());
        data.setDrtbCases(new ArrayList<>());
        data.setTbCases(new ArrayList<>());
        data.setNtmCases(new ArrayList<>());

        for (TbCase tbcase : lst) {
            // is a case a presumptive one?
            if (tbcase.getDiagnosisType() == DiagnosisType.SUSPECT) {
                data.getPresumptives().add(createPresumptiveData(tbcase));
            } else {
                // get confirmed case data
                ConfirmedCaseData caseData = createConfirmedData(tbcase);

                switch (tbcase.getClassification()) {
                    case DRTB: data.getDrtbCases().add(caseData);
                        break;
                    case NTM: data.getNtmCases().add(caseData);
                        break;
                    default: data.getTbCases().add(caseData);
                }
            }
        }

        loadPresumptiveExamResults(data.getPresumptives());
    }

    /**
     * Create a presumptive case record based on the data loaded from the query
     * @param tbcase the instance of the TbCase loaded from the query
     * @return instance of {@link PresumptiveCaseData}
     */
    private PresumptiveCaseData createPresumptiveData(TbCase tbcase) {
        PresumptiveCaseData data = createCaseData(tbcase, PresumptiveCaseData.class);

        data.setCaseNumber(tbcase.getRegistrationNumber());

        return data;
    }

    /**
     * Load at once the data from the presumptive cases, if any available
     * @param lst
     */
    private void loadPresumptiveExamResults(List<PresumptiveCaseData> lst) {
        if (lst == null || lst.isEmpty()) {
            return;
        }

        // load microscopy results
        loadMicroscopyResults(lst);
        loadXpertResults(lst);
    }

    /**
     * Load the microscopy results of the list of presumptives
     * @param lst
     */
    private void loadMicroscopyResults(List<PresumptiveCaseData> lst) {
        StringBuilder sql = new StringBuilder("select a.case_id, a.result from exammicroscopy a\n" +
                "where a.event_date = (select min(b.event_date) from exammicroscopy b where b.case_id = a.case_id)\n" +
                "and a.case_id in (");

        String join = "";
        List params = new ArrayList();
        for (PresumptiveCaseData it: lst) {
            sql.append(join).append("?");
            params.add(ObjectUtils.uuidAsBytes(it.getId()));
            join = ",";
        }

        sql.append(")");

        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.query(sql.toString(), params.toArray(), resultSet -> {
            PresumptiveCaseData caseData = findPresumptive(lst, resultSet.getBytes(1));
            MicroscopyResult res = MicroscopyResult.values()[resultSet.getInt(2)];
            caseData.setMicroscopyResult(new Item<>(res, messages.get(res.getMessageKey())));
        });
    }

    /**
     * Search for a presumptive in the list of presumptives by its id in byte representation
     * @param lst
     * @param id
     * @return
     */
    private PresumptiveCaseData findPresumptive(List<PresumptiveCaseData> lst, byte[] id) {
        UUID uuid = ObjectUtils.bytesToUUID(id);

        Optional<PresumptiveCaseData> res = lst.stream()
                .filter(it ->  it.getId().equals(uuid))
                .findFirst();

        if (!res.isPresent()) {
            throw new IllegalArgumentException("Case not found");
        }

        return res.get();
    }

    /**
     * Load the Xpert results, if available, of all presumptives in the given list
     * @param lst
     */
    private void loadXpertResults(List<PresumptiveCaseData> lst) {
        StringBuilder sql = new StringBuilder("select a.case_id, a.result from examxpert a\n" +
                "where a.event_date = (select min(b.event_date) from examxpert b where b.case_id = a.case_id)\n" +
                "and a.case_id in (");

        String join = "";
        List params = new ArrayList();
        for (PresumptiveCaseData it: lst) {
            sql.append(join).append("?");
            params.add(ObjectUtils.uuidAsBytes(it.getId()));
            join = ",";
        }

        sql.append(")");

        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.query(sql.toString(), params.toArray(), resultSet -> {
            PresumptiveCaseData caseData = findPresumptive(lst, resultSet.getBytes(1));
            XpertResult res = XpertResult.values()[resultSet.getInt(2)];
            caseData.setXpertResult(new Item<>(res, messages.get(res.getMessageKey())));
        });

    }

    private ConfirmedCaseData createConfirmedData(TbCase tbcase) {
        ConfirmedCaseData data = createCaseData(tbcase, ConfirmedCaseData.class);

        data.setCaseNumber(tbcase.getCaseNumber());
        if (tbcase.getInfectionSite() != null) {
            data.setInfectionSite(new Item(tbcase.getInfectionSite(), messages.get(tbcase.getInfectionSite().getMessageKey())));
        }
        data.setRegistrationGroup(tbcase.getRegistrationGroup());

        // is case on treatment ?
        if (tbcase.isOnTreatment()) {
            data.setIniTreatmentDate(tbcase.getTreatmentPeriod().getIniDate());
            data.setTreatmentProgress(calcTreatmentProgress(tbcase.getTreatmentPeriod()));
        }

        return data;
    }

    /**
     * Presumptive and confirmed share common information. This method creates the data object
     * to store data about a confirmed or a presumptive case
     *
     * @param tbcase the TB case data
     * @param clazz  data class to be instantiated, inherited from {@link CommonCaseData}
     * @param <K>
     * @return the instance of the data class, with common data filled
     */
    private <K extends CommonCaseData> K createCaseData(TbCase tbcase, Class<K> clazz) {
        K data = ObjectUtils.newInstance(clazz);

        Patient p = tbcase.getPatient();

        data.setId(tbcase.getId());
        data.setName(p.getName());
        data.setGender(p.getGender());
        data.setRegistrationDate(tbcase.getRegistrationDate());

        return data;
    }

    /**
     * Calculate the treatment progress based on the today's date
     *
     * @param period
     * @return
     */
    private int calcTreatmentProgress(Period period) {
        Date today = DateUtils.getDate();

        // the date today is not inside the treatment period ?
        if (!period.isDateInside(today)) {
            return today.before(period.getIniDate()) ? 0 : 100;
        }

        int days = period.getDays();

        Period p = new Period(period.getIniDate(), today);
        int prog = (p.getDays() * 100) / days;

        return prog;
    }
}
