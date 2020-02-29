package org.NauhWuun.zdb;

public class Mapper 
{
	/**
	 * Max Segments Count/512 + 1
	 */
	public static int MAXSEGMENTS = (2 << 8) + 1;
	public static int MINSEGMENTS = 1;

	private Segment[] segment;
	private int SegCounts;

	public Mapper(final int SegCounts) {
		if (SegCounts + 1 >= MAXSEGMENTS && SegCounts <= MINSEGMENTS)
			throw new IllegalArgumentException("Sorry, more Segments...");

		this.SegCounts = SegCounts;
	}

	public void partition(final int index) {
		int offset = index / SegCounts;
		int id = (int) (index % SegCounts);

		segment[id] = new Segment(id, offset).Build();
	}

	public Segment get(final int index) {
		int offset = index / SegCounts;
		int id = (int) (index % SegCounts);

		return segment[id];
	}

	public void LocalDisAllkSegments() {
		int index = 0;
		
		do {
			segment[(int) (index % SegCounts)].flushDisk();
		} while (index < SegCounts);
	}

	public void LocalDiskSegment(int index) {
		segment[index].flushDisk();
	}

	public boolean compare(Segment v1, Segment v2) {
		return v1.getId() == v2.getId();
	}

	public final int getSegmentCount() {
		return this.SegCounts;
	}
}