package org.msh.etbm.commons.filters;

import org.msh.etbm.commons.IsItem;

/**
 * A filter interface, but with and ID and displayable name
 *
 * Created by rmemoria on 20/12/16.
 */
public interface FilterItem extends Filter, IsItem<String> {
}
