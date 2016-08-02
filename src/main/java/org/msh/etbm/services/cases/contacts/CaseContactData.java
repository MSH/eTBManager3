package org.msh.etbm.services.cases.contacts;

import org.msh.etbm.db.enums.Gender;

import java.util.UUID;

/**
 * Created by Mauricio on 01/08/2016.
 */
public class CaseContactData {

    private UUID id;

    private String name;

    private Gender gender;

    private String age;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
