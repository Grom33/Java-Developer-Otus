package ru.otus.gromov;

import ru.otus.gromov.annotation.After;
import ru.otus.gromov.annotation.Before;
import ru.otus.gromov.annotation.Test;

public class TestClass {

    private Integer A, B, C;

    public TestClass() {
    }

    @Before
    public void before() {
        A = 2;
        B = 2;
        C = 5;
    }

    @Test
    public void testOne() {
        Assert.assertEquals(A, B);
    }

    @Test
    public void testTwo() {
        Assert.assertEquals(C, (A + B));
    }

    @Test
    public void testThree() {
        Assert.assertEquals(null, C);
    }

    @After
    public void after() {
        A = null;
        B = null;
        C = null;
    }

}
