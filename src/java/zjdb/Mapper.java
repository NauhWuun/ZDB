package java.zjdb;

public class Mapper 
{
	public static int MAXSEGMENTS = (2 << 9) + 1;
	public static int MINSEGMENTS = 1;

	private Segment[] segment;
	private int SegCounts;

	public Mapper(final int SegCounts) {
		if (SegCounts + 1 >= MAXSEGMENTS && SegCounts <= MINSEGMENTS)
			throw new IllegalArgumentException("Sorry, more Segments...");

		this.SegCounts = SegCounts;
	}

	public void partition(final int index) {
		/**
		 * Temporary storage offset --unnsed
		 */
		int offset = index / SegCounts;

		int id = (int) (index % SegCounts);
		segment[id] = new Segment(id, offset).Build();
	}

	public Segment get(final int index) {
		/**
		 * Non-Sub partition
		 */
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

	/**
	 * Override, if you knew me, please...
	 */
	protected void shuffle(long from, long to, long count) {}
	protected void rebalance(Segment[] segment) {}
	public void DynamicSegment(int newSegmentCount) {}
	public void Remove(int index) {}

	public boolean compare(Segment v1, Segment v2) {
		return v1.getId() == v2.getId();
	}

	public final int getSegmentCount() {
		return this.SegCounts;
	}
}