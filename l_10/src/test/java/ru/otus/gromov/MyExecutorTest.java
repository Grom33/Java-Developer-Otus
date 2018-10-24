package ru.otus.gromov;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MyExecutorTest {

    @Test
    public void initDbTest(){
        Executor executor = new MyExecutor();
        ((MyExecutor) executor).initDb(new UserDataSet(1l, "dsafd", 12));
    }
}