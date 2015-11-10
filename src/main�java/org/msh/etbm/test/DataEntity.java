package org.msh.etbm.test;

import org.msh.etbm.db.Address;

import java.util.Date;

/**
 * Created by rmemoria on 10/11/15.
 */
public class DataEntity {
    private String name;
    private int age;
    private Address address;
    private Date birthDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
