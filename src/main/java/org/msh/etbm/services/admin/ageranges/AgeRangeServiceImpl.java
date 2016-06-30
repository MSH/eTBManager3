package org.msh.etbm.services.admin.ageranges;

import org.msh.etbm.Messages;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.db.entities.AgeRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.List;

/**
 * Created by rmemoria on 6/1/16.
 */
@Service
public class AgeRangeServiceImpl extends EntityServiceImpl<AgeRange, AgeRangesQueryParams>
        implements AgeRangeService {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    @Autowired
    Messages messages;

    @Override
    protected void buildQuery(QueryBuilder<AgeRange> builder, AgeRangesQueryParams queryParams) {
        builder.addDefaultProfile(AgeRangesQueryParams.PROFILE_DEFAULT, AgeRangeData.class);

        builder.addDefaultOrderByMap(AgeRangesQueryParams.ORDERBY_AGE, "iniAge");
        builder.setOrderByKey(AgeRangesQueryParams.PROFILE_DEFAULT);
    }

    @Override
    public String getCommandType() {
        return CommandTypes.ADMIN_AGERANGES;
    }

    @Override
    protected void beforeSave(AgeRange entity, Errors errors) {
        if (entity.getIniAge() < 0) {
            errors.rejectValue("iniDate", Messages.NOT_VALID);
        }

        if (entity.getEndAge() < 0) {
            errors.rejectValue("endDate", Messages.NOT_VALID);
        }

        // check ranges
        if (entity.getIniAge() != 0 && entity.getEndAge() != 0 && entity.getIniAge() >= entity.getEndAge()) {
            errors.reject("admin.ageranges.msgerror1");
            return;
        }

        // check if exists a similar age range
        for (AgeRange age : findAll()) {
            if ((age != entity) &&
                    (age.getIniAge() == entity.getIniAge()) &&
                    (age.getEndAge() == entity.getEndAge())) {
                errors.reject("admin.ageranges.msgerror2");
                return;
            }
        }

        adjustAgeRanges(entity);
    }

    /**
     * Adjust the age ranges according to the instance being created/edited
     */
    protected void adjustAgeRanges(AgeRange entity) {
        List<AgeRange> ages = findAll();
        int index = 0;
        AgeRange instance = entity;

        while (index < ages.size()) {
            AgeRange range = ages.get(index);
            if (range != instance) {
                if ((range.getIniAge() >= instance.getIniAge()) &&
                        (range.getEndAge() <= instance.getEndAge())) {
                    getEntityManager().remove(range);
                    ages.remove(range);
                    index--;
                } else if ((range.getIniAge() < instance.getIniAge()) &&
                        (range.getEndAge() > instance.getEndAge())) {
                    int end = range.getEndAge();
                    range.setEndAge(instance.getIniAge() - 1);
                    AgeRange aux = new AgeRange();
                    aux.setIniAge(instance.getEndAge() + 1);
                    aux.setEndAge(end);
                    aux.setWorkspace(entity.getWorkspace());
                    getEntityManager().persist(aux);
                    ages.add(aux);
                } else if ((range.getIniAge() >= instance.getIniAge()) && (range.getIniAge() <= instance.getEndAge())) {
                    range.setIniAge(instance.getEndAge() + 1);
                } else if ((range.getEndAge() <= instance.getEndAge()) && (range.getEndAge() >= instance.getIniAge())) {
                    range.setEndAge(instance.getIniAge() - 1);
                }
            }
            index++;
        }
    }

    private List<AgeRange> findAll() {
        return getEntityManager().createQuery("from AgeRange order by iniAge").getResultList();
    }
}
