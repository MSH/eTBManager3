package org.msh.etbm.services.admin.admunits.impl;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.db.entities.CountryStructure;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.db.repositories.CountryStructureRepository;
import org.msh.etbm.services.admin.admunits.AdminUnitRequest;
import org.msh.etbm.services.admin.admunits.CountryStructureData;
import org.msh.etbm.services.admin.admunits.CountryStructureRequest;
import org.msh.etbm.services.admin.admunits.CountryStructureService;
import org.msh.etbm.services.usersession.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of services to handle country structures
 * Created by rmemoria on 24/10/15.
 */
@Service
public class CountryStructureImpl implements CountryStructureService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    DozerBeanMapper mapper;

    @Autowired
    UserSession userSession;

    @Autowired
    CountryStructureRepository repository;


    @Override
    @CommandLog(type = "create", handler = CountryStructureLogHandler.class)
    @Transactional
    public UUID create(@Valid CountryStructureRequest req) {
        CountryStructure cs = new CountryStructure();
        checkUnique(req, null);
        mapper.map(req, cs);

        cs.setWorkspace(getWorkspace());

        repository.save(cs);
        return cs.getId();
    }

    @Override
    @CommandLog(type = "update", handler = CountryStructureLogHandler.class)
    @Transactional
    public UUID update(UUID id, @Valid CountryStructureRequest req) {
        CountryStructure cs = repository.findOne(id);

        checkUnique(req, id);
        checkWorkspace(cs);
        mapper.map(req, cs);

        repository.save(cs);

        return cs.getId();
    }


    @Override
    @CommandLog(type = "delete", handler = CountryStructureLogHandler.class)
    @Transactional
    public CountryStructureData delete(UUID id) {
        CountryStructure cs = entityManager.find(CountryStructure.class, id);
        checkWorkspace(cs);
        return null;
    }

    @Override
    public CountryStructureData get(UUID id) {
        CountryStructure cs = entityManager.find(CountryStructure.class, id);
        CountryStructureData data = mapper.map(cs, CountryStructureData.class);
        return data;
    }

    @Override
    public List<CountryStructureData> query() {
        return null;
    }

    /**
     * Check if this is the correct workspace
     * @param cs the country structure to be saved
     */
    private void checkWorkspace(CountryStructure cs) {
        Workspace ws = cs.getWorkspace();
        if (ws != null && !ws.getId().equals(userSession.getUserWorkspace().getWorkspace().getId())) {
            throw new IllegalArgumentException("Invalid workspace");
        }
    }

    /**
     * Get the current workspace in use by the current user
     * @return instance of the Workspace
     */
    protected Workspace getWorkspace() {
        Workspace ws = entityManager.find(Workspace.class, userSession.getUserWorkspace().getWorkspace().getId());
        return ws;
    }

    /**
     * Check if there is a country structure with the same name in the given level
     * @param req the request to update or save a country structure
     * @param id the id of the country structure, in case of an update, or null if it is a request for a new country structure
     */
    protected void checkUnique(CountryStructureRequest req, UUID id) {
        List<CountryStructure> lst = repository.findByNameAndWorkspaceIdAndLevel(req.getName(),
                userSession.getUserWorkspace().getWorkspace().getId(),
                req.getLevel());

        if (id == null && lst.size() > 0) {
            throw new IllegalArgumentException("There is already a record with the given name");
        }

        for (CountryStructure cs: lst) {
            if (!cs.getId().equals(id)) {
                throw new IllegalArgumentException("There is already a record with the given name");
            }
        }
    }
}
