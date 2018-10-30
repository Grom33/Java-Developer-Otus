package ru.otus.gromov.myOrm.helpers;

import org.junit.Before;
import org.junit.Test;
import ru.otus.gromov.base.dataSets.AdressDataSet;
import ru.otus.gromov.base.dataSets.PhoneDataSet;
import ru.otus.gromov.base.dataSets.UserDataSet;

import java.util.Collections;

import static org.junit.Assert.*;

public class ObjectHelperTest {
	private final UserDataSet TEST_DATASET_1 = new UserDataSet();

	@Before
	public void setUp() throws Exception {
		TEST_DATASET_1.setName("Ivan Ivanovich");
		TEST_DATASET_1.setAdress(new AdressDataSet("Moscow, street street"));
		TEST_DATASET_1.setPhones(Collections.singletonList(new PhoneDataSet("903-940-59-34", TEST_DATASET_1)));
	}

	@Test
	public void prepareInitQueryForObject() {
		System.out.println(ObjectHelper.prepareInitQueryForObject(TEST_DATASET_1));
	}

	@Test
	public void prepareValuesQueryForObject() {
		System.out.println(ObjectHelper.prepareValuesQueryForObject(TEST_DATASET_1));
	}
}