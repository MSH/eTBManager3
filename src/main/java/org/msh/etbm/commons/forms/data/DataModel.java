package org.msh.etbm.commons.forms.data;

import org.msh.etbm.commons.models.ModelManager;
import org.msh.etbm.commons.models.data.fields.Field;


/**
 * Information about the data model used by the {@link Form}
 *
 * Created by rmemoria on 30/7/16.
 */
public interface DataModel {

    /**
     * Return an instance of the {@link Field} represented by the given field reference.
     * The {@link Field} instance comes from the corresponding {@link org.msh.etbm.commons.models.data.Model}
     * referenced in the data model
     *
     * @param modelManager
     * @param fieldRef
     * @return
     */
    Field getFieldModel(ModelManager modelManager, String fieldRef);
}
