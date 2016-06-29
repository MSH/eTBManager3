package org.msh.etbm.services.admin.userprofiles;


import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.db.entities.UserPermission;
import org.msh.etbm.db.entities.UserProfile;
import org.msh.etbm.services.security.permissions.Permission;
import org.msh.etbm.services.security.permissions.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of the User profile service for CRUD operations
 * <p>
 * Created by rmemoria on 26/1/16.
 */
@Service
public class UserProfileServiceImpl extends EntityServiceImpl<UserProfile, UserProfileQueryParams>
        implements UserProfileService {

    private static final String CMD_NAME = "profiles";

    @Autowired
    Permissions permissions;

    @Override
    protected void buildQuery(QueryBuilder<UserProfile> builder, UserProfileQueryParams queryParams) {
        // add profiles
        builder.addProfile(UserProfileQueryParams.PROFILE_ITEM, SynchronizableItem.class);
        builder.addDefaultProfile(UserProfileQueryParams.PROFILE_DEFAULT, SynchronizableItem.class);
        builder.addProfile(UserProfileQueryParams.PROFILE_DETAILED, UserProfileDetailedData.class);

        // add order by
        builder.addDefaultOrderByMap(UserProfileQueryParams.ORDERBY_NAME, "name");

        if (queryParams.getWorkspaceId() != null) {
            builder.setWorkspaceId(queryParams.getWorkspaceId());
        }
    }

    @Override
    protected void mapRequest(Object request, UserProfile entity) {
        if (entity.getPermissions().size() > 0) {
            entity.getPermissions().clear();
            removeOldPermissions(entity);
        }

        super.mapRequest(request, entity);
    }

    @Override
    protected void beforeSave(UserProfile entity, Errors errors) {
        List<Permission> toadd = new ArrayList<>();

        // check user permissions
        int index = 0;
        while (index < entity.getPermissions().size()) {
            UserPermission userperm = entity.getPermissions().get(index);

            // set the user profile
            userperm.setUserProfile(entity);

            // search for permission by its ID
            Permission p = permissions.find(userperm.getPermission());

            // permission not found ?
            if (p == null) {
                errors.reject("Invalid permission: " + userperm.getPermission());
                break;
            }

            // is there a parent permission ?
            if (p.getParent() != null) {
                // search for the parent permission in the list of user permissions
                UserPermission parent = entity.permissionByPermissionID(p.getParent().getId());

                // parent permission was not found ?
                if (parent == null) {
                    // create a new user permission
                    parent = new UserPermission();
                    parent.setPermission(p.getParent().getId());
                    parent.setCanChange(p.getParent().isChangeable());
                    // add it to the end of the list, so it will be analysed in the loop
                    entity.getPermissions().add(parent);
                }
            }

            index++;
        }
    }


    /**
     * Delete the previous permissions of an user profile being edited
     *
     * @param userProfile the user profile
     */
    protected void removeOldPermissions(UserProfile userProfile) {
        // is a new user profile? So do nothing
        if (userProfile.getId() == null) {
            return;
        }

        System.out.println("permissions = " + userProfile.getPermissions().size());
        getEntityManager().createQuery("delete from UserPermission where userProfile.id = :id")
                .setParameter("id", userProfile.getId())
                .executeUpdate();
    }

    @Override
    public String getFormCommandName() {
        return CMD_NAME;
    }


    @Override
    public List<Item> execFormRequest(FormRequest req) {
        UserProfileQueryParams qry = new UserProfileQueryParams();
        qry.setProfile(UserProfileQueryParams.PROFILE_ITEM);

        UUID wsId = req.getIdParam("workspaceId");
        qry.setWorkspaceId(wsId);

        QueryResult res = findMany(qry);
        return res.getList();
    }
}
