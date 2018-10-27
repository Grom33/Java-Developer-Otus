package ru.otus.gromov.base.dataSets;

import javax.persistence.*;

@Entity
@Table(name = "userdataset")
public class UserDataSet extends DataSet {

    @Column(name = "name")

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private AdressDataSet adress;

    @OneToMany(cascade = CascadeType.ALL)
    private PhoneDataSet phone;

    //Important for Hibernate
    public UserDataSet() {
    }

    public UserDataSet(String name, AdressDataSet adress, PhoneDataSet phone) {
        this.name = name;
        this.adress = adress;
        this.phone = phone;
    }

    public AdressDataSet getAdress() {
        return adress;
    }

    public void setAdress(AdressDataSet adress) {
        this.adress = adress;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public PhoneDataSet getPhone() {
        return phone;
    }

    private void setPhone(PhoneDataSet phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "UserDataSet{" +
                "id'" + getId() + '\'' +
                "name='" + name + '\'' +
                ", phone=" + phone +
                '}';
    }
}

