package ru.otus.gromov.domain;

public class UserDataSet extends DataSet {
    private String name;
    private int age;
    private boolean checked;


    public UserDataSet(long id, String name, int age, boolean check) {
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
}
