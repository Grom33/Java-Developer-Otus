package ru.otus.gromov.base.dataSets;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "userdataset")
public class UserDataSet extends DataSet {

	@Column(name = "name")
	private String name;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private AdressDataSet adress;

	@OneToMany(mappedBy = "userDataSet", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<PhoneDataSet> phones = new ArrayList<>();

	//Important for Hibernate
	public UserDataSet() {
	}

	public UserDataSet(String name, AdressDataSet adress, List<PhoneDataSet> phones) {
		this.name = name;
		this.adress = adress;
		this.phones = phones;
	}

	public UserDataSet(long id, String name, AdressDataSet adress, List<PhoneDataSet> phones) {
		super(id);
		this.name = name;
		this.adress = adress;
		this.phones = phones;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AdressDataSet getAdress() {
		return adress;
	}

	public void setAdress(AdressDataSet adress) {
		this.adress = adress;
	}


	public List<PhoneDataSet> getPhones() {
		return phones;
	}

	public void setPhones(List<PhoneDataSet> phones) {
		this.phones = phones;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		if (!super.equals(object)) return false;
		UserDataSet that = (UserDataSet) object;
		return name.equals(that.name) &&
				adress.equals(that.adress) &&
				phones.equals(that.phones);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), name, adress, phones);
	}

	@Override
	public String toString() {
		return "UserDataSet{" +
				"name='" + name + '\'' +
				", adress=" + adress +
				", phones=" + phones +
				'}';
	}
}

