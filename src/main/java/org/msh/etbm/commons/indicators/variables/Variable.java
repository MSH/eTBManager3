package org.msh.etbm.commons.indicators.variables;

import org.msh.etbm.commons.IsItem;
import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.indicators.keys.Key;
import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.springframework.context.ApplicationContext;

/**
 * Interface that must be implemented by classes that want to expose itself as a
 * report variable. A report variable is displayed in the report resulting table.
 *
 * @author Ricardo Memoria
 *
 */
public interface Variable extends IsItem<String> {

    /**
     * Prepare the query to return the data necessary to be handled by the variable.
     * This method basically includes the fields to be returned, table joins and its restrictions (where clause)
     * @param def interface of {@link QueryDefs} to inject fields to select, joins and restrictions
     * @param iteration the iteration number
     */
    void prepareVariableQuery(QueryDefs def, int iteration);

    /**
     * Transform the values returned from the database in a key to be used in the indicator table.
     * This method is called for each record returned from the query.
     * The values passed as argument are exactly the values selected previously in the method
     * {@link Variable#prepareVariableQuery(QueryDefs, int)}. This values must be transformed to an
     * instance of the {@link Key} class, representing the key in the indicator. Several keys may
     * be returned (using {@link Key#asMultiple(Object...)}) to indicate that the value returned by
     * the query (the count) must be used in the returned keys.</br>
     * @param values is an array of elements returned from the query used in prepareVariableQuery
     * @param iteration is the iteration number
     * @return
     */
    Key createKey(Object[] values, int iteration);


    /**
     * From the key created by the method <code>createKey()</code>, return the text to be displayed in the table cell
     * @param key is the object key created by the method <code>createKey()</code>
     * @return the text to be displayed for the key
     */
    String getKeyDisplay(Key key);

    /**
     * Convert a group object key to a text to be displayed to the user
     * @param key is the group key created before by <code>createGroupKey()</code> method
     * @return a text ready for displaying representing the key
     */
    String getGroupKeyDisplay(Key key);

    /**
     * Compare two values of the variable. It follows the implementation of the {@link Comparable} interface. If more than 1 field is specified
     * to be returned from the data base, the parameters objects are actually an array of objects, so a proper cast must be done to the right value.
     * <p>
     * If just one field is specified, so val1 and val2 will point to this value
     * @param key1
     * @param key2
     * @return
     */
    int compareValues(Key key1, Key key2);

    /**
     * Return the possible list of values of the variable. This list is used to initialize the data table
     * @return list of object keys that will be used to create a fix list of values in the indicator
     */
    Object[] getDomain();


    /**
     * If the variable must display information in a two-level structure, so this method must return true
     * @return true if variable is grouped
     */
    boolean isGrouped();

    /**
     * Return the number of iterations that this variable will be called, i.e,
     * the number of times the report will query the database using different
     * conditions based on its iteration.
     *
     * @return the number of iterations this variable will be executed. If it supports
     * just one iteration, return a value equals or lower than 1
     */
    int getIterationCount();


    /**
     * Return true if the total of the values can be achieved by calculating
     * the sum of the values. If false, the total will not be calculated
     * @return true if total can be calculated by summing the values, otherwise
     * returns false if the total doesn't exist
     */
    boolean isTotalEnabled();


    /**
     * Return the unit of measure returned by the variable. Example, patients, cases, exams, etc.
     * Object can be anything that represents this unit. It's basically used to compare
     * if a variable is compatible with another variable
     * @return unit type
     */
    VariableOutput getVariableOutput();

    /**
     * Initialize the variable passing the instance of the ApplicationContext.
     * This is called just once when variable is created and before any other method,
     * in order to give the filter the possibility to get beans
     * @param context instance of ApplicationContext interface
     */
    void initialize(ApplicationContext context);

}
