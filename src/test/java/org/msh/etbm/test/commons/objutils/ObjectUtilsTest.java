package org.msh.etbm.test.commons.objutils;

import org.junit.Test;
import org.msh.etbm.commons.objutils.*;
import org.msh.etbm.test.commons.objutils.fixtures.ClassB;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Test ObjectUtils class
 * Created by rmemoria on 28/1/16.
 */
public class ObjectUtilsTest {

    private static final String NAME = "Richard Memory";
    private static final int AGE = 33;

    /**
     * Test reading and writing to property
     */
    @Test
    public void readAndWrite() {
        Model model = new Model();

        ObjectUtils.setProperty(model, "name", NAME);
        ObjectUtils.setProperty(model, "age", AGE);

        assertEquals(model.getName(), NAME);
        assertEquals(model.getAge(), AGE);

        assertEquals(ObjectUtils.getProperty(model, "name"), NAME);
        assertEquals(ObjectUtils.getProperty(model, "age"), AGE);
    }

    /**
     * Test setting the wrong type to the property
     */
    @Test(expected = ObjectAccessException.class)
    public void invalidWriteType() {
        Model model = new Model();

        ObjectUtils.setProperty(model, "age", NAME);
    }

    /**
     * Testing creating a new instance of a class
     */
    @Test
    public void newInstance() {
        Model model = ObjectUtils.newInstance(Model.class);

        assert (model != null);
    }


    @Test
    public void hashTest() {
        String[] fruits = {"Banana", "Apple", "Strawberry", "Orange"};
        Model model = new Model("name1", 1L, 33, true, new Date(), Arrays.asList(fruits));
        int val = ObjectUtils.hash(model);
        assertTrue(val != 0);
        System.out.println(val);

        model.setAge(34);
        int val2 = ObjectUtils.hash(model);
        assertNotEquals(val, val2);
        System.out.println(ObjectUtils.hash(model));

        String[] fruits2 = {"Banana", "Apple", "Orange"};
        model.setFruits(Arrays.asList(fruits2));
        int val3 = ObjectUtils.hash(model);
        assertNotEquals(val, val3);
        assertNotEquals(val2, val3);
    }

    /**
     * Test the {@link PropertyValue} implementation
     */
    @Test
    public void propertyValueTest() {
        // test a collection
        List<Model> models = new ArrayList<>();
        String[] fruits = {"Banana", "Apple", "Strawberry", "Orange"};
        models.add(new Model("name1", 1L, 33, true, new Date(), Arrays.asList(fruits)));

        String[] fruits2 = {"Banana", "Apple", "Strawberry", "Orange"};
        models.add(new Model("name2", 2L, 33, false, new Date(), Arrays.asList(fruits2)));

        PropertyValue val = new PropertyValue(models);

        assertTrue(val.isCollection());
        assertFalse(val.isNull());
        assertNotNull(val.getItems());

        for (CollectionItem item : val.getItems()) {
            assertNotNull(item.getValue());
            assert (item.getHash() != 0);
        }

        // test a single value
        PropertyValue pv = new PropertyValue(100);
        assertNull(pv.getItems());
        assertFalse(pv.isCollection());
        assertFalse(pv.isNull());
    }

    /**
     * Test the implementation of  {@link ObjectValues}
     */
    @Test
    public void objectValuesTest() {
        String[] fruits = {"Banana", "Apple", "Strawberry", "Orange"};
        Model model = new Model("name1", 1L, 33, true, new Date(), Arrays.asList(fruits));
        ObjectValues ov = new ObjectValues(model);

        assertNotNull(ov.get("name"));
        assertNotNull(ov.get("id"));
        assertNotNull(ov.get("age"));
        assertNotNull(ov.get("married"));
        assertNotNull(ov.get("lastUpdate"));
        assertNotNull(ov.get("fruits"));
    }


    @Test
    public void patterMatchTest() {
        String name = "$user ($action)";
        Pattern PARAM_PATTERN = Pattern.compile("\\$([._a-zA-Z]+)");
        Matcher matcher = PARAM_PATTERN.matcher(name);

        // parse the restriction
        List<String> params = new ArrayList<>();
        while (matcher.find()) {
            String p = matcher.group().substring(1);
            System.out.println(p);
            String val = "user".equals(p) ? "Ricardo Lima" : "Editing data";
            name = name.replace("$" + p, val);
        }
        System.out.println(name);
    }


    @Test
    public void testFieldGeneric() {
        Field fieldActive = ObjectUtils.findField(ClassB.class, "active");
        assertNotNull(fieldActive);
        assertEquals(Optional.class, fieldActive.getType());

        Field fieldName = ObjectUtils.findField(ClassB.class, "name");
        assertNotNull(fieldName);
        assertEquals(Optional.class, fieldName.getType());

        Field f = ObjectUtils.findField(ClassB.class, "notExist");
        assertNull(f);

        Class activeType = ObjectUtils.getPropertyGenericType(ClassB.class, "active", 0);
        assertNotNull(activeType);
        assertEquals(Boolean.class, activeType);

        Class nameType = ObjectUtils.getPropertyGenericType(ClassB.class, "name", 0);
        assertNotNull(nameType);
        assertEquals(String.class, nameType);
    }


    @Test
    public void testHashSHA1() {
        // hash a string
        String res = ObjectUtils.hashSHA1("Test");
        assertNotNull(res);
        assertEquals(40, res.length());
//        System.out.println("SHA1 = " + res + " len = " + res.length());

        // hash a complex object
        Model m = new Model("Test",
                1000L,
                10,
                true,
                new Date(),
                Arrays.asList("Banana", "Apple"));
        res = ObjectUtils.hashSHA1(m);
        assertNotNull(res);
        assertEquals(40, res.length());

        // create a second object as the same
        Model m2 = new Model(m.getName(),
                m.getId(),
                m.getAge(),
                m.isMarried(),
                m.getLastUpdate(),
                Arrays.asList("Banana", "Apple"));
        String res2 = ObjectUtils.hashSHA1(m2);
        assertNotNull(res2);
        assertEquals(res, res2);

        // change the second object to check if hash will be different
        m2.setAge(11);
        res2 = ObjectUtils.hashSHA1(m2);
        assertNotNull(res2);
        assertNotEquals(res, res2);

        // hash an array of objects
        Object[] args = { m, m2 };
        res2 = ObjectUtils.hashSHA1(args);
        assertNotNull(res2);
    }
}
