package ru.otus.gromov.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

public class ObjFactory {
    private final Object[] ListOfObject = new Object[30];
    private int LastPositionInResultList = 0;

    public ObjFactory() {
        TestB testB = new TestB(123,"TESTTEST",12344L);
        TestC testC = new TestC(123,234,true,false);

        this.ListOfObject[0] = (new StringBuilder(1000));
        this.ListOfObject[1] = 0L;
        this.ListOfObject[2] = Long.MAX_VALUE;
        this.ListOfObject[3] = Integer.MAX_VALUE;
        this.ListOfObject[4] = "";
        this.ListOfObject[5] = "sdfgsdfgsdfg4e4";
        this.ListOfObject[6] = new String[]{"", "Tssdfgdfsg", "sdfgdfgdsfg"};
        this.ListOfObject[7] = new String[1000];
        this.ListOfObject[8] = new Object[1000];
        this.ListOfObject[9] = new ArrayList<String>();
        this.ListOfObject[10] = new MyClass();
        this.ListOfObject[11] = true;
        this.ListOfObject[12] = Short.MAX_VALUE;
        this.ListOfObject[13] = Byte.MAX_VALUE;
        this.ListOfObject[14] = Float.MAX_VALUE;
        this.ListOfObject[15]= Double.MAX_VALUE;
        this.ListOfObject[16] = testB;
        this.ListOfObject[17] = testC;
        this.ListOfObject[18] = new TestA(testB,testC,444);
    }

    public Object getSomeObject() {
        Object res = ListOfObject[LastPositionInResultList];
        if (LastPositionInResultList < (ListOfObject.length - 1)) {
            LastPositionInResultList++;
        } else {
            LastPositionInResultList = 0;
        }
        return res;
    }

    private class MyClass{

    }
}
