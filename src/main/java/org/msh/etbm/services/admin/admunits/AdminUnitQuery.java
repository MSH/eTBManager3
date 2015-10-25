package org.msh.etbm.services.admin.admunits;

import java.util.UUID;

/**
 * Created by rmemoria on 21/10/15.
 */
public class AdminUnitQuery {
    private Integer page;
    private String orderBy;
    private String name;
    private UUID parentId;


    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }
}
