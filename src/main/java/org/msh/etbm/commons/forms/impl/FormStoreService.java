package org.msh.etbm.commons.forms.impl;

import org.msh.etbm.commons.forms.FormException;
import org.msh.etbm.commons.forms.data.Form;
import org.msh.etbm.commons.models.ModelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by rmemoria on 30/7/16.
 */
@Component
public class FormStoreService {

    public static final String CACHE_ID = "forms";

    @Autowired
    ModelManager modelManager;

    @Cacheable(cacheNames = CACHE_ID)
    public Form get(String formid, UUID workspaceId) {
        String resourcePath = "/forms/" + formid + ".json";
        Form frm = null;

        try {
            ClassPathResource res = new ClassPathResource(resourcePath);

            FormParser p = new FormParser();
            frm = p.parse(res.getInputStream());
        } catch (IOException e) {
            throw new FormException("Error when trying to load resource " + resourcePath);
        }

        return frm;
    }

    /**
     * Remove a custom form used by the database, if available
     * @param formid
     * @param workspaceId
     */
    @CacheEvict(cacheNames = CACHE_ID)
    public void remove(String formid, UUID workspaceId) {
        // TODO Ricardo: remove form from database
    }

    /**
     * Update the form used by the workspace
     * @param form
     * @param workspaceId
     */
    @CachePut(cacheNames = CACHE_ID)
    public void update(Form form, UUID workspaceId) {
        // TODO Ricardo: update form in the database
    }
}
