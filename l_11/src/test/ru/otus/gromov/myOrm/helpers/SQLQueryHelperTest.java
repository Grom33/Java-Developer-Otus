package ru.otus.gromov.myOrm.helpers;

import org.junit.Before;
import org.junit.Test;
import ru.otus.gromov.base.dataSets.AdressDataSet;
import ru.otus.gromov.base.dataSets.PhoneDataSet;
import ru.otus.gromov.base.dataSets.UserDataSet;

import java.util.Collections;

public class SQLQueryHelperTest {
	private SQLQueryHelper sqlExceptionHelper;
	private final UserDataSet TEST_DATASET_1 = new UserDataSet();

	@Before
	public void setup(){
		sqlExceptionHelper = new SQLQueryHelper();
		TEST_DATASET_1.setId(1);
		TEST_DATASET_1.setName("Ivan Ivanovich");
		TEST_DATASET_1.setAdress(new AdressDataSet("Moscow, street street"));
		TEST_DATASET_1.setPhones(Collections.singletonList(new PhoneDataSet("903-940-59-34", TEST_DATASET_1)));
	}

	@Test
	public void ripObject() {
		System.out.println(sqlExceptionHelper.buildQuery(TEST_DATASET_1));

	}
}