package org.NauhWuun.zdb.Cache.Cached;

import java.io.Serializable;

public class VALUE implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 5691821217664005550L;
    private Object[] params;
    private int index = 0;

    public VALUE(Object params) {
        this.params[index++] = params;
    }

    public final Object getValue(final int index) {
        return this.params;
    }

    public final Object getValue(Object _param) {
        for (Object param : params) {
            if (param.hashCode() == _param.hashCode())
                return param;
        }

        return null;
    }

    public final Object[] getAllValues() {
        return this.params;
    }

    public String toString() {
        return String.valueOf(params);
    }
}
