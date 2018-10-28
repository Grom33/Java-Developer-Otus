package ru.otus.gromov.service;

import org.junit.Before;
import org.junit.Test;
import ru.otus.gromov.base.dataSets.AdressDataSet;
import ru.otus.gromov.base.dataSets.PhoneDataSet;
import ru.otus.gromov.base.dataSets.UserDataSet;
import ru.otus.gromov.myOrm.domain.MyUserDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class DBServiceMyOrmImplTest {

	private final UserDataSet TEST_DATASET_1 = new UserDataSet();
	private final UserDataSet TEST_DATASET_2 = new UserDataSet();
	private final UserDataSet TEST_DATASET_3 = new UserDataSet();
	private final List<UserDataSet> TEST_LIST_DATASET = new ArrayList<>();
	private DBService service;

	@Before
	public void setUp() throws Exception {
		service = new DBServiceMyOrmImpl();

		TEST_DATASET_1.setName("Petr Petrovich");
		TEST_DATASET_1.setAdress(new AdressDataSet("Moscow, street street"));
/*		TEST_DATASET_1.setPhones(Collections.singletonList(new PhoneDataSet("903-940-59-34", TEST_DATASET_1)));
		TEST_LIST_DATASET.add(TEST_DATASET_1);
		TEST_DATASET_2.setName("Petr Petrovich");
		TEST_DATASET_2.setAdress(new AdressDataSet("SPB, street street"));
		TEST_DATASET_2.setPhones(Collections.singletonList(new PhoneDataSet("911-220-59-34", TEST_DATASET_2)));
		TEST_LIST_DATASET.add(TEST_DATASET_2);
		TEST_DATASET_3.setName("Sidr Sidorovich");
		TEST_DATASET_3.setAdress(new AdressDataSet("Minsk, street street"));
		TEST_DATASET_3.setPhones(Collections.singletonList(new PhoneDataSet("999-970-59-34", TEST_DATASET_3)));
		TEST_LIST_DATASET.add(TEST_DATASET_3);*/
	}

	@Test
	public void getLocalStatus() {
	}

	@Test
	public void save() {
		service.save(TEST_DATASET_1);
	}

	@Test
	public void read() {
	}

	@Test
	public void readByName() {
	}

	@Test
	public void readAll() {
	}

	@Test
	public void shutdown() {
	}
}