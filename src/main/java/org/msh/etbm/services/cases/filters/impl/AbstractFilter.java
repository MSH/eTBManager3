package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.filters.Filter;
import org.msh.etbm.commons.filters.FilterTypes;
import org.msh.etbm.commons.indicators.variables.Variable;
import org.msh.etbm.commons.indicators.variables.VariableOptions;
import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.springframework.context.ApplicationContext;

import java.util.*;

/**
 * Simple filter implementation to act as an starting point implementation
 *
 * Created by rmemoria on 17/8/16.
 */
public abstract class AbstractFilter implements Filter, Variable {

    public static final String KEY_NULL = "null";

    private static final VariableOptions DEFAULT_VAR_OPTIONS =
            new VariableOptions(false, true, 0, new Item<>("cases", "Cases"));

    private String id;
    private String label;
    private ApplicationContext applicationContext;
    private Messages messages;
    private VariableOptions variableOptions;

    public AbstractFilter(String id, String label) {
        super();
        this.id = id;
        this.label = label;
        setVariableOptions(DEFAULT_VAR_OPTIONS);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getLabel() {
        return getMessages().eval(label);
    }

    @Override
    public Map<String, Object> getResources(Map<String, Object> params) {
        List<Item> options = getOptions();

        if (options != null) {
            return Collections.singletonMap("options", options);
        }

        return null;
    }

    @Override
    public void initialize(ApplicationContext context) {
        this.applicationContext = context;
    }

    /**
     * Return the instance of {@link Messages} for translation of messages to the correct locale
     * @return instance of {@link Messages}
     */
    protected Messages getMessages() {
        if (messages == null) {
            messages = applicationContext.getBean(Messages.class);
        }
        return messages;
    }

    /**
     * Add restrictions to the query based on the given value. If value is a collection, so the restriction
     * is implemented as an IN SQL restriction
     * @param defs
     * @param field
     * @param value
     * @param converter called for each value to convert to an appropriated value parameter
     */
    protected void addValuesRestriction(QueryDefs defs, String field, Object value, ValueConverter converter) {
        StringBuilder s = new StringBuilder();

        Collection lst = value instanceof Collection ? (Collection)value : null;

        // check if there is one single value
        Object singleValue;
        if (lst == null) {
            singleValue = value;
        } else {
            if (lst.size() == 0) {
                return;
            }
            singleValue = lst.size() == 1 ? lst.toArray()[0] : null;
        }

        // handle collection
        if (lst != null && lst.size() > 1) {
            handleMultiValuesRestriction(defs, field, lst, converter);
        } else {
            handleSingleValueRestriction(defs, field, singleValue, converter);
        }
    }

    /**
     * Iterate over the given value. If value is an instance of a collection, it calls iterator for
     * each value. If value is a single value, iterate over itself
     * @param value
     * @param iterator
     */
    protected void iterateValues(Object value, ValueIterator iterator) {
        Collection lst = value instanceof Collection ? (Collection)value : null;

        if (lst != null) {
            for (Object val: lst) {
                iterator.iterate(val);
            }
        } else {
            iterator.iterate(value);
        }
    }


    /**
     * Add value restrictions when it is informed as an array of values
     * @param def
     * @param field
     * @param lst
     * @param converter
     */
    private void handleMultiValuesRestriction(QueryDefs def, String field, Collection lst, ValueConverter converter) {
        StringBuilder s = new StringBuilder();

        s.append(field).append(" in ");
        String delim = "(";
        List params = new ArrayList<>();

        for (Object item: lst) {
            Object param = converter != null ? converter.convert(item) : item;
            if (param != null) {
                s.append(delim).append("?");
                params.add(param);
                delim = ",";
            }
        }
        s.append(")");

        if (params.size() > 0) {
            def.restrict(s.toString(), params.toArray());
        }
    }

    /**
     * Add value restrictions when it is informed just a single value from the client request
     * @param def
     * @param field
     * @param value
     * @param converter
     */
    private void handleSingleValueRestriction(QueryDefs def, String field, Object value, ValueConverter converter) {
        Object param = converter != null ? converter.convert(value) : value;
        if (param != null) {
            String s = field + " = ?";
            def.restrict(s, param);
        }
    }

    /**
     * Return the instance of ApplicationContext, which provides an interface to return
     * managed beans by the spring framework
     *
     * @return instance of ApplicationContext
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    /**
     * Return the list of options for select or multiselect controls
     *
     * @return List of {@link Item} objects
     */
    public List<Item> getOptions() {
        return null;
    }

    @Override
    public String valueToDisplay(Object value) {
        String type = getFilterType();
        if (!FilterTypes.SELECT.equals(type) && !FilterTypes.MULTI_SELECT.equals(type)) {
            return getMessages().get("global.notdef");
        }

        List<Item> options = getOptions();

        String s = convertValuesToDisplay(value, options);

        return s;
    }

    protected String convertValuesToDisplay(Object value, Collection<Item> options) {
        if (value == null) {
            return "";
        }

        // check if value is a collection
        if (value instanceof Collection) {
            Collection lst = (Collection)value;

            StringBuilder s = new StringBuilder();
            String delim = "";
            for (Object val: lst) {
                s.append(delim)
                        .append(convertValuesToDisplay(val, options));
                delim = ", ";
            }

            return s.toString();
        }

        // search for the item in the list of options
        for (Item item: options) {
            if (item.getId().toString().equals(value.toString())) {
                return item.getName();
            }
        }

        return getMessages().get("NotValid");
    }


    @Override
    public int compareValues(Object val1, Object val2) {
        if (val1 == val2) {
            return 0;
        }

        if ((val1 == null) || (KEY_NULL.equals(val1))) {
            return 1;
        }

        if ((val2 == null) || (KEY_NULL.equals(val2))) {
            return -1;
        }

        if (val1 instanceof Comparable) {
            return ((Comparable)val1).compareTo(val2);
        }

        return 0;
    }

    protected void setVariableOptions(VariableOptions variableOptions) {
        this.variableOptions = variableOptions;
    }

    @Override
    public VariableOptions getVariableOptions() {
        return variableOptions;
    }

    @Override
    public int compareGroupValues(Object val1, Object val2) {
        return 0;
    }

    @Override
    public Object[] getDomain() {
        return new Object[0];
    }

    @Override
    public String createGroupKey(Object values) {
        return values != null ? values.toString() : null;
    }

    @Override
    public String getGroupKeyDisplay(String key) {
        return key;
    }

    @Override
    public void prepareVariableQuery(QueryDefs def, int iteration) {
    }

    @Override
    public String createKey(Object values) {
        if (values == null) {
            return getMessages().get(Messages.UNDEFINED);
        }
        return values.toString();
    }

    @Override
    public String getKeyDisplay(String key) {
        return key;
    }

}
