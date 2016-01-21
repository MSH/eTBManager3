package org.msh.etbm.test;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.db.Address;
import org.msh.etbm.db.entities.Laboratory;
import org.msh.etbm.db.entities.Unit;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.db.repositories.WorkspaceRepository;
import org.msh.etbm.services.admin.units.UnitFormData;
import org.msh.etbm.services.admin.units.UnitType;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.web.api.StandardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    Validator validator;

    @RequestMapping("/hello")
    public String hello() {
        return "Hello nice world!";
    }

    @RequestMapping("/workspaces")
    public List<Workspace> getWorkspaces() {
        return entityManager.createQuery("from Workspace").getResultList();
    }


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
        for (String key: map.keySet()) {
            Object val = map.get(key);
            System.out.println(key + " = " + val);
        }
        return new StandardResult("ok", null, true);
    }

    @RequestMapping("/optionaltest")
    public StandardResult optionalTest(@RequestBody DataRequest data, BindingResult bindingResult) {
        System.out.println("ERRORS = " + bindingResult.hasErrors());

        // recover the entity
        DataEntity ent = new DataEntity();
        ent.setName("Karla");
        ent.setAge(33);
        Address addr = new Address();
        addr.setAddress("R. Tonelero");
        addr.setComplement("Copacabana");
        ent.setAddress(addr);

        // map it to a new request
        DataRequest req2 = mapper.map(ent, DataRequest.class);

        // copy request data to req 2
        mapper.map(data, req2);

        // validate request 2 containing all merged data
        validator.validate(req2, bindingResult);

        if (bindingResult.hasErrors()) {
            for (FieldError err: bindingResult.getFieldErrors()) {
                System.out.println(err);
            }
            return new StandardResult(false, null, false);
        }


        mapper.map(data, ent);
        return new StandardResult(ent, null, true);
    }
}
