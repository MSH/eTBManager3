package org.msh.etbm.services.session.search;

import org.msh.etbm.commons.entities.EntityServiceEvent;
import org.msh.etbm.db.Synchronizable;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.db.enums.Gender;
import org.msh.etbm.db.enums.SearchableType;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Mauricio on 05/10/2016.
 */
@Component
public class SearchableListener {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRequestService userRequestService;

    @Transactional
    @TransactionalEventListener(condition = "#event.isSearchableEntity() and #event.result.operation.name() == 'NEW'")
    public void copyToSearchable(EntityServiceEvent event) {
        Object entity = entityManager.find(event.getResult().getEntityClass(), event.getResult().getId());

        if (entity == null) {
            throw new RuntimeException("Entity doesn't exists");
        }

        Searchable searchable = buildSearchable(entity, null);

        entityManager.persist(searchable);
    }

    @Transactional
    @TransactionalEventListener(condition = "#event.isSearchableEntity() and #event.result.operation.name() == 'EDIT'")
    public void updateSearchable(EntityServiceEvent event) {
        Searchable searchable = entityManager.find(Searchable.class, event.getResult().getId());
        Object entity = entityManager.find(event.getResult().getEntityClass(), event.getResult().getId());

        if (entity == null) {
            throw new RuntimeException("Entity doesn't exists");
        }

        searchable = buildSearchable(entity, searchable);

        entityManager.persist(searchable);
    }

    @Transactional
    @TransactionalEventListener(condition = "#event.isSearchableEntity() and #event.result.operation.name() == 'DELETE'")
    public void removeSearchable(EntityServiceEvent event) {
        Searchable searchable = entityManager.find(Searchable.class, event.getResult().getId());
        if (searchable != null) {
            entityManager.remove(searchable);
        }
    }

    private Searchable buildSearchable(Object entity, Searchable searchable) {
        // if searchable is null a searchable is being created
        if (searchable == null) {
            Workspace workspace = entityManager.find(Workspace.class, userRequestService.getUserSession().getWorkspaceId());

            searchable = new Searchable();
            searchable.setWorkspace(workspace);
            searchable.setId(((Synchronizable) entity).getId());
        }

        if (entity instanceof TbCase) {
            return buildSearchable((TbCase)entity, searchable);
        }

        if (entity instanceof Tbunit) {
            return buildSearchable((Tbunit)entity, searchable);
        }

        if (entity instanceof Laboratory) {
            return buildSearchable((Laboratory)entity, searchable);
        }

        if (entity instanceof AdministrativeUnit) {
            return buildSearchable((AdministrativeUnit)entity, searchable);
        }

        if (entity instanceof Workspace) {
            return buildSearchable((Workspace)entity, searchable);
        }

        throw new RuntimeException("Entity is not a searchable");
    }

    private Searchable buildSearchable(TbCase entity, Searchable searchable) {
        // TODO: [MSANTOS] o icone na exibição esta trocado
        searchable.setType(Gender.MALE.equals(entity.getPatient().getGender()) ? SearchableType.CASE_MAN : SearchableType.CASE_WOMAN );
        searchable.setUnit(entity.getOwnerUnit());
        searchable.setSubtitle(entity.getCaseNumber());
        //TODO: [MSANTOS] COMO PEGAR O NOME? QUALÇ A ORDEM? MIDLE, LAST, FIRST?
        searchable.setTitle(entity.getPatient().getName().getName());

        return searchable;
    }

    private Searchable buildSearchable(Tbunit entity, Searchable searchable) {
        searchable.setType(SearchableType.TBUNIT);
        searchable.setUnit(entity);
        searchable.setSubtitle(entity.getAddress().getAdminUnit().getFullDisplayName());
        searchable.setTitle(entity.getName());

        return searchable;
    }

    private Searchable buildSearchable(Laboratory entity, Searchable searchable) {
        searchable.setType(SearchableType.LAB);
        searchable.setUnit(entity);
        searchable.setSubtitle(entity.getAddress().getAdminUnit().getFullDisplayName());
        searchable.setTitle(entity.getName());

        return searchable;
    }

    private Searchable buildSearchable(AdministrativeUnit entity, Searchable searchable) {
        searchable.setType(SearchableType.ADMINUNIT);
        searchable.setUnit(null);
        searchable.setSubtitle(null);
        searchable.setTitle(entity.getFullDisplayName());

        return searchable;
    }

    private Searchable buildSearchable(Workspace entity, Searchable searchable) {
        searchable.setType(SearchableType.WORKSPACE);
        searchable.setUnit(null);
        searchable.setSubtitle(null);
        searchable.setTitle(entity.getName());

        return searchable;
    }
}
