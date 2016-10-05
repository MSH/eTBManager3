package org.msh.etbm.commons.indicators.indicator;

import org.msh.etbm.commons.indicators.datatable.GroupedDataTable;
import org.msh.etbm.commons.indicators.variables.Variable;

import java.util.List;


/**
 * Created by rmemoria on 3/10/16.
 */
public interface IndicatorDataTable extends GroupedDataTable {

    KeyDescriptorList getColumnKeyDescriptors();

    KeyDescriptorList getRowKeyDescriptors();

    List<Variable> getColumnVariables();

    List<Variable> getRowVariables();
}
