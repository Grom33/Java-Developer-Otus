package ru.otus.gromov.base.dataSets;

import javax.persistence.*;


@Entity
public class EmptyDataSet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
}
