package org.msh.etbm.services.cases.tag;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.db.entities.Tag;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.services.cases.CaseLogHandler;
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
public class ManualCaseTagsService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRequestService userRequestService;

    @Transactional
    @CommandLog(handler = CaseLogHandler.class, type = CommandTypes.CASES_CASE_TAG)
    public ManualCaseTagsResponse updateTags(CaseTagsFormData data) {
        TbCase tbcase = entityManager.find(TbCase.class, data.getTbcaseId());

        // response to be returned
        ManualCaseTagsResponse res = new ManualCaseTagsResponse();

        // store previous list
        assignManualTags(tbcase.getTags(), res.getPrevManualTags());

        // temporary tag list that will be assigned to the case at the end of this method
        List<Tag> newTagList = new ArrayList<Tag>();

        // store the auto generated tags
        for (Tag t : tbcase.getTags()) {
            if (t.getSqlCondition() != null) {
                newTagList.add(t);
            }
        }

        // load existing tags
        if ((data.getTagIds() != null && !data.getTagIds().isEmpty())) {
            for (UUID tagId : data.getTagIds()) {
                newTagList.add(entityManager.find(Tag.class, tagId));
            }
        }

        // create new tags and add to list
        if (data.getNewTags() != null && !data.getNewTags().isEmpty()) {
            newTagList.addAll(getNewTags(data.getNewTags()));
        }

        // set new tag list
        tbcase.setTags(newTagList);
        entityManager.persist(tbcase);
        entityManager.flush();

        // finish preparing response
        assignManualTags(tbcase.getTags(), res.getNewManualTags());
        res.setTbcaseId(tbcase.getId());
        res.setTbcaseDisplayString(tbcase.getDisplayString());

        return res;
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

    private void assignManualTags(List<Tag> source, List<Item<UUID>> destination) {
        for (Tag t : source) {
            if (t.getSqlCondition() == null) {
                destination.add(new Item(t.getId(), t.getName()));
            }
        }
    }
}
