package ru.otus.gromov.base.dataSets;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "phonesDataSet")
public class PhoneDataSet extends DataSet {

    @Column(name = "number")
    private String number;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDataSet userDataSet;

    public PhoneDataSet() {
    }

    public PhoneDataSet(String number, UserDataSet userDataSet) {
        this.number = number;
        this.userDataSet = userDataSet;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
       if (this == o) return true;
       if (!(o instanceof PhoneDataSet)) return false;
       if (!super.equals(o)) return false;
        PhoneDataSet that = (PhoneDataSet) o;
        return number.equals(that.number) &&
                userDataSet.equals(that.userDataSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), number, userDataSet);
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "number='" + number + '\'' +
                '}';
    }
}
