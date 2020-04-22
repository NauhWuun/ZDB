package org.NauhWuun.zdb;

import java.io.Serializable;

public class Segment<K extends Serializable, V extends Serializable> {
    public final long id;
    private Pair[] arr;
    private Integer count;

    public Segment(int id, int size) {
        this.id = id;
        this.arr = new Pair[size];
    }

    public void add(int index, K key, V value) {
        if (value == null)
            throw new IllegalArgumentException("add()-ing a null value. Did you mean to use set() instead?");
        Pair<K, V> pair = null;
        Pair old = arr[index];
        if (old == null) {
            pair = new Pair<>(key, value);
            arr[index] = pair;
            if (count != null)
                count++;
        }

        int freeIndex = getNextFreeIndexRight(index);
        if (freeIndex != -1) {
            for (int i = freeIndex; i > index; i--) {
                arr[i] = arr[i - 1];
            }
            pair = new Pair<>(key, value);
            arr[index] = pair;
        } else {
            freeIndex = getNextFreeIndexLeft(index);
            if (freeIndex == -1) {
                throw new IllegalArgumentException("Segment is full");
            }
            for (int i = freeIndex; i < index - 1; i++) {
                arr[i] = arr[i + 1];
            }
            pair = new Pair<>(key, value);
            arr[index - 1] = pair;
        }
        if (count != null)
            count++;
    }

    public void set(int index, K key, V value) {
        Pair old = arr[index];
        arr[index] = new Pair<>(key, value);
        if (old == null && value != null && count != null)
            count++;
        if (old != null && value == null && count != null)
            count--;
    }

    protected int getNextFreeIndexRight(int start) {
        start++;
        while (start < arr.length && arr[start] != null) {
            start++;
        }
        if (start == arr.length) {
            return -1;
        }
        return start;
    }

    protected int getNextFreeIndexLeft(int start) {
        start--;
        while (start >= 0 && arr[start] != null) {
            start--;
        }
        if (start < 0) {
            return -1;
        }
        return start;
    }

    public Pair get(int index) {
        return arr[index];
    }

    public int getCount() {
        if (count == null) {
            count = 0;
            for (Object elem : arr) {
                if (elem != null)
                    count++;
            }
        }
        return count;
    }

    public class Pair<K, V> {
        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
