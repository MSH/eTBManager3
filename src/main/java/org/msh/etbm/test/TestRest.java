package org.msh.etbm.test;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.commons.indicators.indicator.client.IndicatorData;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.msh.etbm.db.entities.Laboratory;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.entities.Unit;
import org.msh.etbm.services.admin.units.UnitType;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.services.admin.units.data.UnitFormData;
import org.msh.etbm.services.cases.cases.data.CaseDetailedData;
import org.msh.etbm.services.cases.filters.CaseFilters;
import org.msh.etbm.services.cases.indicators.CaseIndicatorRequest;
import org.msh.etbm.services.cases.indicators.CaseIndicatorResponse;
import org.msh.etbm.services.cases.indicators.CaseIndicatorsService;
import org.msh.etbm.services.offline.server.ServerFileGenerator;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by rmemoria on 9/5/15.
 */
@RestController
@RequestMapping("/api/test")
public class TestRest {

    @PersistenceContext
    EntityManager entityManager;

    @Resource
    MessageSource messageSource;

    @Autowired
    DozerBeanMapper mapper;

    @Autowired
    DataSource dataSource;

    @Autowired
    ModelDAOFactory modelDAOFactory;

    @Autowired
    FormService formService;

    @Autowired
    CaseIndicatorsService caseIndicatorsService;

    @Autowired
    ServerFileGenerator syncFileGenerator;

    @Autowired
    UserRequestService userRequestService;


    @RequestMapping("/message")
    public String getMessage() {
        Locale locale = LocaleContextHolder.getLocale();
        String s = messageSource.getMessage("NotNull", null, locale);
        return s + "  ";
    }


    @RequestMapping("/mappintest")
    public String mappingTest() {
        Laboratory lab = new Laboratory();
        UnitData data = mapper.map(lab, UnitData.class);
        System.out.println(data);

        UnitFormData req = new UnitFormData();
        req.setUnitType(UnitType.LAB);
        Unit unit = mapper.map(req, Unit.class);
        return unit.getClass().getName();
    }

    @RequestMapping(value = "/map", method = RequestMethod.POST)
    public StandardResult mapTest(@RequestBody Map<String, Object> map) {
        for (String key : map.keySet()) {
            Object val = map.get(key);
            System.out.println(key + " = " + val);
        }
        return new StandardResult("ok", null, true);
    }



    @RequestMapping(value = "/form")
    @Authenticated
    public FormInitResponse initForm() {
        Map<String, Object> doc = new HashMap<>();
        return formService.init("patient-edt", doc, false);
    }

    @RequestMapping(value = "/form/readonly/{id}")
    @Authenticated
    public FormInitResponse initFormReadOnly(@PathVariable UUID id) {
        TbCase tbcase = entityManager.find(TbCase.class, id);

        String formid = "newnotif-";
        formid = formid.concat(tbcase.getDiagnosisType().name().toLowerCase()).concat("-");
        formid = formid.concat(tbcase.getClassification().name().toLowerCase());

        CaseDetailedData caseData = mapper.map(tbcase, CaseDetailedData.class);

        return formService.init(formid, caseData, true);
    }

    @RequestMapping(value = "/query")
    public String testQuery(@RequestParam(value = "foo", required = false) String foo) {
        return foo + "\n";
    }

    @RequestMapping(value = "/indicator")
    @Authenticated
    public StandardResult generateIndicator() {
        CaseIndicatorRequest req = new CaseIndicatorRequest();
        String[] colvars = { CaseFilters.CASE_CLASSIFICATION, CaseFilters.DIAGNOSIS_TYPE };
        String[] rowVars = { CaseFilters.CASE_STATE };

        req.setColumnVariables(Arrays.asList(colvars));
        req.setRowVariables(Arrays.asList(rowVars));

        CaseIndicatorResponse resp = caseIndicatorsService.execute(req);

        IndicatorData ind = resp.getIndicator();

        System.out.println("Column Keys:");
        for (Map<String, String> map: ind.getColumns().getDescriptors()) {
            System.out.println(map.toString());
        }

        System.out.println("\nDATA:");
        for (List row: ind.getValues()) {
            System.out.println(row);
        }

        return new StandardResult(ind, null, true);
    }


    @RequestMapping("/syncfile")
    @Authenticated
    public StandardResult generateSyncFile() throws IOException {
        UUID unitId = userRequestService.getUserSession().getUnitId();
        UUID workspaceId = userRequestService.getUserSession().getWorkspaceId();

        File file = syncFileGenerator.generate(unitId, workspaceId, Optional.empty());

        return new StandardResult(file.toString(), null, true);
    }
}
