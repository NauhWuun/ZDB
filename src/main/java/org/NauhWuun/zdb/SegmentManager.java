package org.NauhWuun.zdb;

import java.io.Serializable;

public interface SegmentManager<K extends Serializable, V extends Serializable> {
    Segment<K, V> fetch(long index);

    boolean persist(Segment<K, V> segment);

    void remove(long index);
}
