package ru.otus.gromov.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.otus.gromov.base.dataSets.AdressDataSet;
import ru.otus.gromov.base.dataSets.PhoneDataSet;
import ru.otus.gromov.base.dataSets.UserDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DBServiceHibernateImplTest {

	private final UserDataSet TEST_DATASET_1 = new UserDataSet();
	private final UserDataSet TEST_DATASET_2 = new UserDataSet();
	private final UserDataSet TEST_DATASET_3 = new UserDataSet();
	private final List<UserDataSet> TEST_LIST_DATASET = new ArrayList<>();
	private DBService service;

	@Before
	public void setUp() {
		service = new DBServiceHibernateImpl();
		TEST_DATASET_1.setName("Ivan Ivanovich");
		TEST_DATASET_1.setAdress(new AdressDataSet("Moscow, street street"));
		TEST_DATASET_1.setPhones(Collections.singletonList(new PhoneDataSet("903-940-59-34", TEST_DATASET_1)));
		TEST_LIST_DATASET.add(TEST_DATASET_1);
		TEST_DATASET_2.setName("Petr Petrovich");
		TEST_DATASET_2.setAdress(new AdressDataSet("SPB, street street"));
		TEST_DATASET_2.setPhones(Collections.singletonList(new PhoneDataSet("911-220-59-34", TEST_DATASET_2)));
		TEST_LIST_DATASET.add(TEST_DATASET_2);
		TEST_DATASET_3.setName("Sidr Sidorovich");
		TEST_DATASET_3.setAdress(new AdressDataSet("Minsk, street street"));
		TEST_DATASET_3.setPhones(Collections.singletonList(new PhoneDataSet("999-970-59-34", TEST_DATASET_3)));
		TEST_LIST_DATASET.add(TEST_DATASET_3);

	}


	@Test
	public void getLocalStatus() {
		Assert.assertEquals("ACTIVE", service.getLocalStatus());

	}

	@Test
	public void readAndSave() {
		service.save(TEST_DATASET_1);
		UserDataSet actual = service.read(TEST_DATASET_1.getId());
		Assert.assertEquals(TEST_DATASET_1.getName(), actual.getName());
		Assert.assertEquals(TEST_DATASET_1.getAdress(), actual.getAdress());
		assertThat(TEST_DATASET_1.getPhones()).usingElementComparatorIgnoringFields("userDataSet").isEqualTo(actual.getPhones());
	}

	@Test
	public void readByName() {
		service.save(TEST_DATASET_1);
		UserDataSet actual = service.readByName(TEST_DATASET_1.getName());
		Assert.assertEquals(TEST_DATASET_1.getName(), actual.getName());
		Assert.assertEquals(TEST_DATASET_1.getAdress(), actual.getAdress());
		assertThat(TEST_DATASET_1.getPhones()).usingElementComparatorIgnoringFields("userDataSet").isEqualTo(actual.getPhones());
	}

	@Test
	public void readAll() {
		service.save(TEST_DATASET_1);
		service.save(TEST_DATASET_2);
		service.save(TEST_DATASET_3);
		List<UserDataSet> actualList = service.readAll();
		Assert.assertEquals(TEST_LIST_DATASET.size(), actualList.size());
	}

	@Test(expected = IllegalStateException.class)
	public void shutdown() {
		service.shutdown();
		service.save(TEST_DATASET_1);

	}
}