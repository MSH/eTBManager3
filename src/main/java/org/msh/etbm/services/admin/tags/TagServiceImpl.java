package org.msh.etbm.services.admin.tags;

import org.hibernate.exception.SQLGrammarException;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceContext;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.db.entities.Tag;
import org.msh.etbm.services.cases.tag.AutoGenTagsCasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import javax.persistence.PersistenceException;

/**
 * Created by rmemoria on 6/1/16.
 */
@Service
public class TagServiceImpl extends EntityServiceImpl<Tag, TagQueryParams> implements TagService {

    @Autowired
    AutoGenTagsCasesService autoGenTagsCasesService;

    @Override
    protected void buildQuery(QueryBuilder<Tag> builder, TagQueryParams queryParams) {
        // order by options
        builder.addDefaultOrderByMap(TagQueryParams.ORDERBY_NAME, "name");
        builder.addOrderByMap(TagQueryParams.ORDERBY_TYPE, "classification, name");

        // profiles
        builder.addDefaultProfile(TagQueryParams.PROFILE_DEFAULT, TagData.class);
        builder.addProfile(TagQueryParams.PROFILE_ITEM, SynchronizableItem.class);

        if (!queryParams.isIncludeDisabled()) {
            builder.addRestriction("active = true");
        }
    }

    @Override
    protected void afterSave(EntityServiceContext<Tag> context, ServiceResult res) {
        autoGenTagsCasesService.updateCases(context.getEntity().getId());
    }

    @Override
    public String getCommandType() {
        return CommandTypes.ADMIN_TAGS;
    }

    @Override
    protected void beforeSave(EntityServiceContext<Tag> context, Errors errors) {
        Tag tag = context.getEntity();

        if (!checkUnique(tag, "name", null)) {
            errors.rejectValue("name", Messages.NOT_UNIQUE);
        }

        String sqlTestMessage = testTagCondition(tag);

        if (sqlTestMessage != null) {
            errors.rejectValue("sqlCondition", Messages.NOT_VALID);
        }
    }

    /**
     * Check if the SQL condition given to the tag is correct
     *
     * @return null if no error is found or the error message.
     */
    public String testTagCondition(Tag tag) {
        String sqlErrorMessage = null;

        try {
            String sql = "select count(*) from tbcase a inner join patient p on p.id=a.patient_id where " + tag.getSqlCondition();
            getEntityManager().createNativeQuery(sql).getSingleResult();
        } catch (PersistenceException e) {
            sqlErrorMessage = "error";
            if (e.getCause() instanceof SQLGrammarException) {
                SQLGrammarException sqlerror = (SQLGrammarException) e.getCause();
                sqlErrorMessage = sqlerror.getSQLException().getMessage();
            }
        }

        return sqlErrorMessage;
    }
}