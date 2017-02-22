package org.msh.etbm.test.commons.objutils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by rmemoria on 28/1/16.
 */
public class Model implements Serializable {

    public static final String PROP1 = "static property 1";
    protected static final String PROP2 = "static property 2";

    private String name;
    private Long id;
    private int age;
    private boolean married;
    private Date lastUpdate;
    private List<String> fruits;

    public Model() {
        super();
    }

    public Model(String name, Long id, int age, boolean married, Date lastUpdate, List<String> fruits) {
        super();
        this.name = name;
        this.id = id;
        this.age = age;
        this.married = married;
        this.lastUpdate = lastUpdate;
        this.fruits = fruits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Model model = (Model) o;

        return !(id != null ? !id.equals(model.id) : model.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public int getMonths() {
        return age * 12;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isMarried() {
        return married;
    }

    public void setMarried(boolean married) {
        this.married = married;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<String> getFruits() {
        return fruits;
    }

    public void setFruits(List<String> fruits) {
        this.fruits = fruits;
    }
}
