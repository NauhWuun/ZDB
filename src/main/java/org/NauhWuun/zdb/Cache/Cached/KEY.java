package org.NauhWuun.zdb.Cache.Cached;

import org.NauhWuun.zdb.HashAlgorithm;
import org.NauhWuun.zdb.RockRand;

import java.io.Serializable;

public class KEY implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 760686850499088598L;
    private long key;
    private long createTime;

    public KEY() {
        this(RockRand.getUnsignedLong());
    }

    public KEY(final long key) {
        this.setCreatedTime();
        this.setKey(key);
    }

    private final KEY setKey(final long _key) {
        key = _key;
        return this;
    }

    public final long getKey() {
        return key;
    }

    public final <V> long hashValue(V value) {
        return HashAlgorithm.FNV1A_64_HASH(value.toString());
    }

    private void setCreatedTime() {
        createTime = System.currentTimeMillis();
    }

    public final long getCreatedTime() {
        return createTime;
    }

    public Boolean validator() {
        return key > 0 && createTime > 0;
    }
}
