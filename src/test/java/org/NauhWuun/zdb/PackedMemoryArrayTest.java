package org.NauhWuun.zdb;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class PackedMemoryArrayTest
{
	@Test
	public void test_shrinkAndExpand(){
		PackedMemoryArray<Integer> arr = new PackedMemoryArray<>("tmp", 9);

		arr.add(1, 10);
		arr.add(2, 30);
		arr.add(2, 20);
		arr.add(7, 60);
		arr.add(6, 40);
		arr.add(7, 50);
		arr.add(5, 35);
		arr.add(3, 35000);

		System.out.println(arr.get(1));
		System.out.println(arr.get(2));

		arr.add(1, 0);
		arr.set(9, 33);
		arr.set(13, 324234234);
	}

	@Test
	public void test_dontShrinkIfRemovingFromDifferentSegments(){
		PackedMemoryArray<Integer> arr = new PackedMemoryArray<>("tmp", 8);
		arr.add(1, 10);
		arr.add(2, 30);
		arr.add(2, 20);
		arr.add(7, 60);
		arr.add(6, 40);
		arr.add(7, 50);
		arr.add(5, 35);
		arr.add(1, 0);
		arr.set(9, null);
		assertArrayEquals(new String[]{
				"null,0,null,10,null,20,null,30",
				"null,null,null,40,null,50,null,60"
		}, arr.print());
		arr.set(3, null);
		assertArrayEquals(new String[]{
				"null,0,null,null,null,20,null,30",
				"null,null,null,40,null,50,null,60"
		}, arr.print());
		arr.add(11, 35);
		assertArrayEquals(new String[]{
				"null,0,null,null,null,20,null,30",
				"null,null,null,35,40,50,null,60"
		}, arr.print());
	}

	private void print(PackedMemoryArray<Integer> arr){
		for (String str: arr.print())
			System.out.println(str);
		System.out.println();
	}
}
