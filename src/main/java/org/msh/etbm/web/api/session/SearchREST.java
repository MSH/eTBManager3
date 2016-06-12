package org.msh.etbm.web.api.session;

import org.msh.etbm.services.session.search.SearchRequest;
import org.msh.etbm.services.session.search.SearchResponse;
import org.msh.etbm.services.session.search.SearchService;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * REST API to provide searching of units, workspaces, administrative units and patients
 * Created by rmemoria on 11/6/16.
 */
@RestController
@RequestMapping("/api/session")
@Authenticated
public class SearchREST {

    @Autowired
    SearchService searchService;

    /**
     * Get information about the user session
     * @param req object containing request parameters to search for
     * @return the list of items found
     */
    @Authenticated
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public List<SearchResponse> searchByKey(@NotNull @Valid @RequestBody SearchRequest req) {
        return searchService.searchByKey(req);
    }

}
