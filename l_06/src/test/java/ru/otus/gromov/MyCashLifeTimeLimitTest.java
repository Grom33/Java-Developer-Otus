package ru.otus.gromov;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MyCashLifeTimeLimitTest extends AbstractMyCashImplTest {
    @Before
    public void setUp() {
        super.myCash = new MyCashImpl<>(SIZE, LIFE_TIME_1000, IDLE_TIME_0, NOT_ETERNAL_CASH);
    }

    @Override
    public void getWithOverdue() throws InterruptedException {
        myCash.put(TEST_KEY_1L, TEST_OBJECT);
        Thread.sleep(1500);
        Assert.assertNull(myCash.get(TEST_KEY_1L));
    }

    @Test
    public void testLifeTime() throws InterruptedException {

        myCash.put(TEST_KEY_1L, TEST_OBJECT);

        Thread.sleep(500);
        myCash.get(TEST_KEY_1L);

        Thread.sleep(500);
        myCash.get(TEST_KEY_1L);

        Thread.sleep(500);
        myCash.get(TEST_KEY_1L);

        Assert.assertNull(myCash.get(TEST_KEY_1L));
    }

    @Test
    public void testDispose() throws InterruptedException {
        myCash.put(TEST_KEY_1L, TEST_OBJECT);
        myCash.dispose();
        Thread.sleep(1500);
        Assert.assertEquals(myCash.get(TEST_KEY_1L), TEST_OBJECT);
    }
}
