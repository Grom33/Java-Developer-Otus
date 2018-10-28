package ru.otus.gromov.myOrm.domain;

import java.util.Objects;

public class MyUserDataSet extends DataSet {
    private String name;
    private int age;
    private boolean checked;

    public MyUserDataSet() {
    }

    public MyUserDataSet(long id, String name, int age, boolean check) {
        super(id);
        this.name = name;
        this.age = age;
        this.checked = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserDataSet{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", checked=" + checked +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyUserDataSet that = (MyUserDataSet) o;
        return age == that.age &&
                checked == that.checked &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, checked);
    }
}
