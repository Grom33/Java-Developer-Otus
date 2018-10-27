package ru.otus.gromov;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.otus.gromov.base.dataSets.AdressDataSet;
import ru.otus.gromov.base.dataSets.PhoneDataSet;
import ru.otus.gromov.base.dataSets.UserDataSet;
import ru.otus.gromov.myOrm.MyHibernate;
import ru.otus.gromov.service.DBService;
import ru.otus.gromov.service.DBServiceHibernateImpl;

public class MyHibernateTest {
	private final UserDataSet TEST_DATASET_1 = new UserDataSet("Ivan", new AdressDataSet("gsdfgsdfgsdfg"), new PhoneDataSet("123123"));
	//private final MyHibernate myHibernate = new MyHibernate();
	private final DBService service = new DBServiceHibernateImpl();


	@Before
	public void setUp(){
//		myHibernate.initDb("sa", "");
	}

	@Test
	public void saveAndLoad() {
////		myHibernate.save(TEST_DATASET_1);
//		UserDataSet actulDataset = (UserDataSet) myHibernate.load(
//				TEST_DATASET_1.getName(),
//				TEST_DATASET_1.getClass());

		service.save(TEST_DATASET_1);
		System.out.println(TEST_DATASET_1);
		UserDataSet userDataSet = service.read(TEST_DATASET_1.getId());
		System.out.println(userDataSet);




//		Assert.assertEquals("Test save and load dataset",
//				TEST_DATASET_1,
//				actulDataset );
	}
}