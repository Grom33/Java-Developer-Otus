package ru.otus.gromov.dummy;

import java.io.Serializable;
import java.util.List;

public class Dummy implements Serializable {
    private int id;
    private long other;
    private String detail;

    private String[] someArray;
    private List andSomeCollection;

    private SubDummy subDummy;

    public Dummy(int id, long other, String detail, String[] somethingElse, List andSomeThingElse, SubDummy subDummy) {
        this.id = id;
        this.other = other;
        this.detail = detail;
        this.someArray = somethingElse;
        this.andSomeCollection = andSomeThingElse;
        this.subDummy = subDummy;
    }
}
