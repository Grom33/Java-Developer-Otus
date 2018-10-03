package ru.otus.gromov;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.otus.gromov.dummy.Dummy;
import ru.otus.gromov.dummy.SubDummy;

import java.util.Arrays;

import static org.junit.Assert.*;

public class JsonHelperTest {
    private static final Dummy DUMMY = new Dummy(12,
            1212122212,
            "TESTSTSTS",
            new String[]{"qwerqwer", "asdfasdf", "asdfsadf"},
            Arrays.asList("tttttt","yyyyyyy","uuuuuuu"),
            new SubDummy("popopo", 1234));

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void serialize() {
        String expected = new Gson().toJson(DUMMY);
        String actual = JsonHelper.serialize(DUMMY);
        System.out.println(actual);
        Assert.assertEquals(expected, actual);
    }
}