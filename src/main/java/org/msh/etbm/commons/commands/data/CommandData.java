package org.msh.etbm.commons.commands.data;

/**
 * Created by rmemoria on 17/10/15.
 */
public interface CommandData {
    DataType getType();

    Object getDataToSerialize();
}
