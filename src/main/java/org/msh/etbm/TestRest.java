package org.msh.etbm;

import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.rest.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

/**
 * Created by rmemoria on 9/5/15.
 */
@RestController
public class TestRest {

    @Autowired
    EntityManager entityManager;

    @RequestMapping("/hello")
    public String hello() {
        return "Hello nice world!";
    }

    @RequestMapping("/workspaces")
    public List<Workspace> getWorkspaces() {
        return entityManager.createQuery("from Workspace").getResultList();
    }

    @RequestMapping("/getuuid")
    public UUID getUUID() {
        return UUID.randomUUID();
    }

    @RequestMapping("/setuuid")
    public void setUUID(@RequestParam("id") UUID id) {
        System.out.println(id);
    }
}
