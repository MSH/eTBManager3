package org.msh.etbm.commons.transactionlog;

import org.msh.etbm.commons.transactionlog.mapping.PropertyMapping;
import org.msh.etbm.db.entities.TransactionLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Contain information about the transaction to be saved in the transaction log report
 *
 * Created by rmemoria on 8/4/15.
 */
public class ActionTX {
    // the event name
    private String eventName;

    // the description (single line) about the event
    private String description;

    // the type of action about the event
    private RoleAction roleAction;

    // the ID of the entity assigned to the transaction log (not required)
    private Integer entityId;

    // the entity class (not required)
    private String entityClass;

    // the entity assigned to the action (not required)
    // this entity will receive the transaction log if it implements the Transactional interface
    private Object entity;

    // the title suffix to be included
    private String titleSuffix;

    // list of values to be written to the detail of the transaction
    private List<PropertyValue> values;

    // allows to include other details to the transaction log
    private DetailXMLWriter detailWriter = new DetailXMLWriter();

    // the saved transaction (it's empty while the tx is not saved)
    private TransactionLog transactionLog;

    /**
     * Map the properties of the entity and include them in the values property
     * @param entity the entity object
     * @param oper the operation to be applied to the entity object, in order to select the properties
     */
    public void mapValues(Object entity, Operation oper) {
/*
        EntityLogMapping mapping = EntityLogManager.instance().get(entity);
        if (oper == Operation.ALL)
            throw new IllegalArgumentException("operation must be specified. ALL is not allowed");

        Map<PropertyMapping, String> lst = mapping.getPropertyList();
        List<PropertyValue> values = new ArrayList<PropertyValue>();

        for (PropertyMapping prop: lst.keySet()) {
            try {
                String propname = lst.get(prop);
                if ((oper == Operation.EDIT) || (prop.isLoggedForOperation(oper))) {
                    Object value = PropertyUtils.getProperty(entity, propname);
                    addValue( entity, prop, oper, propname, value );
                }

            } catch (Exception e) {
                if (!e.getClass().getName().contains("NestedNullException")){ //AK 07/07/12 exclude annoyed error message about treatmentPeriod
                    e.printStackTrace();
                    new RuntimeException(e);
                }
            }
        }
*/
    }

    /**
     * Prepare the transaction action to be recorded. If the return value is
     * false, it means there is nothing to be saved. It will return false if it's
     * an entity being edited but no change was performed
     *
     * @return boolean value
     */
    protected boolean prepareForSaving() {
        if ((roleAction == RoleAction.EDIT) && (values == null)) {
            return false;
        }

        if (values == null) {
            return true;
        }

        boolean result = false;

        int index = 0;
        while (index < values.size()) {
            PropertyValue val = values.get(index);

            if (val.getOperation() == Operation.EDIT) {
                boolean changed = val.isValueChanged();

                if (changed || val.getMapping().isLoggedForOperation(Operation.EDIT))
                    index++;
                else values.remove(index);

                if (changed)
                    result = true;
            }
            else {
                if (val.getValue() == null)
                    values.remove(index);
                else {
                    index++;
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * Notify the action was saved, and invalidate it for another usage
     * @param tx the saved transaction
     */
    public void notifySaved(TransactionLog tx) {
        values = null;
        detailWriter.clear();
        transactionLog = tx;
    }

    /**
     * Return the detail writer responsible for including detail information in the transaction log
     * @return
     */
    public DetailXMLWriter getDetailWriter() {
        return detailWriter;
    }


    /**
     * Generate the string containing the details to be recorded in the transaction log.
     * Once called, the content of the detail write should not be called again
     * @return string value
     */
    protected String generateDetails() {
        if (values == null) {
            return null;
        }

        for (PropertyValue val: values) {
            if (val.getOperation() != Operation.EDIT)
                detailWriter.addTableRow(val.getMapping().getMessageKey(), val.getValue());
            else detailWriter.addTableRow(val.getMapping().getMessageKey(), val.getValue(), val.getEntityNewValue());
        }
        return getDetailWriter().asXML();
    }


    /**
     * Add a new property value to the list of values
     * @param entity
     * @param prop
     * @param propname
     * @param value
     * @return
     */
    protected PropertyValue addValue(Object entity, PropertyMapping prop, Operation oper, String propname, Object value) {
        if (values == null) {
            values = new ArrayList<PropertyValue>();
        }
        PropertyValue propval = new PropertyValue(entity, prop, oper, propname, value);
        values.add( propval );
        return propval;
    }

    public List<PropertyValue> getValues() {
        return values;
    }

    public String getEventName() {
        return eventName;
    }

    /**
     * Add a row to the table in the details of the action. Just a helper method
     * @param key
     * @param vs
     * @return
     */
    public ActionTX addRow(String key, Object... vs) {
        getDetailWriter().addTableRow(key, vs);
        return this;
    }


    public ActionTX setEventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ActionTX setDescription(String description) {
        this.description = description;
        return this;
    }

    public RoleAction getRoleAction() {
        return roleAction;
    }

    public ActionTX setRoleAction(RoleAction roleAction) {
        this.roleAction = roleAction;
        return this;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public ActionTX setEntityId(Integer entityId) {
        this.entityId = entityId;
        return this;
    }

    public String getEntityClass() {
        return entityClass;
    }

    public ActionTX setEntityClass(String entityClass) {
        this.entityClass = entityClass;
        return this;
    }

    public Object getEntity() {
        return entity;
    }

    public ActionTX setEntity(Object entity) {
        this.entity = entity;
        return this;
    }

    public TransactionLog getTransactionLog() {
        return transactionLog;
    }

    public void setTransactionLog(TransactionLog transactionLog) {
        this.transactionLog = transactionLog;
    }
    /**
     * Return true if the action was already recorded
     * @return
     */
    public boolean isSaved() {
        return transactionLog != null;
    }

    public String getTitleSuffix() {
        return titleSuffix;
    }

    public void setTitleSuffix(String titleSuffix) {
        this.titleSuffix = titleSuffix;
    }


    /**
     * Helper method to start a transaction action without declaring the log service
     * @return instance of ActionTX generated by the transaction service
     */
    public static ActionTX begin(String eventName) {
        return begin(eventName);
    }

    /**
     * Helper method to start a transaction action without declaring the log service
     * @param entity the entity assigned to the transaction
     * @param roleAction the role action of the transaction
     * @return instance of ActionTX
     */
/*
    public static ActionTX begin(String eventName, Object entity, RoleAction roleAction) {
        return ((TxLogServices)App.getComponent("txLogServices")).begin(eventName, entity, roleAction);
    }
*/

    /**
     * Save the transaction log action
     */
/*
    public TransactionLog end() {
        return ((TxLogServices) App.getComponent("txLogServices")).end(this);
    }
*/

    /**
     * Update the content of the action transaction
     */
/*
    public void update() {
        ((TxLogServices)App.getComponent("txLogServices")).update(this);
    }
*/

}
