package ru.otus.gromov;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.otus.gromov.domain.UserDataSet;

public class MyHibernateTest {
	private final UserDataSet TEST_DATASET_1 = new UserDataSet(2l, "Ivan Ivanov", 35, true);
	private final MyHibernate myHibernate = new MyHibernate();


	@Before
	public void setUp(){
		myHibernate.initDb("sa", "");
	}

	@Test
	public void saveAndLoad() {
		myHibernate.save(TEST_DATASET_1);
		UserDataSet actulDataset = (UserDataSet) myHibernate.load(
				TEST_DATASET_1.getId(),
				TEST_DATASET_1.getClass());

		Assert.assertEquals("Test save and load dataset",
				TEST_DATASET_1,
				actulDataset );
	}
}