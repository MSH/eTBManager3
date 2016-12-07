package org.msh.etbm.services.init.demodata;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.JsonUtils;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.services.admin.substances.SubstanceData;
import org.msh.etbm.services.admin.tags.TagData;
import org.msh.etbm.services.cases.tag.AutoGenTagsCasesService;
import org.msh.etbm.services.init.demodata.data.*;
import org.msh.etbm.services.session.search.SearchableCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Spring service to create demonstration data defined on json file, when initializing the workspace.
 * Created by msantos on 20/10/16.
 */
@Service
public class DemonstrationDataCreator {

    @Autowired
    DozerBeanMapper mapper;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    AutoGenTagsCasesService autoGenTagsCasesService;

    @Autowired
    SearchableCreator searchableCreator;

    // Medicines with custom id
    List<Medicine> relevantMedicines;

    /**
     * Creates demonstration data based on json file
     */
    @Transactional
    public void create(UUID workspaceId) {
        Workspace workspace = entityManager.find(Workspace.class, workspaceId);

        // read the template data
        DemonstrationDataTemplate template = JsonUtils.parseResource("/templates/json/demonstration-data-template.json", DemonstrationDataTemplate.class);

        insertSubstances(template, workspace);

        insertMedicines(template, workspace);

        relevantMedicines = entityManager.createQuery("from Medicine where customId is not null").getResultList();

        insertRegimens(template, workspace);

        insertCases(template, workspace);

        insertTags(template, workspace);

        searchableCreator.create(TbCase.class);

        runAutoGenTags(workspace);
    }

    /**
     * Insert substances defined on json file, on database
     * @param template
     * @param workspace
     */
    private void insertSubstances(DemonstrationDataTemplate template, Workspace workspace) {
        for (SubstanceData subData : template.getSubstances()) {
            Substance s = new Substance();
            mapper.map(subData, s);

            s.setWorkspace(workspace);
            s.setActive(true);
            entityManager.persist(s);
        }

        entityManager.flush();
    }

    /**
     * Get a list of substances based on a string parameter, where substances are declared by its abbrevName separated by commas
     * @param allSubstances
     * @param substancesAbbrevName
     * @return
     */
    private List<Substance> getSubstances(List<Substance> allSubstances, String substancesAbbrevName) {
        if (substancesAbbrevName == null || substancesAbbrevName.isEmpty()) {
            return null;
        }

        List<Substance> ret = new ArrayList<>();
        String[] substStrList = substancesAbbrevName.split(",");

        for (String substString : substStrList) {
            for (Substance substance : allSubstances) {
                if (substString.equals(substance.getShortName())) {
                    ret.add(substance);
                }
            }
        }

        return ret;
    }

    /**
     * Insert medicines defined on json file, on database
     * @param template
     * @param workspace
     */
    private void insertMedicines(DemonstrationDataTemplate template, Workspace workspace) {
        List<Substance> allSubstances = entityManager.createQuery("from Substance").getResultList();

        for (MedicineDemoData medData : template.getMedicines()) {
            Medicine m = new Medicine();
            mapper.map(medData, m);

            m.setWorkspace(workspace);
            m.setActive(true);
            m.setSubstances(getSubstances(allSubstances, medData.getSubstancesAbbrevName()));
            entityManager.persist(m);
        }

        entityManager.flush();
    }

    /**
     * Finds medicine by its customId
     * @param customId
     * @return
     */
    private Medicine findMedicineByCustomId(String customId) {
        if (relevantMedicines == null || customId == null || customId.isEmpty()) {
            return null;
        }

        for (Medicine m : relevantMedicines) {
            if (customId.equals(m.getCustomId())) {
                return m;
            }
        }

        return null;
    }

    /**
     * Insert regimens defined on json file, on database
     * @param template
     * @param workspace
     */
    private void insertRegimens(DemonstrationDataTemplate template, Workspace workspace) {
        for (RegimenDemoData regimenData : template.getRegimens()) {
            Regimen r = new Regimen();
            mapper.map(regimenData, r);

            r.setWorkspace(workspace);
            r.setActive(true);
            entityManager.persist(r);

            r.setMedicines(new ArrayList<>());

            for (MedicineRegimenDemoData medRegimenData : regimenData.getMedicineRegimenDemoDatas()) {
                MedicineRegimen mr = new MedicineRegimen();
                mapper.map(medRegimenData, mr);
                mr.setMedicine(findMedicineByCustomId(medRegimenData.getMedicineCustomId()));
                mr.setRegimen(r);
                r.getMedicines().add(mr);
            }

            entityManager.persist(r);
        }

        entityManager.flush();
    }

