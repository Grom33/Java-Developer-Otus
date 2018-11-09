package ru.otus.gromov;

import org.junit.Assert;
import org.junit.Test;

public class ParallelSortTest {

	@Test
	public void sort() throws InterruptedException {
		int[] unsorted = {507, 769, 364, 401, 510, 894, 620, 583, 737, 70, 596, 508, 905};
		int[] expected = {70, 364, 401, 507, 508, 510, 583, 596, 620, 737, 769, 894, 905};
		ParallelSort parallelSort = new ParallelSort(unsorted, 4);
		parallelSort.sort();
		Assert.assertArrayEquals(expected, parallelSort.getSorted());
	}
}