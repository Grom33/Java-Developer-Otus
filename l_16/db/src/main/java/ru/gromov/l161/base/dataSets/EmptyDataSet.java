package ru.gromov.l161.base.dataSets;

import javax.persistence.*;


@Entity
public class EmptyDataSet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
}
