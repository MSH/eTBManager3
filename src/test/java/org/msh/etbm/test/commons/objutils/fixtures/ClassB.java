package org.msh.etbm.test.commons.objutils.fixtures;

import java.util.Optional;

/**
 * Created by rmemoria on 25/7/16.
 */
public class ClassB extends ClassA {

    private Optional<Boolean> active;

    public Optional<Boolean> getActive() {
        return active;
    }

    public void setActive(Optional<Boolean> active) {
        this.active = active;
    }
}
