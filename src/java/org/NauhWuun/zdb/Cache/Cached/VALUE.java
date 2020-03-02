package org.NauhWuun.zdb.Cache.Cached;

import java.util.ArrayList;
import java.util.List;

public class VALUE
{
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
