package org.msh.etbm.commons.objutils;

/**
 * Generate differences of an state object from an initial state to the current state
 *
 * Created by rmemoria on 15/5/16.
 */
public class ObjectDiffGenerator {

    private Object object;
    private ObjectValues iniValues;

    /**
     * Constructor passing the object with initial state to record
     * @param obj the object in its initial state
     */
    public ObjectDiffGenerator(Object obj) {
        this.object = obj;
        this.iniValues = DiffsUtils.generateValues(obj);
    }

    /**
     * Generate the changes in the object comparing its actual state with the initial state
     * @return instance of {@link Diffs}
     */
    public Diffs generate() {
        ObjectValues actualVals = DiffsUtils.generateValues(object);
        return DiffsUtils.generateDiffsFromValues(iniValues, actualVals);
    }
}
