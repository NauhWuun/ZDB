package org.NauhWuun.zdb.Cache.Cached;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VALUE implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -9151492570671786037L;
    private List<Object> params;

    public VALUE() {
        params = new ArrayList<>();
    }

    public void addValue(Object _param) {
        params.add(_param);
    }

    public void removeValue(Object _param) {
        params.remove(_param);
    }

    public final boolean getValue(Object _param) {
        return params.contains(_param);
    }

    public final List<Object> getAllValues() {
        return this.params;
    }

    public String toString() {
        return params.toString();
    }
}
