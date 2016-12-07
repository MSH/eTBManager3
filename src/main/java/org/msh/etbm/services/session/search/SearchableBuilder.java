package org.msh.etbm.services.session.search;

import org.msh.etbm.commons.PersonNameUtils;
import org.msh.etbm.db.Synchronizable;
import org.msh.etbm.db.WorkspaceEntity;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.db.enums.SearchableType;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

/**
 * Created by Mauricio on 21/10/2016.
 */
public abstract class SearchableBuilder {

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    protected PersonNameUtils personNameUtils;

    /**
     * Builds a NEW searchable instance and returns it, based on the entity workspace
     * @param entity
     * @return
     */
    protected Searchable buildSearchable(Object entity) {
        return this.buildSearchable(entity, null, getWorkspaceId(entity));
    }

    /**
     * Builds a searchable checking the type of the entity
     * If param searchable is null a new searchable will be instantiated
     * This method will run based on the entity workspace
     * @param entity
     * @param searchable
     * @return
     */
    protected Searchable buildSearchable(Object entity, Searchable searchable) {
        return buildSearchable(entity, searchable, getWorkspaceId(entity));
    }

    /**
     * Builds a searchable checking the type of the entity
     * If param searchable is null a new searchable will be instantiated
     * This method will run based on the workspace passed as parameter
     * @param entity
     * @param searchable
     * @return
     */
    protected Searchable buildSearchable(Object entity, Searchable searchable, UUID workspaceId) {
        // if searchable is null a searchable is being created
        if (searchable == null) {
            Workspace workspace = entityManager.find(Workspace.class, workspaceId);

            searchable = new Searchable();
            searchable.setWorkspace(workspace);
            searchable.setId(((Synchronizable) entity).getId());
        }

        if (entity instanceof TbCase) {
            return buildSearchable((TbCase)entity, searchable, workspaceId);
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

    /**
     * Builds a TbCase searchable
     * @param entity
     * @param searchable
     * @return
     */
    private Searchable buildSearchable(TbCase entity, Searchable searchable, UUID workspaceId) {
        searchable.setType("MALE".equals(entity.getPatient().getGender()) ? SearchableType.CASE_MAN : SearchableType.CASE_WOMAN );
        searchable.setUnit(entity.getOwnerUnit());
        searchable.setSubtitle(entity.getDiagnosisType().equals(DiagnosisType.SUSPECT) ? entity.getRegistrationNumber() : entity.getCaseNumber());
        searchable.setTitle(personNameUtils.displayPersonName(entity.getPatient().getName(), workspaceId));

        return searchable;
    }

    /**
     * Builds a Tbunit searchable
     * @param entity
     * @param searchable
     * @return
     */
    private Searchable buildSearchable(Tbunit entity, Searchable searchable) {
        searchable.setType(SearchableType.TBUNIT);
        searchable.setUnit(entity);
        searchable.setSubtitle(entity.getAddress().getAdminUnit().getFullDisplayName());
        searchable.setTitle(entity.getName());

        return searchable;
    }

    /**
     * Builds a Laboratory searchable
     * @param entity
     * @param searchable
     * @return
     */
    private Searchable buildSearchable(Laboratory entity, Searchable searchable) {
        searchable.setType(SearchableType.LAB);
        searchable.setUnit(entity);
        searchable.setSubtitle(entity.getAddress().getAdminUnit().getFullDisplayName());
        searchable.setTitle(entity.getName());

        return searchable;
    }

    /**
     * Builds a AdministrativeUnit searchable
     * @param entity
     * @param searchable
     * @return
     */
    private Searchable buildSearchable(AdministrativeUnit entity, Searchable searchable) {
        searchable.setType(SearchableType.ADMINUNIT);
        searchable.setUnit(null);
        searchable.setSubtitle(null);
        searchable.setTitle(entity.getFullDisplayName());

        return searchable;
    }

    /**
     * Builds a Workspace searchable
     * @param entity
     * @param searchable
     * @return
     */
    private Searchable buildSearchable(Workspace entity, Searchable searchable) {
        searchable.setType(SearchableType.WORKSPACE);
        searchable.setUnit(null);
        searchable.setSubtitle(null);
        searchable.setTitle(entity.getName());

        return searchable;
    }

    private UUID getWorkspaceId (Object entity) {
        if (entity instanceof Workspace) {
            return ((Workspace)entity).getId();
        } else if (entity instanceof WorkspaceEntity) {
            return ((WorkspaceEntity)entity).getWorkspace().getId();
        }

        throw new RuntimeException("Error creating searchable. Unsupported entity. " + entity.getClass());
    }

}
