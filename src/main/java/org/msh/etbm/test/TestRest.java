package org.msh.etbm.test;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.models.ModelDAO;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.msh.etbm.commons.models.ModelDAOResult;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.fields.*;
import org.msh.etbm.commons.models.db.DataLoader;
import org.msh.etbm.commons.models.db.RecordData;
import org.msh.etbm.commons.models.db.SQLQueryBuilder;
import org.msh.etbm.commons.models.db.SQLQueryInfo;
import org.msh.etbm.db.entities.Laboratory;
import org.msh.etbm.db.entities.Unit;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.db.repositories.WorkspaceRepository;
import org.msh.etbm.services.admin.units.UnitType;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.services.admin.units.data.UnitFormData;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.FieldError;
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
@RequestMapping("/test")
public class TestRest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Resource
    MessageSource messageSource;

    @Autowired
    DozerBeanMapper mapper;

    @Autowired
    DataSource dataSource;

    @Autowired
    ModelDAOFactory modelDAOFactory;


    @RequestMapping("/workspace")
    public List<Workspace> getWorkspace(@RequestParam("name") String name) {
        List<Workspace> lst = workspaceRepository.findByName(name);
        System.out.println(lst.get(0));
        return lst;
    }

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


    @RequestMapping(value = "/model")
    @Authenticated
    public String modelTest() {
        ModelDAO dao = modelDAOFactory.create("patient");

        StringBuilder s = new StringBuilder();

        Map<String, Object> vals = new HashMap<>();
        vals.put("name", "Ricardo3");
        vals.put("middleName", "Memória");
        vals.put("lastName", "Lima");
        vals.put("birthDate", DateUtils.newDate(1971, 11, 13));
        vals.put("gender", "MALE");
        ModelDAOResult res = dao.create(vals);

        // there are errors ?
        if (res.getErrors() != null) {
            s.append("\nTHERE ARE ERRORS:\n");
            for (FieldError err: res.getErrors().getFieldErrors()) {
                s.append(err.toString()).append('\n');
            }
            return s.toString();
        }

        s.append('\n').append("ID = ").append(res.getId()).append('\n');

        List<RecordData> lst = dao.findMany(true, null);

        s.append("DATA: \n");
        for (RecordData data: lst) {
            s.append(data).append('\n');
        }

        return s.toString();
    }

    @RequestMapping(value = "/load")
    public String dataLoad() {
        ModelDAO daoPatient = modelDAOFactory.create("patient");

        Model m = new Model();
        m.setName("unit");
        m.setTable("unit");

        StringField fldName = new StringField("name");
        StringField fldShortName = new StringField("shortName");
        StringField fldCustomId = new StringField("customId");
        BoolField fldActive = new BoolField("active");
        FKAdminUnitField fldAdminUnit = new FKAdminUnitField("adminUnitId", "adminunit_id");
        DateField fldInventoryStartDate = new DateField("invStartDate", "inventoryStartDate");
        FKUnitField fldAuthorizer = new FKUnitField("authorizerId", "authorizerunit_id");

        m.setFields(Arrays.asList(fldName, fldShortName, fldCustomId, fldActive, fldAdminUnit, fldInventoryStartDate, fldAuthorizer));

        SQLQueryBuilder gen = new SQLQueryBuilder();
        gen.setDisplaying(true);
        SQLQueryInfo data = gen.generate(m, null);

        StringBuilder s = new StringBuilder();
        s.append("## QUERY ##\n");
        s.append(data.getSql());

        DataLoader loader = new DataLoader();
        List<RecordData> list = loader.loadData(dataSource, data);

        s.append("\n\n## RESULT ##\n");
        for (RecordData record: list) {
            s.append('\n').append(record);
        }
        s.append('\n');

        return s.toString();
    }
}
