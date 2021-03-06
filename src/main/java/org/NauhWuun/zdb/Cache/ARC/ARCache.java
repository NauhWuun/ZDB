package org.NauhWuun.zdb.Cache.ARC;

public class ARCache<K, V> {
    protected LRUCache<K, V> lru;
    protected LFUCache<K, V> lfu;
    protected int size;

    public ARCache(final int size) {
        this.size = size;
        this.lru = new LRUCache<K, V>(size / 2);
        this.lfu = new LFUCache<K, V>(size - size / 2);
    }

    public void put(K key, V value) {
        if (this.lfu.get(key) != null) {
            this.lfu.set(key, value);
        }

        this.lru.set(key, value);
    }

    public V get(K key) {
        if (key == null)
            throw new IllegalArgumentException("the key not NULL, please re-check keys");
        V value = null;

        try {
            value = this.lru.remove(key);
            this.lfu.set(key, value);
            return value;
        } catch (Exception e) {
            value = this.lfu.get(key);
            if (value != null) {
                return value;
            }

            if (this.lru.get(key) != null) {
                int lruSize = this.lru.getSize();
                if (lruSize < this.size) {
                    this.lru.expand();
                    this.lfu.shrink();
                }
            }

            if (this.lfu.get(key) != null) {
                int lfuSize = lfu.getSize();
                if (lfuSize < this.size) {
                    this.lfu.expand();
                    this.lru.shrink();
                }
            }
        }

        return null;
    }

    public final int getSize() {
        return size;
    }
}
