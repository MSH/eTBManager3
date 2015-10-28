package org.msh.etbm;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.entities.CountryStructure;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.db.repositories.WorkspaceRepository;
import org.msh.etbm.services.admin.admunits.AdminUnitRequest;
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

    @RequestMapping("/getuuid")
    public UUID getUUID() {
        return UUID.randomUUID();
    }

    @RequestMapping("/setuuid")
    public void setUUID(@RequestParam("id") UUID id) {
        System.out.println(id);
    }

    @RequestMapping("/countws")
    @Transactional
    public Long countWorkspaces() {
        Long res = workspaceRepository.count();
        return res;
    }

    @RequestMapping("/custom")
    public AdminUnitRequest test() {
        CountryStructure cs = (CountryStructure)entityManager
                .createQuery("from CountryStructure")
                .setMaxResults(1)
                .getSingleResult();

        AdminUnitRequest req = new AdminUnitRequest();
        req.setCsId(cs.getId());
        req.setName("Rio de Janeiro");

        AdministrativeUnit au = mapper.map(req, AdministrativeUnit.class);
        System.out.println(au.getCountryStructure().getName());
        return req;
    }
}
