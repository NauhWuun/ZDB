package org.NauhWuun.zdb;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class PackedMemoryArrayTest
{
	@Test
	public void test_shrinkAndExpand() throws IOException {
		PackedMemoryArray<Integer> arr = new PackedMemoryArray<>(100);

		arr.add(1, 10);
		arr.add(2, 30);
	}
}
