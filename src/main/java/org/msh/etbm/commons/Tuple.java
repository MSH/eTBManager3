package org.msh.etbm.commons;

/**
 * Store two values (a tuple). Useful when you want to return two values from
 * a method call
 *
 * Created by rmemoria on 11/12/16.
 */
public class Tuple<K, E> {

    private K value1;
    private E value2;

    /**
     * Create a new tuple from two given values
     * @param val1 the first value of the tuple
     * @param val2 the second value of the tuple
     * @param <K>
     * @param <E>
     * @return
     */
    public static <K, E> Tuple of(K val1, E val2) {
        return new Tuple<K, E>(val1, val2);
    }

    /**
     * Create a new tuple
     * @param val1
     * @param val2
     */
    public Tuple(K val1, E val2) {
        value1 = val1;
        value2 = val2;
    }

    public K getValue1() {
        return value1;
    }

    public void setValue1(K value1) {
        this.value1 = value1;
    }

    public E getValue2() {
        return value2;
    }

    public void setValue2(E value2) {
        this.value2 = value2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tuple<?, ?> tuple = (Tuple<?, ?>) o;

        if (!value1.equals(tuple.value1)) {
            return false;
        }
        return value2.equals(tuple.value2);
    }

    @Override
    public int hashCode() {
        int result = value1.hashCode();
        result = 31 * result + value2.hashCode();
        return result;
    }
}
