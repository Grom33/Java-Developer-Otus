package ru.otus.gromov;

import org.junit.Assert;
import org.junit.Test;

/**
 * VM options: -Xmx256m -Xms256m
 */

public abstract class AbstractMyCashImplTest {
    final static int SIZE = 1010;
    final static int LIFE_TIME_1000 = 1000;
    final static int IDLE_TIME_1000 = 1000;
    final static int LIFE_TIME_0 = 0;
    final static int IDLE_TIME_0 = 0;
    final static boolean ETERNAL_CASH = true;
    final static boolean NOT_ETERNAL_CASH = false;

    final static BigObject TEST_OBJECT = new BigObject();
    final static Long TEST_KEY_1L = 1L;

    private final static int ITERATIONS = 1000;


    MyCash<Long, BigObject> myCash;

    @Test
    public void putAndGet() {
        myCash.put(TEST_KEY_1L, TEST_OBJECT);
        Assert.assertEquals(myCash.get(TEST_KEY_1L), TEST_OBJECT);
    }

    @Test
    public void getValueAfterGCClean() {
        myCash.put(TEST_KEY_1L, TEST_OBJECT);
        fillCash(ITERATIONS);
        Assert.assertNull(myCash.get(TEST_KEY_1L));
    }

    @Test
    public void testCashSizeLimit() {
        myCash.put(TEST_KEY_1L, TEST_OBJECT);
        fillCash(SIZE);
        int hitCount = 0;
        for (int i = 2; i < SIZE + 1; i++) {
            if (myCash.get((long) i) != null) hitCount++;
        }
        Assert.assertTrue(SIZE >= hitCount);

    }

    @Test
    public abstract void getWithOverdue() throws InterruptedException;

    private void fillCash(int iterations) {
        for (int k = 2; k < iterations; k++) {
            myCash.put((long) k, new BigObject());
        }
    }

    static class BigObject {
        final byte[] array = new byte[1024 * 1024];

        public byte[] getArray() {
            return array;
        }
    }
}