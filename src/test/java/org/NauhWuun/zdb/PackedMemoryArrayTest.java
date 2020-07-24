package org.NauhWuun.zdb;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class PackedMemoryArrayTest
{
	@Test
	public void test_shrinkAndExpand() throws IOException {
		PackedMemoryArray<Integer, Integer> arr = new PackedMemoryArray<>(100);

		arr.add(1, 1, 10);
		arr.add(2, 22, 30);

		System.out.println(arr.get(1).getKey() + "," + arr.get(1).getValue());
		System.out.println(arr.get(2).getKey() + "," + arr.get(2).getValue());
	}
}
