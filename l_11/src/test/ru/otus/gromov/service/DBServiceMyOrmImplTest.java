package ru.otus.gromov.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.otus.gromov.base.dataSets.AdressDataSet;
import ru.otus.gromov.base.dataSets.PhoneDataSet;
import ru.otus.gromov.base.dataSets.UserDataSet;
import ru.otus.gromov.myOrm.exception.DBIsNotInstantiatedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
		TEST_DATASET_1.setId(1L);
		TEST_DATASET_1.setAdress(new AdressDataSet(1L, "Moscow, street street"));
		TEST_DATASET_1.setPhones(Arrays.asList(new PhoneDataSet(1L, "903-940-59-34", TEST_DATASET_1), new PhoneDataSet(2L, "911-943-59-34", TEST_DATASET_1)));
		TEST_LIST_DATASET.add(TEST_DATASET_1);
		TEST_DATASET_2.setName("Ivan Ivanovich");
		TEST_DATASET_2.setId(2L);
		TEST_DATASET_2.setAdress(new AdressDataSet(2L, "SPB, street street"));
		TEST_DATASET_2.setPhones(Collections.singletonList(new PhoneDataSet(4L, "911-220-59-34", TEST_DATASET_2)));
		TEST_LIST_DATASET.add(TEST_DATASET_2);
		TEST_DATASET_3.setName("Sidr Sidorovich");
		TEST_DATASET_3.setId(3L);
		TEST_DATASET_3.setAdress(new AdressDataSet(3L, "Minsk, street street"));
		TEST_DATASET_3.setPhones(Collections.singletonList(new PhoneDataSet(5L, "999-970-59-34", TEST_DATASET_3)));
		TEST_LIST_DATASET.add(TEST_DATASET_3);
	}

	@After
	public void reset() {
		service.shutdown();
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
		TEST_LIST_DATASET.forEach(t -> service.save(t));
		Assert.assertEquals(TEST_LIST_DATASET.size(), service.readAll().size());

	}

	@Test(expected = DBIsNotInstantiatedException.class)
	public void shutdown() {
		service.shutdown();
		service.read(TEST_DATASET_1.getId());
	}
}