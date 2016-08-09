package org.msh.etbm.test;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.commons.models.ModelDAO;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.msh.etbm.commons.models.ModelDAOResult;
import org.msh.etbm.commons.models.db.RecordData;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by rmemoria on 9/5/15.
 */
@RestController
@RequestMapping("/api/test")
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

    @Autowired
    FormService formService;


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
        ModelDAOResult res = dao.insert(vals);

        // there are errors ?
        if (res.getErrors() != null) {
            s.append("\nTHERE ARE ERRORS:\n");
            for (FieldError err: res.getErrors().getFieldErrors()) {
                s.append(err.toString()).append('\n');
            }
            return s.toString();
        }

        s.append('\n').append("ID = ").append(res.getId()).append('\n');

        List<RecordData> lst = dao.findMany(true, null, null);

        s.append("DATA: \n");
        for (RecordData data: lst) {
            s.append(data).append('\n');
        }

        return s.toString();
    }

    @RequestMapping(value = "/load")
    @Authenticated
    public String dataLoad() {
        StringBuilder s = new StringBuilder();

        ModelDAO daoPatient = modelDAOFactory.create("patient");

        s.append("\n## CREATING##\n");
        Map<String, Object> p = new HashMap<>();
        p.put("name", "Ricardo");
        p.put("middleName", "Memoria");
        p.put("lastName", "Lima");
        p.put("birthDate", DateUtils.newDate(1971, 11, 13));
        p.put("gender", "MALE");
        ModelDAOResult res = daoPatient.insert(p);

        if (res.getErrors() != null) {
            s.append("\nERRORS:\n");
            for (ObjectError err: res.getErrors().getAllErrors()) {
                s.append(err).append('\n');
            }
            return s.toString();
        }
        s.append("\nID = ").append(res.getId());

        // find record
        s.append("\n\n## FindOne ##\n");
        RecordData p2 = daoPatient.findOne(res.getId());
        s.append("Name = ").append(p2.getValues().get("name"));

        // update record
        s.append("\n\n## Updating ###");
        Map<String, Object> vals = p2.getValues();
        vals.put("name", "Karla");
        daoPatient.update(p2.getId(), vals);

        // find to check the changes
        p2 = daoPatient.findOne(p2.getId());
        if (!p2.getValues().get("name").equals("Karla")) {
            s.append("Update didn't work");
            return s.toString();
        }

        // delete record
        s.append("\n\n## Deleting ##\n");
        daoPatient.delete(res.getId());
        s.append("  deleted...\n");

        return s.toString();
    }

    @RequestMapping(value = "/form")
    @Authenticated
    public FormInitResponse initForm() {
        Map<String, Object> doc = new HashMap<>();
        return formService.init("patient-edt", doc, false);
    }

    @RequestMapping(value = "/query")
    public String testQuery(@RequestParam(value = "foo", required = false) String foo) {
        return foo + "\n";
    }
}
