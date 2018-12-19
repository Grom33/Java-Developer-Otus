package ru.gromov.l161.base.dataSets;
import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
public class DataSet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	public DataSet(long id) {
		this.id = id;
	}

	public DataSet() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		//if (!(o instanceof DataSet)) return false;
		DataSet dataSet = (DataSet) o;
		return id == dataSet.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "DataSet{" +
				"id=" + id +
				'}';
	}
}