    /**
     * insert tbcases defined on json file, on database
     * @param template
     * @param workspace
     */
    private void insertCases(DemonstrationDataTemplate template, Workspace workspace) {
        for (CaseDemoData caseData  : template.getTbcases()) {
            CaseComorbidities comorbidities = null;
            TbCase c = new TbCase();
            mapper.map(caseData, c);

            c.setWorkspace(workspace);
            c.getPatient().setWorkspace(workspace);
            c.setOwnerUnit((Tbunit) findSingleEntity(Tbunit.class, "name like '" + caseData.getOwnerUnitName() + "'"));
            c.setNotificationUnit((Tbunit) findSingleEntity(Tbunit.class, "name like '" + caseData.getNotifUnitName() + "'"));
            c.getNotifAddress().setAdminUnit((AdministrativeUnit) findSingleEntity(AdministrativeUnit.class, "name like '" + caseData.getNotifAddrAdminUnitName() + "'"));
            c.getCurrentAddress().setAdminUnit((AdministrativeUnit) findSingleEntity(AdministrativeUnit.class, "name like '" + caseData.getCurrAddrAdminUnitName() + "'"));

            if (caseData.getCustomRegimenId() != null && !caseData.getCustomRegimenId().isEmpty()) {
                c.setRegimen((Regimen) findSingleEntity(Regimen.class, "customId like '" + caseData.getCustomRegimenId() + "'"));
            }

            if (c.getComorbidities() != null) {
                comorbidities = c.getComorbidities();
                c.setComorbidities(null);
            }

            entityManager.persist(c.getPatient());
            entityManager.persist(c);

            if (comorbidities != null) {
                comorbidities.setTbCase(c);
                c.setComorbidities(comorbidities);

                entityManager.persist(comorbidities);
            }

            //create prescriptions
            if (caseData.getPrescriptionDemoData() != null) {
                c.setPrescriptions(new ArrayList<>());
                for (PrescriptionDemoData prescData : caseData.getPrescriptionDemoData()) {
                    PrescribedMedicine p = new PrescribedMedicine();
                    mapper.map(prescData, p);

                    p.setProduct(findMedicineByCustomId(prescData.getCustomMedicineId()));
                    p.setTbcase(c);
                    c.getPrescriptions().add(p);
                }
            }

            //create treatmentHealthUnit
            if (caseData.getTreatmentUnitDemoDatas() != null) {
                c.setTreatmentUnits(new ArrayList<>());
                for (TreatmentUnitDemoData treatUnitData : caseData.getTreatmentUnitDemoDatas()) {
                    TreatmentHealthUnit t = new TreatmentHealthUnit();
                    mapper.map(treatUnitData, t);

                    t.setTbcase(c);
                    t.setTbunit((Tbunit) findSingleEntity(Tbunit.class, "name like '" + treatUnitData.getUnitName() + "'"));

                    c.getTreatmentUnits().add(t);
                }
            }

            entityManager.persist(c);
        }

        entityManager.flush();
    }

    /**
     * Find an entity based on a condition
     * @param entityClass
     * @param condition
     * @return
     */
    private Object findSingleEntity(Class entityClass, String condition) {
        List<Object> lst = entityManager.createQuery("from " + entityClass.getSimpleName() + " where " + condition)
                .getResultList();

        if (lst == null || lst.size() < 1) {
            throw new RuntimeException("Entity not found: " + entityClass.getSimpleName() + " condition: " + condition);
        }

        return lst.get(0);
    }

    /**
     * Insert tags defined on json file, on database
     * @param template
     * @param workspace
     */
    private void insertTags(DemonstrationDataTemplate template, Workspace workspace) {
        for (TagData tagData : template.getTags()) {
            Tag t = new Tag();
            mapper.map(tagData, t);

            t.setWorkspace(workspace);
            t.setActive(true);
            entityManager.persist(t);
        }

        entityManager.flush();
    }

    /**
     * Runs sql of auto generated tags, so they will be automatically related to the cases that fits on this condition
     * @param workspace
     */
    private void runAutoGenTags(Workspace workspace) {
        List<Tag> tags = entityManager.createQuery("from Tag where sqlCondition is not null").getResultList();

        if (tags == null || tags.size() <= 0) {
            return;
        }

        for (Tag t : tags) {
            if (t.getSqlCondition() != null || !t.getSqlCondition().isEmpty()) {
                autoGenTagsCasesService.updateCases(t.getId(), workspace.getId());
            }
        }
    }
}
