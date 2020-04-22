package org.NauhWuun.zdb.Cache.ARC;

import java.util.*;

public class LRUCache<K, V> {
    protected int size;
    protected Map<K, Node<K, V>> map = new HashMap<K, Node<K, V>>();
    protected Node<K, V> head = null;
    protected Node<K, V> end = null;

    public LRUCache(final int size) {
        this.size = size;
    }

    public void set(K key, V value) {
        Node<K, V> oldEntry = this.map.get(key);
        if (oldEntry != null) {
            oldEntry.value = value;
            return;
        } else if (isFull()) {
            this.evict();
        }

        Node<K, V> createdNode = new Node<K, V>(key, value);
        this.insertNode(createdNode);
        this.map.put(key, createdNode);
    }

    public V get(K key) {
        Node<K, V> node = this.map.get(key);
        if (node == null) {
            return null;
        }

        this.setHead(node);
        return node.value;
    }

    public V remove(K key) {
        Node<K, V> node = this.map.get(Objects.requireNonNull(key));
        if (node == null) {
            throw new IllegalArgumentException(key.toString());
        }

        this.removeNode(node);
        this.map.remove(key);
        return node.value;
    }

    protected void removeNode(Node<K, V> node) {
        if (node == null)
            return;

        if (head == node) {
            head = node.next;
            node.prev = null;
        } else if (node == end) {
            end = node.prev;
            end.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        this.map.remove(node.key);
    }

    public K evict() {
        if (this.end == null)
            return null;

        Node<K, V> last = this.end;
        this.removeNode(this.end);
        return last.key;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isFull() {
        return this.map.size() >= this.size;
    }

    protected void insertNode(Node<K, V> node) {
        if (this.head == null) {
            this.head = node;
            this.end = node;
        }

        this.head.prev = node;
        node.next = this.head;
        this.head = node;
    }

    protected void setHead(Node<K, V> node) {
        if (node == this.head)
            return;

        node.prev.next = node.next;
        if (node == this.end) {
            this.end = node.prev;
        } else {
            node.next.prev = node.prev;
        }

        this.head.prev = node;
        node.next = this.head;
        this.head = node;
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

    class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
