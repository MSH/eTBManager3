package org.msh.etbm.services.admin.ageranges;

import org.msh.etbm.Messages;
import org.msh.etbm.commons.ErrorMessages;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.AgeRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * Created by rmemoria on 6/1/16.
 */
@Service
public class AgeRangeServiceImpl extends EntityServiceImpl<AgeRange, EntityQueryParams>
    implements AgeRangeService {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    @Autowired
    Messages messages;

    /**
     * Return the list of age ranges
     * @return
     */
    @Override
    public QueryResult findMany(EntityQueryParams params) {
        QueryBuilder<AgeRange> builder = queryBuilderFactory.createQueryBuilder(AgeRange.class);

        builder.addDefaultOrderByMap("age", "iniAge");
        builder.setOrderByKey("age");

        return builder.createQueryResult(AgeRangeData.class);
    }

    @Override
    protected void prepareToSave(AgeRange entity, BindingResult bindingResult) {
        super.prepareToSave(entity, bindingResult);

        if (bindingResult.hasErrors()) {
            return;
        }

        if (entity.getIniAge() < 0) {
            bindingResult.rejectValue("iniDate", ErrorMessages.NOT_VALID);
        }

        if (entity.getEndAge() < 0) {
            bindingResult.rejectValue("endDate", ErrorMessages.NOT_VALID);
        }

        // check ranges
        if (entity.getIniAge() != 0 && entity.getEndAge() != 0 && entity.getIniAge() >= entity.getEndAge()) {
            bindingResult.reject("admin.ageranges.msgerror1");
            return;
        }

        // check if exists a similar age range
        for (AgeRange age: findAll()) {
            if ((age != entity) &&
                    (age.getIniAge() == entity.getIniAge()) &&
                    (age.getEndAge() == entity.getEndAge())) {
                bindingResult.reject("admin.ageranges.msgerror2");
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
        return getEntityManager().createQuery("from AgeRange").getResultList();
    }
}
