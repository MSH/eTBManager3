package org.msh.etbm;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.db.repositories.WorkspaceRepository;
import org.msh.etbm.services.admin.admunits.AdminUnitRequest;
import org.msh.etbm.services.admin.units.UnitRequest;
import org.msh.etbm.services.admin.units.UnitType;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

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

    @Resource
    DozerBeanMapper mapper;

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

        UnitRequest req = new UnitRequest();
        req.setType(UnitType.LAB);
        Unit unit = mapper.map(req, Unit.class);
        return unit.getClass().getName();
    }
}
