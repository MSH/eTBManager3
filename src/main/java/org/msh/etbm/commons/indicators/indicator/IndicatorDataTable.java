package org.msh.etbm.commons.indicators.indicator;

import org.msh.etbm.commons.indicators.datatable.GroupedDataTable;
import org.msh.etbm.commons.indicators.variables.Variable;

import java.util.List;
import java.util.Map;


/**
 * A grouped data table extended to store indicator information.
 *
 * Created by rmemoria on 3/10/16.
 */
public interface IndicatorDataTable extends GroupedDataTable {

    /**
     * The list of columns descriptors, assigning the title to the ID element in the key level
     * @return
     */
    List<Map<String, String>> getColumnKeyDescriptors();

    /**
     * The list of row descriptors, assigning the title to the ID element in the key level
     * @return
     */
    List<Map<String, String>> getRowKeyDescriptors();

    List<Variable> getColumnVariables();

    List<Variable> getRowVariables();
}
