package org.NauhWuun.zdb;

import org.NauhWuun.zdb.Cache.ARC.ARCache;

import java.io.Serializable;

public class CachingManager<K extends Serializable, V extends Serializable> implements SegmentManager<K, V> {
    ARCache<Long, Segment<K, V>> cache;
    SegmentManager<K, V> realManager;

    public CachingManager(int capacity, SegmentManager<K, V> realManager) {
        cache = new ARCache<>(capacity);
        this.realManager = realManager;
    }

    @Override
    public Segment<K, V> fetch(long index) {
        Segment<K, V> segment = cache.get(index);
        if (segment != null)
            return segment;

        return realManager.fetch(index);
    }

    @Override
    public boolean persist(Segment<K, V> segment) {
        cache.put(segment.id, segment);
        return realManager.persist(segment);
    }

    @Override
    public void remove(long index) {
        realManager.remove(index);
    }
}