package ru.otus.gromov.base.dataSets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "phone")
public class AdressDataSet extends DataSet{

	@Column(name = "adress")
	private String adress;

	public AdressDataSet() {
	}

	public AdressDataSet(String adress) {
		this.adress = adress;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}
}
