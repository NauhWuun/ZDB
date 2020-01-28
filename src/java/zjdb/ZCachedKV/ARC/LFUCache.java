package java.zjdb.ZCachedKV.ARC;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LFUCache<K, V>
{
	private static int autoInc = 1;
	protected int size;
	protected Map<K, Node<K, V>> map = new ConcurrentHashMap<>();

	protected PriorityQueue<Node<K, V>> queue = new PriorityQueue<Node<K, V>>(/* 100, if you has limit requirement */(a, b) -> {
		int cmp = a.frequency - b.frequency;
		if (cmp == 0) {
			cmp = a.order - b.order;
		}
		return cmp;
	});

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
		/**
		 * set pool size infinity, can fix it, if you need 
		 */
	//	return this.map.size() >= this.size;

		return false;
	}

	class Node<K, V> 
	{
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
