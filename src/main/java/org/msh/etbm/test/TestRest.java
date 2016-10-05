package org.msh.etbm.test;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.commons.indicators.IndicatorGenerator;
import org.msh.etbm.commons.indicators.IndicatorRequest;
import org.msh.etbm.commons.indicators.indicator.IndicatorDataTable;
import org.msh.etbm.commons.indicators.indicator.KeyDescriptor;
import org.msh.etbm.commons.indicators.indicator.client.IndicatorData;
import org.msh.etbm.commons.indicators.indicator.client.IndicatorDataConverter;
import org.msh.etbm.commons.indicators.variables.Variable;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.msh.etbm.db.entities.Laboratory;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.entities.Unit;
import org.msh.etbm.services.admin.units.UnitType;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.services.admin.units.data.UnitFormData;
import org.msh.etbm.services.cases.cases.CaseDetailedData;
import org.msh.etbm.services.cases.filters.CaseFilters;
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
    CaseFilters caseFilters;

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
    public StandardResult generateIndicator() {
        IndicatorGenerator gen = new IndicatorGenerator();

        IndicatorRequest req = new IndicatorRequest();
        req.setMainTable("tbcase");
        Variable varClassif = caseFilters.variableById(CaseFilters.CASE_CLASSIFICATION);
        Variable varState = caseFilters.variableById(CaseFilters.CASE_STATE);
        Variable varDiagType = caseFilters.variableById(CaseFilters.DIAGNOSIS_TYPE);

        List<Variable> vars = new ArrayList<>();
        vars.add(varClassif);
        vars.add(varDiagType);
        req.setColumnVariables(vars);
        req.setRowVariables(Collections.singletonList(varState));

        IndicatorDataTable tbl = gen.execute(dataSource, req);

        System.out.println("Column Keys:");
        for (KeyDescriptor kd: tbl.getColumnKeyDescriptors().getGroups()) {
            System.out.println(kd);
        }
        for (Object[] key: tbl.getColumnKeys()) {
            System.out.println(Arrays.toString(key));
        }
        System.out.println("Row Keys:");
        for (KeyDescriptor kd: tbl.getRowKeyDescriptors().getGroups()) {
            System.out.println(kd);
        }
        for (Object[] key: tbl.getRowKeys()) {
            System.out.println(Arrays.toString(key));
        }

        System.out.println("\nDATA:");
        for (int r = 0; r < tbl.getRowCount(); r++) {
            List vals = tbl.getRowValues(r);
            for (Object val: vals) {
                System.out.print(val + ", ");
            }
            System.out.println('\n');
        }

        IndicatorDataConverter conv = new IndicatorDataConverter();
        IndicatorData data = conv.convertFromDataTableIndicator(tbl);

        return new StandardResult(data, null, true);
    }
}
