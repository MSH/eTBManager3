package org.msh.etbm.services.admin.admunits.impl;

import java.util.UUID;

/**
 * Store temporary information about a new code of an administrative unit
 * during an operation of update and insert (when new code is issued).
 * <p/>
 * Basically store the number of references at the execution point for a
 * parent unit
 *
 * Created by rmemoria on 31/10/15.
 */
public class CodeRef {
    /**
     * The parent ID being processed
     */
    private UUID id;
    private int refCount;
    private String code;

    public CodeRef(UUID id) {
        this.id = id;
    }

    /**
     * Set the code to the given parent. This operation is supposed to happen just once
     * in the object life-cycle
     * @param code the code of the given parent ID
     */
    public void setCode(String code) {
        if (this.code != null) {
            throw new RuntimeException("Code was already set");
        }
        this.code = code;
    }

    /**
     * Add a reference to the parent being processed
     */
    public void addRef() {
        refCount++;
    }

    /**
     * Remove a reference from a parent being processed
     */
    public void removeRef() {
        if (refCount == 0) {
            throw new RuntimeException("Reference of admin unit code is already zero");
        }
        refCount--;
    }

    /**
     * Generate a new code for the given parent
     * @return
     */
    public String generateNewCode() {
        String val = code;
        if (val.length() > 3) {
            int len = val.length();
            val = val.substring(len-3, len);
        }
        val = CodeUtils.incCode(val);

        if (code.length() > 3) {
            code = code.substring(0, code.length() - 3) + val;
        }
        else {
            code = val;
        }

        return code;
    }

    public int getRefCount() {
        return refCount;
    }

    public String getCode() {
        return code;
    }
}
