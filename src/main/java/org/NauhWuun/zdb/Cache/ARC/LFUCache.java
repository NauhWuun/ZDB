package org.NauhWuun.zdb.Cache.ARC;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LFUCache<K, V> {
    private static int autoInc = 1;
    protected int size;
    protected Map<K, Node<K, V>> map = new ConcurrentHashMap<>();

    protected PriorityQueue<Node<K, V>> queue = new PriorityQueue<Node<K, V>>(
            Comparator.comparingInt((Node<K, V> a) -> a.frequency).thenComparingInt(a -> a.order));

    public LFUCache(final int size) {
        this.size = size;
    }

    public void set(K key, V value) {
        if (this.get(key) != null) {
            return;
        } else if (isFull()) {
            this.evict();
        }

        Node<K, V> createdNode = new Node<K, V>(key, value);
        this.map.put(key, createdNode);
        this.queue.add(createdNode);
    }

    public V get(K key) {
        Node<K, V> node = this.map.get(key);
        if (node == null) {
            return null;
        }

        node.frequency++;
        return node.value;
    }

    public K evict() {
        Node<K, V> evicted = this.queue.remove();
        this.map.remove(evicted.key);
        return evicted.key;
    }

    public void expand() {
        size++;
    }

    public void shrink() {
        if (this.map.size() == size) {
            this.evict();
        }

        if (size > 0) {
            size--;
        }
    }

    public int getSize() {
        return this.size;
    }

    public boolean isFull() {
        return this.map.size() >= this.size;
    }

    class Node<K, V> {
        K key;
        V value;
        int frequency;
        int order;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.frequency = 1;
            this.order = autoInc++;
        }
    }
}
