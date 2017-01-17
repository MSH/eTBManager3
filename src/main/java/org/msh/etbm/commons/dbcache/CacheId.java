package org.msh.etbm.commons.dbcache;

import java.lang.reflect.Method;

/**
 * Used as a singleton object to check if there is just
 *
 * Created by rmemoria on 11/1/17.
 */
public class CacheId {

    /**
     * Method being used in the cache operation
     */
    private Method method;

    /**
     * Arguments used to invoke the method
     */
    private Object[] args;

    /**
     * The arguments in json format
     */
    private String argsJson;

    /**
     * The cache entry ID
     */
    private String entry;

    /**
     * The hash of the arguments, to be used as comparator
     */
    private String hash;

    /**
     * The reference count of the ID used when synchronizing its execution
     */
    private int refCount;



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CacheId syncTerm = (CacheId) o;

        if (!entry.equals(syncTerm.entry)) {
            return false;
        }
        return hash.equals(syncTerm.hash);
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getEntry() {
        return entry;
    }

    public String getHash() {
        return hash;
    }

    public int getRefCount() {
        return refCount;
    }

    public void addRefCount() {
        refCount++;
    }

    public void remRefCount() {
        if (refCount <= 0) {
            throw new DbCacheException("Cannot decrease reference count because it is already 0");
        }
        refCount--;
    }

    public String getArgsJson() {
        return argsJson;
    }

    public void setArgsJson(String argsJson) {
        this.argsJson = argsJson;
    }
}
