package org.msh.etbm.test.services;

import java.util.UUID;

/**
 * Result value of the createAndFindOne test method
 *
 * Created by rmemoria on 10/3/16.
 */
public class TestResult {

    private UUID id;
    private Object data;

    public TestResult(UUID id, Object data) {
        this.id = id;
        this.data = data;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
