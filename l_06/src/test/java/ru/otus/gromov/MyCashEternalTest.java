package ru.otus.gromov;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MyCashEternalTest extends AbstractMyCashImplTest {

    @Before
    public void setUp(){
        super.myCash = new MyCashImpl<>(SIZE, LIFE_TIME_0, IDLE_TIME_0, ETERNAL_CASH);
    }

    @Test
    public void getWithOverdue() throws InterruptedException {
        myCash.put(TEST_KEY_1L, TEST_OBJECT);
        Thread.sleep(2000);
        Assert.assertEquals(myCash.get(TEST_KEY_1L), TEST_OBJECT);
    }
}
