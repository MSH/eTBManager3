package org.msh.etbm.services.session.search;

import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.entities.Searchable;
import org.msh.etbm.db.enums.SearchableType;
import org.msh.etbm.services.offline.client.data.RecordChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Listener that creates, updates or remove a searchable based on an entity
 * This listener listen to to Sync importer.
 * Created by Mauricio on 15/12/2016.
 */
@Component
public class SearchableSyncListener extends SearchableBuilder {

    /**
     * Called when a Searchable entity was created while client sync
     * @param event
     */
    @Transactional
    @EventListener(condition = "T(org.msh.etbm.services.session.search.SearchableSyncListener).isInsertingSearchable(#event)")
    public void copyToSearchable(RecordChangeEvent event) {
        SearchableType type = SearchableType.findByTable(event.getTable());

        Class c = type.getParentClass() != null ? type.getParentClass() : type.getEntityClass();

        Object entity = entityManager.find(c, checkId(event.getId()));

        if (entity == null) {
            throw new RuntimeException("Entity doesn't exists");
        }

        Searchable searchable = buildSearchable(entity);

        entityManager.persist(searchable);
    }

    /**
     * Called when a Searchable entity was updated while client sync
     * @param event
     */
    @Transactional
    @EventListener(condition = "T(org.msh.etbm.services.session.search.SearchableSyncListener).isUpdatingSearchable(#event)")
    public void updateSearchable(RecordChangeEvent event) {
        // find searchable
        Searchable searchable = entityManager.find(Searchable.class, checkId(event.getId()));

        // find entity
        SearchableType type = SearchableType.findByTable(event.getTable());

        Class c = type.getParentClass() != null ? type.getParentClass() : type.getEntityClass();

        Object entity = entityManager.find(c, checkId(event.getId()));

        if (entity == null) {
            throw new RuntimeException("Entity doesn't exists");
        }

        searchable = buildSearchable(entity, searchable);

        entityManager.persist(searchable);

        if (entity instanceof AdministrativeUnit) {
            super.updateAdminUnitChilds((AdministrativeUnit) entity);
        }
    }

    /**
     * Called when a Searchable entity was removed while client sync
     * @param event
     */
    @Transactional
    @EventListener(condition = "T(org.msh.etbm.services.session.search.SearchableSyncListener).isDeletingSearchable(#event)")
    public void removeSearchable(RecordChangeEvent event) {
        Searchable searchable = entityManager.find(Searchable.class, checkId(event.getId()));
        if (searchable != null) {
            entityManager.remove(searchable);
        }
    }

    /**
     * Checks if the event is about a searchable inserting
     * @param event
     * @return
     */
    public static boolean isInsertingSearchable(RecordChangeEvent event) {
        if (event == null || event.getAction() == null || !"INSERT".equals(event.getAction())) {
            return false;
        }

        SearchableType type = SearchableType.findByTable(event.getTable());

        return type != null;
    }

    /**
     * Checks if the event is about a searchable updating
     * @param event
     * @return
     */
    public static boolean isUpdatingSearchable(RecordChangeEvent event) {
        if (event == null || event.getAction() == null || !"UPDATE".equals(event.getAction())) {
            return false;
        }

        SearchableType type = SearchableType.findByTable(event.getTable());

        return type != null;
    }

    /**
     * Checks if the event is about a searchable deleting
     * @param event
     * @return
     */
    public static boolean isDeletingSearchable(RecordChangeEvent event) {
        if (event == null || event.getAction() == null || !"DELETE".equals(event.getAction()) || event.getId() == null) {
            return false;
        }

        SearchableType type = SearchableType.findByTable(event.getTable());

        return type != null;
    }

    /**
     * Checks id type and convert it into a UUID type
     * @param id
     * @return
     */
    private UUID checkId(Object id) {
        if (id instanceof UUID) {
            return (UUID) id;
        } else if (id instanceof byte[]) {
            return ObjectUtils.bytesToUUID((byte[])id);
        }

        throw new RuntimeException("Id type is not being converted");
    }
}
