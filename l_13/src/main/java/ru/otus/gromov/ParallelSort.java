package ru.otus.gromov;

import java.util.ArrayList;
import java.util.List;

public class ParallelSort {
	private final int[] unsorted;
	private int[] sorted;
	private final int maxThread;
	private final List<SortingThread> threadPool = new ArrayList<>();

	public ParallelSort(int[] unsorted, int threadCount) {
		this.unsorted = unsorted;
		this.maxThread = threadCount;
	}

	public void sort() throws InterruptedException {
		int offset = unsorted.length / maxThread;
		for (int i = 0; i < maxThread; i++) {
			int startPosition = i * offset;
			int endPosition, lengthResultArray;
			endPosition = startPosition + offset;
			lengthResultArray = endPosition - startPosition;
			if (i == (maxThread - 1)) lengthResultArray += (unsorted.length - (endPosition));
			int[] partOfUnsortedArray = new int[lengthResultArray];
			System.arraycopy(unsorted, startPosition, partOfUnsortedArray, 0, lengthResultArray);
			SortingThread sortingThread = new SortingThread(partOfUnsortedArray);
			sortingThread.start();
			threadPool.add(sortingThread);
		}
		for (SortingThread t : threadPool) {
			t.join();
		}
		threadPool.forEach((thread) -> sorted = merge(thread.getSorted(), sorted));
	}

	public int[] getSorted() {
		return sorted;
	}

	public int[] merge(int[] leftPart, int[] rightPart) {
		if (leftPart == null) return rightPart;
		if (rightPart == null) return leftPart;
		int cursorLeft = 0, cursorRight = 0, counter = 0;
		int[] merged = new int[leftPart.length + rightPart.length];
		while (cursorLeft < leftPart.length && cursorRight < rightPart.length) {
			if (leftPart[cursorLeft] <= rightPart[cursorRight]) {
				merged[counter] = leftPart[cursorLeft];
				cursorLeft++;
			} else {
				merged[counter] = rightPart[cursorRight];
				cursorRight++;
			}
			counter++;
		}
		if (cursorLeft < leftPart.length) {
			System.arraycopy(leftPart, cursorLeft, merged, counter, merged.length - counter);
		}
		if (cursorRight < rightPart.length) {
			System.arraycopy(rightPart, cursorRight, merged, counter, merged.length - counter);
		}
		return merged;
	}
}
