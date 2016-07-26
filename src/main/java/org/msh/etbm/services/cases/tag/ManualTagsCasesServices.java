package org.msh.etbm.services.cases.tag;

import org.msh.etbm.db.entities.Tag;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service to update cases tags links of manually assigned tags
 * Created by Mauricio on 25/07/2016.
 */
@Service
public class ManualTagsCasesServices {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRequestService userRequestService;

    @Transactional
    //@CommandLog(handler = CaseCloseLogHandler.class, type = CommandTypes.CASES_CASE_CLOSE)
    public void updateTags(CaseTagsFormData data) {
        List<Tag> manualTags = new ArrayList<Tag>();

        // Remove all tags from case that is not auto gen
        entityManager.createNativeQuery("delete from tags_case where case_id = :caseId " +
                "and tag_id in (select id from tag where sqlCondition is null)")
                .setParameter("caseId", data.getTbcaseId())
                .executeUpdate();

        // load existing tags
        if ((data.getTagIds() != null && !data.getTagIds().isEmpty())) {
            for (UUID tagId : data.getTagIds()) {
                manualTags.add(entityManager.find(Tag.class, tagId));
            }
        }

        // create new tags and add to list
        if (data.getNewTags() != null && !data.getNewTags().isEmpty()) {
            List<Tag> aksljd = getNewTags(data.getNewTags());
            manualTags.addAll(aksljd);
        }

        // set manual tags
        TbCase tbcase = entityManager.find(TbCase.class, data.getTbcaseId());
        tbcase.getTags().addAll(manualTags);
        entityManager.persist(tbcase);
        entityManager.flush();
    }

    private List<Tag> getNewTags(List<String> newTags) {
        Workspace currentWorkspace = entityManager.find(Workspace.class, userRequestService.getUserSession().getWorkspaceId());

        // check if already exists a tag with the same name
        List<Tag> ret = new ArrayList<Tag>();
        String allTagNames = "";

        for (String tagName : newTags) {
            allTagNames += "'" + tagName.toUpperCase() + "',";
        }

        allTagNames = allTagNames.substring(0, allTagNames.length() - 1);

        List<Tag> existingTags = entityManager.createQuery(" From Tag t where t.sqlCondition is null " +
                "and t.workspace.id = :wId and t.active = true and upper(t.name) in (" + allTagNames + ") ")
                .setParameter("wId", currentWorkspace.getId())
                .getResultList();

        //create tags that doesn't exists
        for (String newTagName : newTags) {
            boolean exists = false;
            for (Tag tag : existingTags) {
                if (getTagCode(tag.getName()).equals(getTagCode(newTagName))) {
                    ret.add(tag);
                    exists = true;
                    break;
                }
            }

            // create the tag and add it on the return
            if (!exists) {
                Tag newTag = new Tag();
                newTag.setActive(true);
                newTag.setName(newTagName);
                newTag.setWorkspace(currentWorkspace);
                entityManager.persist(newTag);
                ret.add(newTag);
            }
        }


        return ret;
    }

    private String getTagCode(String tagName) {
        if (tagName == null) {
            return null;
        }

        // remove `´~^
        tagName = Normalizer.normalize(tagName, Normalizer.Form.NFD);
        tagName = tagName.replaceAll("[^\\p{ASCII}]", "");

        return tagName.toUpperCase().trim().replace(" ", "");
    }
}
