package ru.gromov.l161.base.dataSets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "adress")
public class AdressDataSet extends DataSet {
	@Column(name = "adress")
	private String adress;

	public AdressDataSet() {
	}

	public AdressDataSet(String adress) {
		this.adress = adress;
	}

	public AdressDataSet(long id, String adress) {
		super(id);
		this.adress = adress;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AdressDataSet)) return false;
		if (!super.equals(o)) return false;
		AdressDataSet that = (AdressDataSet) o;
		return adress.equals(that.adress);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), adress);
	}

	@Override
	public String toString() {
		return "AdressDataSet{" +
				"adress='" + adress + '\'' +
				'}';
	}
}
