package org.NauhWuun.zdb;

import java.io.Serializable;

public class PackedMemoryArray<T extends Serializable>
{
	int segmentSize;
	int segmentCount;
	int height;
	final String dataPath = "./Data";
	SegmentManager<T> manager;

	public PackedMemoryArray(int segmentSize) {
		this.segmentSize = segmentSize;
		height = 1;
		manager = new CachingManager<>(Integer.MAX_VALUE, new LocalDiskManager<>(dataPath));
		manager.persist(new Segment<>(0, segmentSize));
	}

	public T get(long index) {
		long segment = index / segmentSize;
		int offset = (int) (index % segmentSize);

		try {
			return manager.fetch(segment).get(offset);
		} catch (Exception e) {
			throw new IllegalArgumentException("get()-ing a null value, please checking index and value");
		}
	}

	public void add(long index, T value) {
		long id = index / segmentSize;
		int offset = (int) (index % segmentSize);
		Segment<T> segment = manager.fetch(id);
		if (segment == null)
			throw new IllegalArgumentException("not find value, maybe Segment is full");

		segment.add(offset, value);
		if (mustRebalance(segment)) {
			rebalance(segment);
		} else {
			manager.persist(segment);
		}
	}

	public void set(long index, T value) {
		_set(index, value);
		Segment<T> segment = manager.fetch(index / segmentSize);
		if (mustRebalance(segment))
			rebalance(segment);
	}

	private void _set(long index, T value) {
		long id = index / segmentSize;
		int offset = (int) (index % segmentSize);
		manager.fetch(id).set(offset, value);
	}

	private boolean mustRebalance(Segment<T> segment) {
		double density = (double) segment.getCount() / segmentSize;
		double[] target = getTargetDensity(height - 1);
		return density <= target[0] && height > 1 || density >= target[1];
	}

	private void rebalance(Segment<T> segment) {
		int depth = height - 2;
		int divisor = 2;
		long rangeFrom = segment.id;
		long rangeTo = segment.id;
		long count = segment.getCount();
		double density = (double) count / segmentSize;
		double[] target = getTargetDensity(height - 1);

		while (depth >= 0) {
			long start = rangeFrom / divisor * divisor;
			if (start == rangeFrom) {
				for (long i = rangeTo + 1; i < rangeTo + divisor; i++)
					count += manager.fetch(i).getCount();
			} else {
				for (long i = start; i < rangeFrom; i++)
					count += manager.fetch(i).getCount();
			}
			rangeFrom = start;
			rangeTo = start + divisor - 1;
			density = (double) count / (divisor * segmentSize);
			target = getTargetDensity(depth);
			if (density > target[0] && density < target[1])
				break;
			depth--;
			divisor *= 2;
		}

		if (depth < 0) {
			if (density >= target[1]) {
				for (int i = segmentCount; i < segmentCount * 2; i++)
					manager.persist(new Segment<>(i, segmentSize));
				expand();
			} else {
				shrink();
				long middle = rangeFrom + (rangeTo - rangeFrom);
				for (long i = rangeTo; i > middle; i--)
					manager.remove(i);
			}
		} else {
			shuffle(rangeFrom * segmentSize, rangeTo * segmentSize);
		}
	}

	private void expand() {
		long count = squash(0, segmentCount * segmentSize);
		height++;
		segmentCount *= 2;
		shuffle(0, segmentCount * segmentSize, count);
	}

	private void shrink() {
		long count = squash(0, segmentCount * segmentSize);
		height--;
		segmentCount /= 2;
		shuffle(0, segmentCount * segmentSize, count);
	}

	private void shuffle(long from, long to) {
		shuffle(from, to, squash(from, to));
	}

	private void shuffle(long from, long to, long count) {
		long j = count + from - 1;
		long i = to - 1;
		long remaining = i - j;
		long gap = remaining / (j - from + 1);
		for (; j >= from; j--) {
			_set(i, get(j));
			_set(j, null);
			i -= gap + 1;
			if ((i - from + 1) % (gap + 1) != 0)
				i--;
		}
	}

	private long squash(long from, long to) {
		long i = from;
		long j = i;
		for (; i < to; i++) {
			if (get(i) != null) {
				if (i != j) {
					_set(j, get(i));
					_set(i, null);
				}
				j++;
			}
		}
		return j;
	}

	protected double[] getTargetDensity(int depth) {
		if (height == 1)
			return new double[]{0.25, 1};
		return new double[]{
				0.5 - 0.25 * depth / (height - 1),
				0.75 + 0.25 * depth / (height - 1)
		};
	}

	protected String[] print() {
		String[] res = new String[segmentCount];
		for (int i = 0; i < segmentCount; i++) {
			res[i] = manager.fetch(i).print();
		}
		return res;
	}
}