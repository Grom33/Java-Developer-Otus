package ru.otus.gromov;

import java.util.Arrays;

public class SortingThread extends Thread {
	private final int[] unsorted;
	private int[] sorted;

	public SortingThread(int[] unsorted) {
		this.unsorted = unsorted;
	}

	@Override
	public void run() {
		sorted = new int[unsorted.length];
		System.arraycopy(unsorted, 0, sorted, 0, unsorted.length);
		Arrays.sort(sorted);
	}

	public int[] getSorted() {
		return sorted;
	}
}
