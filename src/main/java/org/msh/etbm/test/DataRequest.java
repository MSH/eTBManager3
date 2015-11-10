package org.msh.etbm.test;

import org.msh.etbm.services.admin.AddressRequest;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;

/**
 * Created by rmemoria on 10/11/15.
 */
public class DataRequest {
    @NotNull
    private Optional<String> name;
    @NotNull
    private Optional<Integer> age;
    private Optional<AddressRequest> address;
    private Optional<Date> birthDate;

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<Integer> getAge() {
        return age;
    }

    public void setAge(Optional<Integer> age) {
        this.age = age;
    }

    public Optional<AddressRequest> getAddress() {
        return address;
    }

    public void setAddress(Optional<AddressRequest> address) {
        this.address = address;
    }

    public Optional<Date> getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Optional<Date> birthDate) {
        this.birthDate = birthDate;
    }
}
